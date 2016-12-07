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
    public Player () {}
    //Constructor
    public Player(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }
    //Methods getters and setters
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
