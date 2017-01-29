package edu.example.repositories;

/**
 * Created by louis on 1/2/2017.
 */
import edu.example.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository <Score, Long> {
}
