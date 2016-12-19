package edu.example;

import javax.persistence.*;
import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by louis on 12/13/2016.
 */

//A game can have multiple players and a player can be in multiple games
//The GamePlayer table associates the players to their games
// Because game and player are references to objects stored in other data tables,
// you need to add JPA annotations to tell JPA how to connect the tables together.


/*For each row of the pet database table, include a column owner_id with the ID of the owner of the pet.
Given a pet, you can get the owner by retrieving the person with the stored owner_id.
Given a person, you can get all that person's pets by collecting all the rows of the pet table that have that person's ID in the owner_id column.

In JPA, we do this by


annotating this field with JPA to link it to the data table
define getOwner()  and setOwner() methods */

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //Properties
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player")
    private Player player;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game")
    private Game game;
    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER)
    private Set<Ship> ships;
    private LocalDateTime joinDate;
    //NOTE Default Constructor http://stackoverflow.com/questions/4488716/java-default-constructor

    public GamePlayer () {}

    public GamePlayer (Player player, Game game) {
        this.player = player;
        this.game = game;
        this.joinDate = LocalDateTime.now();
        this.ships = new HashSet<>();

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

    public void setJoinDate(LocalDateTime joinDate){
        this.joinDate = joinDate;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    public long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    //TODO ask Ferran for alternative method to connect gameplayer and ships
    public void addShip(Ship ship) {
        ship.setGamePlayer(this); //add gameplayer to ship
        this.ships.add(ship);
    }
}
