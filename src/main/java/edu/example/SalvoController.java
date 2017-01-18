package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


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
    @Autowired
    private ShipRepository shipsRepo;
    @Autowired
    private SalvoRepository salvoesRepo;

    @RequestMapping(value= "/games", method=RequestMethod.GET)
    private Map<String, Object> mapAllGames(Authentication auth) {
        //findAll() returns the list of games instances
        Map<String, Object> dto = new LinkedHashMap<>();

        if(!isGuest(auth))
            dto.put("currentPlayer", makePlayerDTO(getCurrentPlayer(auth)));

        dto.put("games", games.findAll().stream()
                .map(game -> makeGameDTO(game))
                .collect(toList()));
        return dto;
    }

    @RequestMapping(value = "/games", method=RequestMethod.POST)
    private ResponseEntity <Object> createNewGame(Authentication auth) {

        if(!isGuest(auth)){
            Player player = getCurrentPlayer(auth);
            Game game = new Game();
            games.save(game);
            GamePlayer gamePlayer = new GamePlayer(player, game);
            gamePlayers.save(gamePlayer);
            Map <String,Long> dto = new HashMap<>();
            dto.put("id",gamePlayer.getId());
            return new ResponseEntity(dto ,CREATED);}

        else return new ResponseEntity(FORBIDDEN);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    private ResponseEntity <HttpStatus> createShips(@RequestBody Set<Ship> ships,
                                                @PathVariable Long gamePlayerId,
                                                Authentication auth){

        Player player = getCurrentPlayer(auth);
        GamePlayer gamePlayer = gamePlayers.findOne(gamePlayerId);

        if (!isGuest(auth) && player.hasGamePlayer(gamePlayerId)){
            if (gamePlayer.getFleet().isEmpty()) {

                ships.forEach(ship -> ship.setGamePlayer(gamePlayer));
                ships.forEach(ship -> shipsRepo.save(ship));

                return new ResponseEntity(CREATED);
            }else
                return new ResponseEntity(FORBIDDEN);
        }else
            return new ResponseEntity(UNAUTHORIZED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/salvoes", method=RequestMethod.POST)
    private ResponseEntity <HttpStatus> fireSalvo(@RequestBody Salvo salvo,
                                                @PathVariable Long gamePlayerId,
                                                Authentication auth){

        Player player = getCurrentPlayer(auth);
        GamePlayer gamePlayer = gamePlayers.findOne(gamePlayerId);

        if (!isGuest(auth) && player.hasGamePlayer(gamePlayerId)){
            if (true) {
                salvo.setTurn(gamePlayer.getLastTurn() + 1);
                salvo.setGamePlayer(gamePlayer);
                salvoesRepo.save(salvo);

                return new ResponseEntity(CREATED);
            }else
                return new ResponseEntity(FORBIDDEN);
        }else
            return new ResponseEntity(UNAUTHORIZED);
    }


    @RequestMapping(value = "/game/{gameId}/players", method=RequestMethod.POST)
    private ResponseEntity <Object> joinGame(@PathVariable Long gameId, Authentication auth) {
        Game game = games.findOne(gameId);
        if(!isGuest(auth) && game != null && !game.isFull()){
            Player player = getCurrentPlayer(auth);

            if (!game.hasPlayer(player)){
            GamePlayer gamePlayer = new GamePlayer(player, game);
            gamePlayers.save(gamePlayer);

            Map <String,Long> dto = new HashMap<>();
            dto.put("id",gamePlayer.getId());
            return new ResponseEntity(dto,CREATED);
            }
            else return new ResponseEntity(FORBIDDEN);
        }
        else return new ResponseEntity(FORBIDDEN);
    }


    @RequestMapping("/player_stats")
    private Object mapLeaderBoard() {
        List<Player> allPlayers = players.findAll();
        return makePlayerStatsDTO(allPlayers);
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    private Object mapGameByGamePlayerId(@PathVariable Long gamePlayerId, Authentication auth) {

        GamePlayer gamePlayer = gamePlayers.findOne(gamePlayerId);
        //check if gameplayer belongs to the player that logged in
        if(gamePlayer.getPlayer().getUserName()== auth.getName())
            return makeGameViewDTO(gamePlayer);
            else return new ResponseEntity(FORBIDDEN);
    }

    @RequestMapping(value = "/players", method=RequestMethod.POST)
    public ResponseEntity <Object> addNewUser(@Valid PlayerForm playerForm,
                                              BindingResult bindingResult,
                                              @RequestParam("username") String username,
                                              @RequestParam("password") String password){

            if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(makeErrorDTO(bindingResult.getFieldErrors()),FORBIDDEN);
        }else{
            //database validation
            if(players.findByUserNameIgnoreCase(username) == null){
                Player player = new Player (username,password);
                players.save(player);
                return new ResponseEntity(CREATED);
            } else {
                return new ResponseEntity("username already exists",FORBIDDEN);
            }
        }
    }

    private Map<String, String> makeErrorDTO(List<FieldError> fieldErrors){
        Map<String, String> dto = new LinkedHashMap<>();
        fieldErrors.forEach(error ->
            dto.put(error.getField(),error.getDefaultMessage()));
        return dto;
    }

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

    private Map <String, Object> mapSalvoes(Set <GamePlayer> gamePlayers){
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
        Set<GamePlayer> gamePlayers = game.getGamePlayers();
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
           dto.put("userName", player.getUserName());
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
        Set<GamePlayer> gamePlayers = game.getGamePlayers();

        GamePlayer enemy = gamePlayers.stream()
                .filter(player -> player.getId() != playerId )
                .findAny()
                .orElse(null);

        return enemy;
    }

    private Player getCurrentPlayer(Authentication auth){
        return players.findByUserName(auth.getName()).get(0);
    }

    private boolean isGuest(Authentication auth) {
        return auth == null || auth instanceof AnonymousAuthenticationToken;
    }


}
