package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @Autowired
    private GamePlayerRepository gamePlayers;

    @RequestMapping("/games")
    private List<Object> mapAllGames() {
        //findAll() returns the list of games instances
        return
                games.findAll().stream()
                        .map(game -> makeGameDTO(game))
                        .collect(toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    private Object mapGameByGamePlayerId(@PathVariable Long gamePlayerId) {
        //findOne() returns the instance of gamePlayer with the ID that you pass as parameter
        GamePlayer gamePlayer = gamePlayers.findOne(gamePlayerId);
        Game game = gamePlayer.getGame();
        Map gamePlayerMap = makeGameDTO(game);
            gamePlayerMap.putAll(makeShipsDTO(gamePlayer));

        return gamePlayerMap;
    }

    private Map<String, Object> makeShipsDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("ships", mapShipsFromGamePlayer(gamePlayer));
        return dto;
    }

    private List<Object> mapShipsFromGamePlayer(GamePlayer gamePlayer){
        return
             gamePlayer.getFleet().stream()
                .map(ship -> makeShipDataDTO(ship))
                .collect(toList());
    }

    private Map<String, Object> makeShipDataDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("locations", ship.getShipLocations());
        dto.put("types", ship.getShipType());
        return dto;
    }

    //DTO = Data transfer object
    // = decide from your class which data do you want to send in you JSON
    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", mapGamePlayersFromGame(game)); //add method that returns Gameplayers

        return dto;
    }
    //Because every Game instance has a list of GamePlayers
    //you can map the GamePlayers by getting this list
    private List<Object> mapGamePlayersFromGame(Game game) {
        return
                game.getGamePlayers().stream()
                        .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                        .collect(toList());
    }
    //NOTE if you dont use DTO, spring looks at getters of the Class and returns all, this maybe unwanted

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("id", gamePlayer.getId());
           dto.put("player", mapPlayerFromGamePlayer(gamePlayer));
        return dto;
    }
    private Object mapPlayerFromGamePlayer(GamePlayer gameplayer) {
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
