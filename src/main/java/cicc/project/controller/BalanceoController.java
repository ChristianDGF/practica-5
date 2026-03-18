package cicc.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession; // Usa javax.servlet.http.HttpSession si estás en Spring Boot 2.x
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class BalanceoController {

    // Inyectamos el puerto en el que está corriendo la app
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/estado")
    public String estadoInstancia(HttpSession session) {
        // Guardamos un atributo en la sesión si no existe para probar que no se pierde
        if (session.getAttribute("mensaje") == null) {
            session.setAttribute("mensaje", "¡Hola desde la sesión distribuida!");
        }

        String hostName = "Desconocido";
        try {
            // Esto obtendrá el ID del contenedor de Docker
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Devolvemos la información necesaria para validar el Round Robin
        return String.format(
            "<h1>Estado de la Instancia</h1>" +
            "<ul>" +
            "<li><b>ID del Contenedor (Hostname):</b> %s</li>" +
            "<li><b>Puerto Interno:</b> %s</li>" +
            "<li><b>ID de Sesión:</b> %s</li>" +
            "<li><b>Dato en Sesión:</b> %s</li>" +
            "</ul>",
            hostName, serverPort, session.getId(), session.getAttribute("mensaje")
        );
    }
}