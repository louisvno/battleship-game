package edu.example;

import javax.persistence.*;
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
    @JoinColumn(name="gamePlayer")
    private GamePlayer gamePlayer;
    @ElementCollection
    @Column(name="shipLocations")
    private List<String> shipLocations;

    //constructors
    public Ship (){

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
