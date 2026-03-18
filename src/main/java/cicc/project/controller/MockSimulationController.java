package cicc.project.controller;

import cicc.project.config.JwtUtil;
import cicc.project.entities.MockEndpoint;
import cicc.project.repository.MockEndpointRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mock")
public class MockSimulationController {

    @Autowired
    private MockEndpointRepository mockRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping("/**")
    public ResponseEntity<?> handleMockRequest(HttpServletRequest request) throws InterruptedException {
        String path = request.getRequestURI().replace("/api/v1/mock", "");
        String method = request.getMethod();

        Optional<MockEndpoint> mockOpt = mockRepository.findByPathAndMethod(path, method);

        if (mockOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Mock no encontrado para la ruta: " + path);
        }

        MockEndpoint mock = mockOpt.get();

        if (mock.getExpirationDate() != null && LocalDateTime.now().isAfter(mock.getExpirationDate())) {
            return ResponseEntity.status(410).body("Este endpoint ha expirado.");
        }
        if (mock.isJwtEnabled()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ") || 
                !jwtUtil.validateToken(authHeader.substring(7))) {
                return ResponseEntity.status(401).body("Acceso denegado: JWT inválido o ausente.");
            }
        }

        if (mock.getDelaySeconds() > 0) {
            Thread.sleep(mock.getDelaySeconds() * 1000L);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        mock.getHeaders().forEach(h -> {
            responseHeaders.add(h.getHeaderKey(), h.getHeaderValue());
        });
        
        if (mock.getContentType() != null) {
            responseHeaders.add("Content-Type", mock.getContentType());
        }

        return ResponseEntity
                .status(mock.getResponseCode())
                .headers(responseHeaders)
                .body(mock.getResponseBody());
    }
}