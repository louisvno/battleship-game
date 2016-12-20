package edu.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//application context object = interface, does everything that the beanfactory does and more
import org.springframework.context.annotation.Bean;
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
			PlayerRepository playerRepo,
			GameRepository games,
			GamePlayerRepository gameplayerRepo,
			ShipRepository shipRepo) {

		return (args) -> {
			//create a few players
			Player p1 = new Player ("Arnie" , "Schwarz");
			Player p2 = new Player ("Silvester" , "Stallone");
			Player p3 = new Player ("Jean Claude" , "Vandamme");
            //save the Players
			playerRepo.save(p1);
			playerRepo.save(p2);
			playerRepo.save(p3);
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
			//save some gameplayers
			gameplayerRepo.save(gp1);
			gameplayerRepo.save(gp2);
			gameplayerRepo.save(gp3);
			gameplayerRepo.save(gp4);

			//create some ship types
			String type1 = "destroyer";
			String type2 = "submarine";
			String type3 = "titanic";
			String type4 = "black pearl";

			//create some ship locations
			List<String> loc1 = new ArrayList<>();
			List<String> loc2 = new ArrayList<>();
			List<String> loc3 = new ArrayList<>();
			List<String> loc4 = new ArrayList<>();
			List<String> loc5 = new ArrayList<>();
			loc1.add("H1");
			loc1.add("H2");
			loc2.add("B1");
			loc2.add("C1");
			loc3.add("B4");
			loc3.add("B5");
			loc3.add("B6");
			loc3.add("B7");
			loc4.add("D10");
			loc4.add("E10");
			loc5.add("J4");
			loc5.add("J5");
			loc5.add("J6");
			loc5.add("J7");

            //create some ships
			Ship ship1 = new Ship(type1,loc1);
			Ship ship2 = new Ship(type2,loc2);
			Ship ship3 = new Ship(type4,loc3);
			Ship ship4 = new Ship(type1,loc4);
			Ship ship5 = new Ship(type4,loc3);
			Ship ship6 = new Ship(type4,loc5);

			//Add ship to GamePlayer ( -> Java Instance) and  Gameplayer to ship (-> Database Table)
			gp1.addShip(ship2);
			gp1.addShip(ship1);
			gp2.addShip(ship3);
			gp3.addShip(ship4);
			gp4.addShip(ship5);
			gp4.addShip(ship6);

            //save ships
            shipRepo.save(ship1);
            shipRepo.save(ship2);
            shipRepo.save(ship3);
            shipRepo.save(ship4);
            shipRepo.save(ship5);
            shipRepo.save(ship6);


		};
	}

}
