package edu.example.repositories;

import edu.example.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by louis on 12/15/2016.
 */

@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship, Long>{

}

