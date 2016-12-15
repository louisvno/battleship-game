package edu.example;

/**
 * Created by louis on 12/7/2016.
 */
//Java Persistence API

        import java.text.SimpleDateFormat;
        import java.time.LocalDateTime;
        import java.time.ZonedDateTime;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.OneToMany;
        import javax.persistence.FetchType;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    //Properties
    private long id;
    private Date creationDate;
    //private Date today = new Date();
    //SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH);
    //String formattedDate = sdf.format(today);


    //one Game can have many gameplayers
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    public List <GamePlayer> gamePlayers;

    //constructors
    //public Game () {}

    public Game () {
        this.creationDate = new Date();

    }

    //Methods getters and setters
    public List <GamePlayer> getGamePlayers (){
        return gamePlayers;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    //Is this needed??
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String toString() {
        return ("game created at " + creationDate);
    }

    public long getId() {
        return id;
    }


}
