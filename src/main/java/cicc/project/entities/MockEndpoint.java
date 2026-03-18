package cicc.project.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class MockEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String path;
    private String method; 

    @Column(name = "response_code")
    private int responseCode;

    private String contentType; 
    
    @Lob 
    private String responseBody; 

    private LocalDateTime expirationDate; 
    private int delaySeconds; 
    private boolean jwtEnabled;

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endpoint_id")
    private List<Header> headers; 
}