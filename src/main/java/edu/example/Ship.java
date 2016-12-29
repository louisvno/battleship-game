package edu.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 12/15/2016.
 */
@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_player")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="ship_locations")
    private List<String> shipLocations = new ArrayList<>();

    //constructors
    public Ship (){

    }
    public Ship (String type, List locations,GamePlayer gameplayer){
        this.shipType = type;
        this.shipLocations = locations;
        this.gamePlayer = gameplayer;
    }

    //methods
    public long getId() {
        return id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

}
