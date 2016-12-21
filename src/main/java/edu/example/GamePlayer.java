package edu.example;

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
    @JoinColumn(name="player")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game")
    private Game game;

    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER)
    private Set<Ship> fleet;

    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER)
    private List <Salvo> salvoes;

    private Date joinDate;
    public GamePlayer () {}

    public GamePlayer (Player player, Game game) {
        this.player = player;
        this.game = game;
        this.joinDate = new Date();
        this.fleet = new HashSet<>();
        this.salvoes = new LinkedList<>();

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

    public void fireSalvo(Salvo salvo){
        salvo.setGamePlayer(this);//assign gameplayer to salvo
        this.salvoes.add(salvo);//add salvo to gameplayer salvoes
    }

    //TODO ask Ferran for alternative method to connect gameplayer and fleet
    public void addShip(Ship ship) {
        ship.setGamePlayer(this); //assign gameplayer to ship
        this.fleet.add(ship); //add ship to fleet
    }
    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }
}
