package edu.example;

/**
 * Created by louis on 12/7/2016.
 */
//Java Persistence API

        import java.time.LocalDateTime;
        import java.time.ZonedDateTime;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.OneToMany;
        import javax.persistence.FetchType;
        import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    //Properties
    private long id;
    private LocalDateTime creationDate;

    //one Game can have many gameplayers
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    public List <GamePlayer> gamePlayers;

    //constructors
    //public Game () {}

    public Game () {
        this.creationDate = LocalDateTime.now();
    }

    //Methods getters and setters
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    //Is this needed??
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String toString() {
        return ("game created at " + creationDate);
    }
}
