package edu.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//application context object = interface, does everything that the beanfactory does and more
import org.springframework.context.annotation.Bean;

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
	public CommandLineRunner initData(PlayerRepository repository) {

		return (args) -> {
//			save a couple of Players
			repository.save(new Player("Jack", "Bauer"));
			repository.save(new Player("Chloe", "O'Brian"));
			repository.save(new Player("Kim", "Bauer"));
			repository.save(new Player("David", "Palmer"));
			repository.save(new Player("Michelle", "Dessler"));
		};
	}
}
