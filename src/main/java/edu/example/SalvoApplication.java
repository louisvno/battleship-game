package edu.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//application context object = interface, does everything that the beanfactory does and more
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	//You use @Bean annotation to mark a method that returns an instance of a Java bean.
	// When Spring sees this, it will run the method and save the bean instance for later use.
	@Bean
    //the initData() method in the CommandLineRunner will need an instance of a PlayerRepository
    // Spring, as needed, will create an instance of a RestRepository and use it wherever one
    // is asked for.
	public CommandLineRunner initData(
			PlayerRepository players,
			GameRepository games,
			GamePlayerRepository gameplayers,
			ShipRepository ships) {

		return (args) -> {
			//create a few players
			Player p1 = new Player ("Arnie" , "Schwarz");
			Player p2 = new Player ("Silvester" , "Stallone");
			Player p3 = new Player ("Jean Claude" , "Vandamme");
            //save the Players
			players.save(p1);
			players.save(p2);
			players.save(p3);
			//create a few games
			//TODO make time difference
			Game g1 = new Game ();
			Game g2 = new Game ();
			Game g3 = new Game ();
            //save games
            games.save(g1);
            games.save(g2);
            games.save(g3);
			//create some gameplayers
			GamePlayer gp1 = new GamePlayer(p1,g1);
			GamePlayer gp2 = new GamePlayer(p2,g2);
			GamePlayer gp3 = new GamePlayer(p3,g3);
			GamePlayer gp4 = new GamePlayer(p2,g3);
			//create some ship types
			String type1 = "destroyer";
			String type2 = "submarine";
			String type3 = "titanic";
			String type4 = "black pearl";
			//TODO add Gameplayer to ship as currently the ships dont have a gameplayer assigned
			// TODO (cont')  therefore the database cannot find the ship corresponding to the gampelayer

			//create some ship locations
			List<String> loc1 = new ArrayList<>();
			List<String> loc2 = new ArrayList<>();
			List<String> loc3 = new ArrayList<>();
			loc1.add("H1");
			loc1.add("H2");
			loc2.add("H3");
			loc2.add("H4");
			loc3.add("H5");
			loc3.add("H6");
			loc3.add("H7");
			loc3.add("H8");

            //create some ships
			Ship ship1 = new Ship(type1,loc1);
			Ship ship2 = new Ship(type2,loc2);
			Ship ship3 = new Ship(type4,loc3);
			//save ships
			ships.save(ship1);
			ships.save(ship2);
			ships.save(ship3);

			gp1.addShip(ship1);
			gp1.addShip(ship1);
            gp2.addShip(ship2);
            gp2.addShip(ship3);
			gp3.addShip(ship2);
            gp3.addShip(ship3);
            gp4.addShip(ship2);
            gp4.addShip(ship3);


            //save some gameplayers
            gameplayers.save(gp1);
            gameplayers.save(gp2);
            gameplayers.save(gp3);
            gameplayers.save(gp4);

		};
	}

}
