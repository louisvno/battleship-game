package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;



/**
 * Created by louis on 12/14/2016.
 */
@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository games;

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        //findAll() returns the list of games instances
        return
                games.findAll().stream()
                        .map(game -> makeGameDTO(game))
                        .collect(toList());
    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", getGameplayersFromGame(game)); //add method that returns Gameplayers
        return dto;
    }
    //from the game return a list of gameplayers
    public List<Object> getGameplayersFromGame(Game game) {
        return
                game.getGamePlayers().stream()
                        .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                        .collect(toList());
    }
    //NOTE if you dont use DTO, spring looks at getters of the Class and returns all, this maybe unwanted

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("id", gamePlayer.getId());
           dto.put("player", getPlayerFromGamePlayer(gamePlayer));
        return dto;
    }
    public Object getPlayerFromGamePlayer(GamePlayer gameplayer) {
        //List<Object> list = new ArrayList <>();

        return makePlayerDTO(gameplayer.getPlayer());
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("id", player.getId());
           dto.put("firstName", player.getFirstName());
        return dto;
    }

}
