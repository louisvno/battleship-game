//UI controller
loadGameData(getParameterByName("gp"));

//get url game number parameter (from stack overflow)
function getParameterByName(name, url) {
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
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
