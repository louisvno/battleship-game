package edu.example.repositories;

/**
 * Created by louis on 12/13/2016.
 */
import edu.example.entities.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer,Long>{

}
