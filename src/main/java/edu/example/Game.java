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
import java.util.*;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date creationDate = new Date();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set <Score> scores = new HashSet<>();

    public Game () {
    }

    public Game (int timeOffset) {
        Date now = new Date();
        now.setTime( now.getTime() + timeOffset );
        this.creationDate = now;
    }


    //Methods getters and setters
    public Set <GamePlayer> getGamePlayers (){
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

    public boolean isFull(){
        if(this.gamePlayers.size() < 2)
            return false;
            else return true;
    }

    public boolean hasPlayer (Player player){
        return this.getGamePlayers().stream()
                .anyMatch(gp -> gp.getPlayer() == player);
    }

    public Set<Score> getScores() {
        return scores;
    }
}
