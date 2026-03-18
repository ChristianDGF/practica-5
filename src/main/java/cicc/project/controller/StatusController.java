package cicc.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    // Capturamos el puerto desde las propiedades (o la variable de entorno de Docker)
    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping
    public ResponseEntity<Map<String, String>> getServerInfo(HttpSession session) {
        Map<String, String> info = new HashMap<>();
        
        String containerId = "Desconocido";
        try {
            // En un entorno Docker, el Hostname corresponde al ID del contenedor
            containerId = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Guardamos un contador en la sesión para demostrar que la sesión se mantiene 
        // a pesar de que cambiemos de contenedor
        Integer contador = (Integer) session.getAttribute("contador_visitas");
        if (contador == null) {
            contador = 1;
        } else {
            contador++;
        }
        session.setAttribute("contador_visitas", contador);

        info.put("mensaje", "Respondiendo desde la instancia clonada");
        info.put("puerto_interno", serverPort);
        info.put("id_contenedor", containerId);
        info.put("id_sesion_activa", session.getId());
        info.put("visitas_en_esta_sesion", String.valueOf(contador));

        return ResponseEntity.ok(info);
    }
}