package cicc.project.entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Agregamos implements Serializable
public class User implements Serializable { 
    
    // Es buena práctica agregar un serialVersionUID
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}