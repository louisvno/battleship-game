package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            PlayerRepository playerRepo,
            GameRepository games,
            GamePlayerRepository gameplayerRepo,
            ShipRepository shipRepo,
            SalvoRepository salvoRepo,
            ScoreRepository ScoreRepo) {

        return (args) -> {
            //create a few players
            Player p1 = new Player("Arnie", "Schwarz", "arnie1947", "terminator1");
            Player p2 = new Player("Silvester", "Stallone", "Rambo", "erm844");
            Player p3 = new Player("Jean Claude", "Vandamme", "Killer101", "hola908");
            Player p4 = new Player("Lionel", "Messi", "Lio", "papoi90");
            Player p5 = new Player("Eric", "Cartman", "Eric2000", "goinghome1");
            Player p6 = new Player("Kanye", "West", "Fishstick", "aintnohobbit");
            Player p7 = new Player("Donald", "Trump", "Donnie", "ilovehilary");
            Player p8 = new Player("Penelope", "Cruz", "LaReina", "poiwe2");
            Player p9 = new Player("Tom", "Cruise", "CaptainAwesome", "pppooe0");
            Player p10 = new Player("Rutger", "Hauer", "Harry", "123456");


            //save the Players
            playerRepo.save(p1);
            playerRepo.save(p2);
            playerRepo.save(p3);
            playerRepo.save(p4);
            playerRepo.save(p5);
            playerRepo.save(p6);
            playerRepo.save(p7);
            playerRepo.save(p8);
            playerRepo.save(p9);
            playerRepo.save(p10);

            //create a few games
            Game g1 = new Game();
            Game g2 = new Game(3600000);
            Game g3 = new Game(7200000);

            //save games
            games.save(g1);
            games.save(g2);
            games.save(g3);

            //create some gameplayers
            GamePlayer gp1 = new GamePlayer(p1, g1);
            GamePlayer gp2 = new GamePlayer(p2, g2);
            GamePlayer gp3 = new GamePlayer(p3, g3);
            GamePlayer gp4 = new GamePlayer(p2, g3);

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
            Ship ship1 = new Ship(type1, loc1, gp1);
            Ship ship2 = new Ship(type2, loc2, gp1);
            Ship ship3 = new Ship(type4, loc3, gp2);
            Ship ship4 = new Ship(type1, loc4, gp3);
            Ship ship5 = new Ship(type4, loc3, gp4);
            Ship ship6 = new Ship(type4, loc5, gp4);

            //save ships
            shipRepo.save(ship1);
            shipRepo.save(ship2);
            shipRepo.save(ship3);
            shipRepo.save(ship4);
            shipRepo.save(ship5);
            shipRepo.save(ship6);

            //create some salvo targets
            List<String> targ1 = new ArrayList<>();
            List<String> targ2 = new ArrayList<>();
            List<String> targ3 = new ArrayList<>();
            List<String> targ4 = new ArrayList<>();

            //add some targets
            targ1.add("A4");
            targ1.add("B7");
            targ2.add("C3");
            targ2.add("A7");
            targ3.add("J6");
            targ3.add("I7");
            targ4.add("J4");
            targ4.add("D10");

            //create some salvoes
            Salvo salvo1 = new Salvo(targ1, gp1, 1);
            Salvo salvo2 = new Salvo(targ2, gp2, 1);
            Salvo salvo3 = new Salvo(targ3, gp3, 1);
            Salvo salvo4 = new Salvo(targ4, gp4, 1);
            Salvo salvo5 = new Salvo(targ1, gp4, 2);


            salvoRepo.save(salvo1);
            salvoRepo.save(salvo2);
            salvoRepo.save(salvo3);
            salvoRepo.save(salvo4);
            salvoRepo.save(salvo5);

            Score score1 = new Score(p1, g1, 1.0);
            Score score2 = new Score(p2, g1, 0.0);
            Score score3 = new Score(p3, g2, 1.0);
            Score score4 = new Score(p2, g2, 1.0);
            Score score5 = new Score(p2, g2, 1.0);
            Score score6 = new Score(p2, g2, 1.0);

            ScoreRepo.save(score1);
            ScoreRepo.save(score2);
            ScoreRepo.save(score3);
            ScoreRepo.save(score4);
            ScoreRepo.save(score5);
            ScoreRepo.save(score6);

        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepo;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                List<Player> players = playerRepo.findByUserName(name);
                if (!players.isEmpty()) {
                    Player player = players.get(0);
                    return new User(player.getUserName(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + name);
                }
            }
        };
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/api/players",
                        "/api/games",
                        "/games.html",
                        "/styles/**",
                        "/scripts/**",
                        "/api/login")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/api/login")
                .and()
                .logout().logoutUrl("/api/logout");

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}


