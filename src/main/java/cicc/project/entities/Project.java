package cicc.project.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private User owner; 

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<MockEndpoint> endpoints;
}