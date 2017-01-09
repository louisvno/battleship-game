/*The repository is an INTERFACE and will allow you to perform various operations
 involving Person objects.

 At runtime, Spring Data REST will create an implementation
 of this interface automatically. Then it will use the @RepositoryRestResource
 annotation to direct Spring MVC to create RESTful endpoints at /people.
 @RepositoryRestResource is not required for a repository to be exported.
 It is only used to change the export details, such as using /people instead of
 the default value of /persons. */

package edu.example;

/**
 * Created by louis on 12/5/2016.
 */

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.repository.query.Param;
        import org.springframework.data.rest.core.annotation.RepositoryRestResource;
        import java.util.List;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
        List<Player> findByFirstName(String firstName);

        List<Player> findByUserName(String name);//
        Player findByUserNameIgnoreCase(String name);//
}
