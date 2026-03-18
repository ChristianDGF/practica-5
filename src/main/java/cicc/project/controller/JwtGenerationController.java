package cicc.project.controller;

import cicc.project.config.JwtUtil;
import cicc.project.entities.MockEndpoint;
import cicc.project.repository.MockEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manage/tokens")
public class JwtGenerationController {

    @Autowired
    private MockEndpointRepository mockRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/generate/{id}")
    public ResponseEntity<?> getMockToken(@PathVariable Long id) {
        return mockRepository.findById(id).map(mock -> {
            String token = jwtUtil.generateToken("admin", mock.getExpirationDate());
            
            Map<String, String> response = new HashMap<>();
            response.put("mockName", mock.getName());
            response.put("token", token);
            response.put("bearer", "Bearer " + token);
            response.put("expiresAt", mock.getExpirationDate().toString());
            
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
}