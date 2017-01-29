package edu.example.entities;

import edu.example.entities.GamePlayer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 12/20/2016.
 */
@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ElementCollection
    @Column(name="targets")
    private List <String> targets; //every salvo can have 2 targets

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_player_id")
    private GamePlayer gamePlayer;
    private Integer turn;

    public Salvo(){
    }
    public Salvo(List<String> targets){
        this.targets = new ArrayList<>();
        this.targets.addAll(targets);
    }

    public Salvo(List<String> targets, GamePlayer gamePlayer, int turn){
        this.targets = new ArrayList<>();
        this.targets.addAll(targets);
        //this.gamePlayer = gamePlayer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
