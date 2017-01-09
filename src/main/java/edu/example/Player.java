//"Domain object" to represent a player (object to store player data)
package edu.example;

//Java Persistence API
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.List;

/**
 * Created by louis on 12/5/2016.
 */

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;


    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List <GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private List <Score> scores;

    public Player () {}

    public Player(String userName, String password) {
        new Player("", "", userName,password);
    }

    public Player(String first, String last, String user, String password) {
        this.firstName = first;
        this.lastName = last;
        this.userName = user;
        this.password = password;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
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

    public long getId() {
        return id;
    }

    public String toString() {
        return (firstName + " " + lastName);
    }

    public double getTotalScore(){
        return this.scores.stream()
            .map(Score::getScore)
                .reduce(0.0,(a, b) -> a + b);
    }

    public long getWins(){
        return this.scores.stream()
                .map(Score::getScore)
                .filter(score -> score == 1.0)
                .count();
    }

    public long getLosses(){
        return this.scores.stream()
                .map(Score::getScore)
                .filter(score -> score == 0.0)
                .count();
    }

    public long getTies(){
        return this.scores.stream()
                .map(Score::getScore)
                .filter(score -> score == 0.5)
                .count();
    }

    public long getGamesPlayed(){
        return this.scores.stream()
                .map(Score::getScore)
                .count();
    }
}
