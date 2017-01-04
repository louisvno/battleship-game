package edu.example;

/**
 * Created by louis on 12/7/2016.
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date creationDate = new Date();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private List <GamePlayer> gamePlayers = new ArrayList<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private List <Score> scores;

    public Game () {
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
