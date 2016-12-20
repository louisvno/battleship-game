//UI controller
loadGameData(getGamePlayerId());

//get url game number parameter
function getGamePlayerId(){
    var url = window.location.href;
    var gameViewId = url.split("?").pop();
    return gameViewId.split("=").pop();
 }

 // pass http service to the controller
function loadGameData(gamePlayerId){
      $.get("/api/game_view/" + gamePlayerId, function(response){
                 createBattleFieldView ();
                 showShips(response.ships);
                 showGamePlayerNames (response.gamePlayers, gamePlayerId);
                 });
 }

function showShips(ships) {
    ships.forEach( function(ship){
        ship.locations.forEach(function (location){
            var loc = document.getElementsByClassName(location);
            loc[0].classList.add("ship");
        })
    })
}

function showGamePlayerNames(gamePlayers, gamePlayerId){
   var $player = $('#player-name');
      var $opponent = $('#opponent-name');

      gamePlayers.forEach( function (gamePlayer) {
        if (gamePlayer.id == gamePlayerId){
           $player.text(gamePlayer.player.firstName)
        } else {$opponent.text(gamePlayer.player.firstName )};
      });

}
