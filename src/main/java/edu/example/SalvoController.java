package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private PlayerRepository players;

    @RequestMapping("/games")
    private List<Object> mapAllGames() {
        //findAll() returns the list of games instances
        return
                games.findAll().stream()
                        .map(game -> makeGameDTO(game))
                        .collect(toList());
    }

    @RequestMapping("/player_stats")
    private Object mapLeaderBoard() {

        List<Player> allPlayers = players.findAll();

        return makePlayerStatsDTO(allPlayers);

    }
    //TODO ask about Object
    @RequestMapping("/game_view/{gamePlayerId}")
    private Object mapGameByGamePlayerId(@PathVariable Long gamePlayerId) {
        //findOne() returns the instance of gamePlayer with the ID that you pass as parameter
        GamePlayer gamePlayer = gamePlayers.findOne(gamePlayerId);

        return makeGameViewDTO(gamePlayer);
    }
    //TODO finish dto mapping
    //rank, player(id, firstname), total score
    private Map<String, Object> makePlayerStatsDTO(List<Player> allPlayers){
        Map<String, Object> dto = new LinkedHashMap<>();

        List<Player> sortedPlayers = allPlayers.stream()
                .sorted((p1,p2) -> Double.compare(p2.getTotalScore(),p1.getTotalScore()))
                //.limit(5)
                .collect(toList());

        dto.put("players", sortedPlayers.stream()
                .map(player -> makePlayerStatsDTO(player))
                .collect(Collectors.toList()));

        return dto;
    }
//    private Map<String, Object> makeRankDTO(List<Player> players) {
//        Map<String, Object> dto = new LinkedHashMap<>();
//        int i= 1;
//        for(Player p : players) {
//            dto.put(Integer.valueOf(i).toString(), makeRankedPlayerDTO(p));
//            i++;
//        }
//        return dto;
//    }

    private Map<String, Object> makePlayerStatsDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();

            dto.put("id", player.getId());
            dto.put("firstName", player.getFirstName());
            dto.put("wins", player.getWins());
            dto.put("losses", player.getLosses());
            dto.put("ties", player.getTies());
            dto.put("totalScore", player.getTotalScore());
            dto.put("gamesPlayed", player.getGamesPlayed());

        return dto;
    }

    private Map<String,Object> makeGameViewDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        Game game = gamePlayer.getGame();

        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", mapGamePlayersFromGame(game));
        dto.put("fleet", mapFleetFromGamePlayer(gamePlayer));
        dto.put("salvoes", mapSalvoes(game.getGamePlayers()));

        return dto;
    }

    private Map <String, Object> mapSalvoes(List <GamePlayer> gamePlayers){
        Map<String, Object> dto = new LinkedHashMap<>();

        gamePlayers.forEach(gamePlayer -> {
            dto.put(Long.valueOf(gamePlayer.getId()).toString(),
                    mapTurns(gamePlayer.getSalvoes()));

        });
        return dto;
    }

    private Map <String, Object> mapTurns( List <Salvo> salvoes){
        Map<String, Object> dto = new LinkedHashMap<>();

        salvoes.forEach( salvo -> {
            dto.put(salvo.getTurn().toString(), mapSalvo(salvo));
        });
        return dto;
    }

    private Map <String, Object> mapSalvo(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("targets", salvo.getTargets());
        dto.put("hits", getTargetsHit(salvo));
        dto.put("missed", getTargetsMissed(salvo));

        return dto;
    }

    private List<Object> mapFleetFromGamePlayer(GamePlayer gamePlayer){
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

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", mapGamePlayersFromGame(game));


        return dto;
    }

    private Map<String, Object> mapGamePlayersFromGame(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        List<GamePlayer> gamePlayers = game.getGamePlayers();
        gamePlayers.forEach( gamePlayer ->
                dto.put(
                        Long.valueOf(gamePlayer.getId()).toString()
                        , makeGamePlayerDTO(gamePlayer)));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("id", gamePlayer.getId());
           dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("id", player.getId());
           dto.put("firstName", player.getFirstName());
        return dto;
    }

    /*
    * Game Logic
    * */

    private List <String> getAllShipLocations(GamePlayer gamePlayer) {
        Set<Ship> fleet = gamePlayer.getFleet();
        List<String> allLocations = fleet.stream()
                .map(Ship::getShipLocations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return allLocations;
    }

    private List<String> getTargetsHit (Salvo salvo){
        GamePlayer enemy = getEnemy(salvo.getGamePlayer());
        if (enemy != null){
            List <String> salvoTargets = salvo.getTargets();
            List <String> enemyShipLocations = getAllShipLocations(enemy);

            List<String> targetsHit = salvoTargets.stream()
                    .filter(enemyShipLocations::contains)
                    .collect(toList());
            return targetsHit;
        }
        else return null;
    }

    private List<String> getTargetsMissed (Salvo salvo){
        GamePlayer enemy = getEnemy(salvo.getGamePlayer());
        if (enemy != null){
            List <String> salvoTargets = salvo.getTargets();
            List <String> enemyShipLocations = getAllShipLocations(enemy);

            List<String> targetsHit = salvoTargets.stream()
                    .filter(target -> !enemyShipLocations.contains(target))
                    .collect(toList());
            return targetsHit;
        }
        else return null;
    }

    private GamePlayer getEnemy (GamePlayer gamePlayer) {
        Long playerId = gamePlayer.getId();
        Game game = gamePlayer.getGame();
        List<GamePlayer> gamePlayers = game.getGamePlayers();

        GamePlayer enemy = gamePlayers.stream()
                .filter(player -> player.getId() != playerId )
                .findAny()
                .orElse(null);

        return enemy;
    }

}
