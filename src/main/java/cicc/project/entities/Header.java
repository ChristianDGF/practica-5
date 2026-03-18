package cicc.project.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Header {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String headerKey;
    private String headerValue;
}