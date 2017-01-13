/**********************************
*   Ship placement UI functions   *
**********************************/
var shipsAvailable = [{
                     shipType : "Aircraft-carrier",
                     shipLocations : [],
                     shipLength : 5
                     },{
                     shipType : "Destroyer",
                     shipLocations : [],
                     shipLength : 4
                     },{
                     shipType : "Destroyer",
                     shipLocations : [],
                     shipLength : 4
                     },{
                     shipType : "Submarine",
                     shipLocations : [],
                     shipLength : 3
                     },{
                     shipType : "Submarine",
                     shipLocations : [],
                     shipLength : 3
                     },{
                     shipType : "Submarine",
                     shipLocations : [],
                     shipLength : 3
                     },{
                     shipType : "Patrolboat",
                     shipLocations : [],
                     shipLength : 2
                     },{
                     shipType : "Patrolboat",
                     shipLocations : [],
                     shipLength : 2
                     },{
                     shipType : "Patrolboat",
                     shipLocations : [],
                     shipLength : 2
                     },{
                     shipType : "Patrolboat",
                     shipLocations : [],
                     shipLength : 2
                     }];
var playerShips = [];
var tempLocations=[];


function clearTempLocations () {
   tempLocations.forEach(function(loc){
             $('#fleet-display td[data-coordinate=' + loc + "]").removeClass("temp-ship");
          })
   tempLocations=[];
}


function setShipPlacementEvents(){

    $('#fleet-display').on('mouseover',"td", function (e){
       if (tempLocations.length !== 0){
       clearTempLocations();
       }
       var firstCoordinate = e.target.getAttribute("data-coordinate");

       if (shipsAvailable.length !== 0) {
           if (placeHorizontal()){
                tempLocations = getShipLocationsHorizontal(firstCoordinate,shipsAvailable[0].shipLength);
           } else {
                tempLocations = getShipLocationsVertical(firstCoordinate,shipsAvailable[0].shipLength);
           }

           tempLocations.forEach(function(loc){
              $('#fleet-display td[data-coordinate=' + loc + "]").addClass("temp-ship");
           })
       }
    });

    $('#fleet-display').on('click',"td", function (e){
        if(isValidPlacement() && shipsAvailable.length !== 0){
            shipsAvailable[0].shipLocations = tempLocations;
            tempLocations.forEach(function(loc){
                $('#fleet-display td[data-coordinate=' + loc + "]").addClass("ship");
            })
            playerShips.push(shipsAvailable.shift());
          //  console.log(playerShips);
            clearTempLocations();
        }
    });
};

function isValidPlacement(){
    var checks=[];
    tempLocations.forEach(function(loc){
        checks.push($('#fleet-display td[data-coordinate=' + loc + "]").hasClass("ship"));
    });
    return checks.every(function (check){return check === false});
}

function placeHorizontal(){
    return ($('input[type=radio]:checked').val()) === "horizontal";
}

function getShipLocationsHorizontal(firstCoordinate,shipLength){
    var x = Number(firstCoordinate.split("").pop());
    var y = firstCoordinate.split("").shift();
    var shipLocations =[];

    for (var i=0; i < shipLength; i++){
        shipLocations.push(y + (x + i));
    }
    return shipLocations;
}

function getShipLocationsVertical(firstCoordinate,shipLength){
    var yCoordinates = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    var x = Number(firstCoordinate.split("").pop());
    var y = firstCoordinate.split("").shift();
    var shipLocations =[];
    var yIndex = yCoordinates.indexOf(y);

    for (var i=0; i < shipLength; i++){
        shipLocations.push((yCoordinates[yIndex +i]) + x);
    }
    return shipLocations;
}

/* end ship placement UI functions */

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

function setShip (){
    playerShips.push();
    shipsAvailable.shift();
}

function createShips (gamePlayerId) {
    $.ajax({ method:"POST",
             url: "/api/games/players/" + gamePlayerId + "/ships",
             contentType:"application/json",
             data: JSON.stringify ([{
                            "shipType" : "destroyer",
                            "shipLocations" : ["A1","A2"]
                            },
                            {
                            "shipType" : "boat",
                            "shipLocations" : ["B1","B2"]
                            }]),
             success: function(){location.reload()}
    });
}

function loadGameData(gamePlayerId){
      $.ajax({ url:"/api/game_view/" + gamePlayerId,
               method:"GET",
               success: function(response){
             //map data from JSON so that might the data structure change only has to be changed here
                     var gamePlayers = response.gamePlayers,

                     salvoes = response.salvoes,
                     player = gamePlayers[gamePlayerId],
                     playerSalvoes = salvoes[gamePlayerId],
                     playerFleet = response.fleet,
                     enemyId = getEnemyId(gamePlayers,gamePlayerId),
                     enemy = gamePlayers[enemyId],
                     enemySalvoes = response.salvoes[enemyId];

                     renderGameView (playerFleet, player,playerSalvoes, enemy,enemySalvoes, playerFleet)
              },
              statusCode: {403: function(){window.location = "/games.html";}
              }
          });
 }

function renderGameView (playerFleet, player,playerSalvoes, enemy,enemySalvoes, playerFleet){
     createGameDisplay ("fleet-display");
     createGameDisplay ("battlefield-display");
     renderShipList(shipsAvailable);
     showPlayerFleet(playerFleet);
     showGamePlayerNames (player, enemy);
     showHitsReceived(enemySalvoes);
     showHits(playerSalvoes);
     showMissed(playerSalvoes);
     setShipPlacementEvents();
}

function renderShipList(shipsAvailable) {
    str="";
   shipsAvailable.forEach(function(ship){
        str += "<li>" + ship.shipType + "</li>";
    });

    $("#ship-list").html(str);
}

function showPlayerFleet(fleet) {
    fleet.forEach( function(ship){
        ship.locations.forEach(function (location){
            var $loc = $('#fleet-display td[data-coordinate=' + location + "]");
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
