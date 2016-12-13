//"Domain object" to represent a player (object to store player data)
package edu.example;

//Java Persistence API
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by louis on 12/5/2016.
 */

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    //Properties
    private long id;
    private String firstName;
    private String lastName;
    //NOTE Default Constructor http://stackoverflow.com/questions/4488716/java-default-constructor
    //Needed for deserialization (process of converting a JSON obj to an instance of a Java class)

    //Algorithm Jackson calls new Player() to create an empty instance p1 of Player.
    //For each key and value in obj (JSON), Jackson calls p1.setFirstName(value) or
    // p1.firstName = value, whichever of these is public.

    //This process will break if there is no no-argument constructor,
    // if the JSON is ill-formed, or if the JSON contains keys that don't match object,
    // or if the necessary field or setter is not public.
    public Player () {}
    //Constructor
    public Player(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }
    //Methods getters and setters
    //These methods are used to get the data from the frontend and then set the data to create
    //a new instance of a player
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return (firstName + " " + lastName);
    }
}
