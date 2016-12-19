//UI controller
loadGameData(getGameViewId());

//get url game number parameter
function getGameViewId(){
    var url = window.location.href;
    var gameViewId = url.split("?").pop();
    return gameViewId.split("=").pop();
 }

 // pass http service to the controller
function loadGameData(gameViewId){
 $.get("/api/game_view/" + gameViewId, function(response){
    createBattleFieldView ();
    showShips(response.ships);
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

