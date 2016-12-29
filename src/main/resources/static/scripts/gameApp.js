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
         //get data from JSON so that might the data structure change only has to be changed here
         var gamePlayers = response.gamePlayers,
         salvoes = response.salvoes,
         player = gamePlayers[gamePlayerId],
         playerSalvoes = salvoes[gamePlayerId],
         playerFleet = response.fleet,
         enemyId = getEnemyId(gamePlayers,gamePlayerId),
         enemy = gamePlayers[enemyId],
         enemySalvoes = response.salvoes[enemyId];

         renderGameView (playerFleet, player, enemy,enemySalvoes, playerFleet)
      });
 }

function renderGameView (playerFleet, player, enemy,enemySalvoes, playerFleet){
     createBattleFieldView ();
     showPlayerFleet(playerFleet);
     showGamePlayerNames (player, enemy);
     getHitsReceived(enemySalvoes, playerFleet);
}

function showPlayerFleet(fleet) {
    fleet.forEach( function(ship){
        ship.locations.forEach(function (location){
            var loc = document.getElementsByClassName(location);
            loc[0].classList.add("ship");
        })
    })
}

function showGamePlayerNames(player, enemy){
      $('#player-name').text(player.player.firstName);
      $('#enemy-name').text(enemy.player.firstName);
}

//TODO compare enemy salvo targets with player ship locations
function getHitsReceived(enemySalvoes, playerFleet){
    //turn:["loc1","loc2"]
    //shipid : { locations : ["loc1","loc2"], type: "shiptype}
    //shipid : { locations : ["loc1","loc2"], type: "shiptype}
    //hits : { turn:["loc1","loc2"]}
    Object(playerFleet).

    return playerFleet;
}

function getEnemyId(gamePlayers, gamePlayerId) {

  return Object.keys(gamePlayers).filter(key => {
        return key != gamePlayerId});
}

