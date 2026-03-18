package cicc.project.repository;

import cicc.project.entities.MockEndpoint;
import cicc.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MockEndpointRepository extends JpaRepository<MockEndpoint, Long> {
    Optional<MockEndpoint> findByPathAndMethod(String path, String method);
    List<MockEndpoint> findByProjectOwner(User owner);
}