package edu.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private int turn;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer")
    private GamePlayer gamePlayer;

    public Salvo(){

    }

    public Salvo(List<String> targets){
        this.targets = new ArrayList<>();
        this.targets.addAll(targets);
        this.turn = 0;
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
