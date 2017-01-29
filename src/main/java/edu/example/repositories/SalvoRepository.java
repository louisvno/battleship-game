package edu.example.repositories;

import edu.example.entities.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by louis on 12/20/2016.
 */
@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo, Long> {

}
