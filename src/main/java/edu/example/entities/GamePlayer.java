package edu.example.entities;

import javax.persistence.*;
import java.util.*;

/**
 * Created by louis on 12/13/2016.
 */

@Entity
public class GamePlayer {

    //Properties
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER)
    private Set<Ship> fleet = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER)
    private Set <Salvo> salvoes = new HashSet<>();

    private Date joinDate = new Date();
    public GamePlayer () {}

    public GamePlayer (Player player, Game game) {
        this.player = player;
        this.game = game;

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public long getId() {
        return id;
    }

    public Set<Ship> getFleet() {
        return fleet;
    }

    public void setFleet(Set<Ship> fleet) {
        this.fleet = fleet;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public boolean hasPlayedTurn (int turn) {
        return this.getSalvoes().stream()
                                .map(s -> s.getTurn())
                                .anyMatch(t -> t == turn);
    }

    public Integer getLastTurn () {
        if (!this.getSalvoes().isEmpty()) {
            return this.getSalvoes().stream()
                    .map(s -> s.getTurn())
                    .max(Integer::compare)
                    .get();
        } else return 0;
    }

    public void fireSalvo(Salvo salvo){
        //salvo.setGamePlayer(this);//assign gameplayer to salvo
        this.salvoes.add(salvo);//add salvo to gameplayer salvoes
    }

}
