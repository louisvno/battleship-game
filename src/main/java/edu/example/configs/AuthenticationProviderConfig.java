package edu.example.configs;

import edu.example.entities.Player;
import edu.example.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Created by louis on 1/25/2017.
 */

@Configuration
public class AuthenticationProviderConfig extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepo;
    /**As for the configureGlobal(AuthenticationManagerBuilder) method,
     *  it sets up an in-memory user store with a single user.
     *  That user is given a username of "user", a password of "password", and a role of "USER".
    */
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }
    //inline implementation of UserDetailsService interface
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

