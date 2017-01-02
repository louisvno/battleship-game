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


         renderGameView (playerFleet, player,playerSalvoes, enemy,enemySalvoes, playerFleet)
      });
 }

function renderGameView (playerFleet, player,playerSalvoes, enemy,enemySalvoes, playerFleet){
     createGameDisplay ("fleet-display");
     createGameDisplay ("battlefield-display");
     showPlayerFleet(playerFleet);
     showGamePlayerNames (player, enemy);
     showHitsReceived(enemySalvoes);
     showHits(playerSalvoes);
     showMissed(playerSalvoes);
}

function showPlayerFleet(fleet) {
    fleet.forEach( function(ship){
        ship.locations.forEach(function (location){
            var $loc = $('#fleet-display .' + location);
            $loc.addClass("ship");
        })
    })
}

function showGamePlayerNames(player, enemy){
      $('#player-name').text(player.player.firstName);
      $('#enemy-name').text(enemy.player.firstName);
}

//gamePlayers; keys are id's
function getEnemyId(gamePlayers, gamePlayerId) {
  return Object.keys(gamePlayers).filter(id => {
        return id != gamePlayerId});
}

//enemysalvoes; keys are turns
function showHitsReceived(enemySalvoes){
    Object.keys(enemySalvoes).forEach(turn => {
        enemySalvoes[turn].hits.forEach(hit => {
            $('#fleet-display .' + hit).addClass("hit").text(turn);
        });
    });
};

//playerSalvoes; keys are turns
function showHits(playerSalvoes){
    Object.keys(playerSalvoes).forEach(turn => {
        playerSalvoes[turn].hits.forEach(hit => {
            $('#battlefield-display .' + hit).addClass("ship hit").text(turn);
        });
    });
};

//playerSalvoes; keys are turns
function showMissed(playerSalvoes){
    Object.keys(playerSalvoes).forEach(turn => {
        playerSalvoes[turn].missed.forEach(miss => {
            $('#battlefield-display .' + miss).addClass("missed").text(turn);
        });
    });
};
