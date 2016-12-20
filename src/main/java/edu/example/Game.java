package edu.example;

/**
 * Created by louis on 12/7/2016.
 */
//Java Persistence API

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.OneToMany;
        import javax.persistence.FetchType;
        import java.util.Date;
        import java.util.List;


@Entity
public class Game {

    //Properties
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date creationDate;

    //one Game can have many gameplayers
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private List <GamePlayer> gamePlayers;

    //constructors
    public Game () {
        this.creationDate = new Date();
    }
    public Game (int timeOffset) {
        Date now = new Date();
        now.setTime( now.getTime() + timeOffset );
        this.creationDate = now;
    }


    //Methods getters and setters
    public List <GamePlayer> getGamePlayers (){
        return gamePlayers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String toString() {
        return ("game created at " + creationDate);
    }

    public long getId() {
        return id;
    }


}
