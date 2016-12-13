package edu.example;

/**
 * Created by louis on 12/7/2016.
 */
//Java Persistence API
        import java.time.LocalDateTime;
        import java.util.Date;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    //Properties
    private long id;
    private LocalDateTime creationDate;
    //NOTE Default Constructor http://stackoverflow.com/questions/4488716/java-default-constructor
    public Game () {}

    //Methods getters and setters
    public String getCreationDate() {
        return creationDate.toString();
    }

    public void setCreationDate() {
        this.creationDate = LocalDateTime.now();
    }

    public String toString() {
        return ("game created at " + creationDate);
    }
}
