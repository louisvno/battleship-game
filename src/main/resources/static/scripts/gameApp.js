/**********************************
*   Ship placement functions   *
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
var placedShips = [];
var tempLocations=[];


function clearTempLocations () {
   tempLocations.forEach(function(loc){
             $('#fleet-display td[data-coordinate=' + loc + "]").removeClass("temp-ship");
          })
   tempLocations=[];
}

function updatePlacedView(){
   var allLocs = placedShips.map(function (s){return s.shipLocations})
                    .reduce(function(a, b) {return a.concat(b)},[])
    console.log(allLocs);
    $('#fleet-display td').removeClass("ship");
    allLocs.forEach(function(loc){
                    $('#fleet-display td[data-coordinate=' + loc + "]").addClass("ship");
                })
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
        if(shipsAvailable.length !== 0 && isValidPlacement()){
            shipsAvailable[0].shipLocations = tempLocations;
            placedShips.push(shipsAvailable.shift());
            clearTempLocations();
            updatePlacedView();
        }
    });

    $('#undo-placement').click(function(){
        if (placedShips.length !== 0){
            shipsAvailable.unshift(placedShips.pop());
            updatePlacedView();
        }

    });

    $('#submit-ships').click( function(){
       if (shipsAvailable.length === 0 && placedShips.length === 10){
            createShips(getParameterByName("gp"));
       };
    });
};

function isValidPlacement(){
    return isNotOverlapping() && isInsideField();
}

function isNotOverlapping (){
    var checks=[];
    tempLocations.forEach(function(loc){
        checks.push($('#fleet-display td[data-coordinate=' + loc + "]").hasClass("ship"));
        });
    return checks.every(function (check){return check === false})
}


function isInsideField() {
    var firstCoordinate = tempLocations[0];
    var regex = /(\d+)/; //find digit once or more
    var x = Number(regex.exec(firstCoordinate)[0]);
    var y = firstCoordinate.split("").shift();
    var fieldSize = 10;

    if(placeHorizontal()){
        var limitCoordinate = fieldSize - (shipsAvailable[0].shipLength-1);
        return  x <= limitCoordinate;
    } else {
        var yCoordinates = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
        var yNum = yCoordinates.indexOf(y)+1;
        console.log(yNum);
        var limitCoordinate = fieldSize - (shipsAvailable[0].shipLength-1);
        return  yNum <= limitCoordinate;
    }
}

function placeHorizontal(){
    return ($('input[type=radio]:checked').val()) === "horizontal";
}

function getShipLocationsHorizontal(firstCoordinate,shipLength){
    var regex = /(\d+)/; //find digit once or more
    var x = Number(regex.exec(firstCoordinate)[0]);
    var y = firstCoordinate.split("").shift();
    var shipLocations =[];

    for (var i=0; i < shipLength; i++){
        shipLocations.push(y + (x + i));
    }
    console.log(shipLocations);
    return shipLocations;
}

function getShipLocationsVertical(firstCoordinate,shipLength){
    var yCoordinates = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    var regex = /(\d+)/; //find digit once or more
    var x = Number(regex.exec(firstCoordinate)[0]);
    var y = firstCoordinate.split("").shift();
    var shipLocations =[];
    var yIndex = yCoordinates.indexOf(y);

    for (var i=0; i < shipLength; i++){
        shipLocations.push((yCoordinates[yIndex +i]) + x);
    }
    return shipLocations;
}

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


/***********************************
*  Main Game Functions
************************************/

//UI controller
$(function (){
   loadGameData(getParameterByName("gp"));
})

function viewController(gameData, gamePlayerId){
    var gamePlayers = gameData.gamePlayers,

    salvoes = gameData.salvoes,
    player = gamePlayers[gamePlayerId],
    playerSalvoes = salvoes[gamePlayerId],
    playerFleet = gameData.fleet,
    enemyId = getEnemyId(gamePlayers,gamePlayerId),
    enemy = gamePlayers[enemyId],
    enemySalvoes = gameData.salvoes[enemyId];

    renderGameView (playerFleet, player,playerSalvoes, enemy,enemySalvoes, playerFleet)

    //if player has no ships go into "ship placement mode"
    if (playerFleet.length === 0 ){
        setShipPlacementEvents();
        //TODO show and hide appropriate view elements
    } else {
        setGamePlayEvents(gamePlayerId);
    }
}

var salvoLocations=[];

function setGamePlayEvents (gamePlayerId){
    $('#battlefield-display').on('click',"td", function (e){
            var coordinate = e.target.getAttribute("data-coordinate");

            if (salvoLocations.length < 5) {salvoLocations.push(coordinate)}
            console.log(salvoLocations);
//            $.ajax({ method:"POST",
//                     url: "/games/players/" + gamePlayerId + "/salvoes",
//                     contentType:"application/json",
//                     data: JSON.stringify (),
//                     success: function(){location.reload()}
//            });
    });
}



function createShips (gamePlayerId) {
        $.ajax({ method:"POST",
                 url: "/api/games/players/" + gamePlayerId + "/ships",
                 contentType:"application/json",
                 data: JSON.stringify (placedShips),
                 success: function(){location.reload()}
        });
}

function loadGameData(gamePlayerId){
      $.ajax({ url:"/api/game_view/" + gamePlayerId,
               method:"GET",
               success: function(response){
                     viewController(response, gamePlayerId);
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
      if(enemy){
        $('#enemy-name').text(enemy.player.firstName);
      }
}

//gamePlayers; keys are id's
function getEnemyId(gamePlayers, gamePlayerId) {
  return Object.keys(gamePlayers).filter(id => {
        return id != gamePlayerId});
}

//enemysalvoes; keys are turns
function showHitsReceived(enemySalvoes){
    if(enemySalvoes){
        Object.keys(enemySalvoes).forEach(turn => {
            enemySalvoes[turn].hits.forEach(hit => {
                $('#fleet-display .' + hit).addClass("hit").text(turn);
            });
        });
    }
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


