/**********************************
*   Globals                       *
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
var salvoTargets=[];

/**********************************
*   Ship placement functions   *
**********************************/

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

$(function (){
   loadGameData(getParameterByName("gp"));
})

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

function viewController(gameData, gamePlayerId){
    //destructure data
    var gamePlayers = gameData.gamePlayers,
        salvoes = gameData.salvoes || {},
        player = gamePlayers[gamePlayerId],
        playerSalvoes = salvoes[gamePlayerId],
        playerFleet = gameData.fleet,
        enemyId = getEnemyId(gamePlayers,gamePlayerId) || {},
        enemy = gamePlayers[enemyId] || {},
        enemySalvoes = gameData.salvoes[enemyId] || {},
        enemyFleet = gameData.enemyFleet || {},
        gameState = gameData.gameState;

    renderMainView (player, enemy);
    //Render different states
    //If player has no ships go into "ship placement mode"
    if (playerFleet.length === 0 ){

        $("#ship-list").html(renderShipList(shipsAvailable));
        $('#battlefield-display').addClass("off");
        $("#fire-salvo").addClass("disabled");
        $("#game-notifications h2").text("Deploy your fleet and press Ready!");
        $("#ship-list").append('<form id ="switch-orientation" action="">' +
                                                   '<h2>Switch ship orientation</h2>'+
                                                   '<label><input type="radio" name="orientation" value="horizontal" checked="checked"> Horizontal</label>'+
                                                   '<label><input type="radio" name="orientation" value="vertical"> Vertical</label>' +
                                               '</form>');
        setShipPlacementEvents();
        $("#right-side-display .orange-display").html('<span>Scanning for enemy ships <span class="dot"></span></span>');
        setWaitingDashAnimation();
        setWaitingDotsAnimation();

    //If player has ships go into "gameplay mode"
    } else if (playerFleet.length > 0){
        $("#submit-ships").hide();
        $("#undo-placement").hide();
        showPlayerFleet(playerFleet);
        showHitsReceived(enemySalvoes);
        showHits(playerSalvoes);
        showMissed(playerSalvoes);
        $("#ship-list").html(renderShipStatusList(playerFleet));

        switch (gameState){
                 case 0:
                     //game ready to shoot salvoes
                     $("#game-notifications h2").text("Ready to fire salvo!");
                     $("#fire-salvo").addClass("active");
                     $("#enemy-ship-list").html(renderShipStatusList(enemyFleet))
                     setGamePlayEvents(gamePlayerId);
                     break;
                 case 1:
                     //waiting for other player
                     checkForUpdates();
                     if(Object.keys(gamePlayers).length > 1 && !isEmpty(enemyFleet)){
                        $("#enemy-ship-list").html(renderShipStatusList(enemyFleet))
                        $("#game-notifications h2").text("Awaiting opponent turn");
                     }else {
                        $("#game-notifications h2").text("Waiting for other player");
                        $("#right-side-display .orange-display").html('<span>Scanning for enemy ships <span class="dot"></span></span>')
                        setWaitingDotsAnimation();
                     }
                     break;
                 case 2:
                     //game finished
                     $("#game-notifications h2").text("Game Finished");
                     break;
        }
    }
}

function setGamePlayEvents (gamePlayerId){
    $('#battlefield-display').on('click',"td", function (e){
            var coordinate = e.target.getAttribute("data-coordinate");
            var index = salvoTargets.indexOf(coordinate);

            if (salvoTargets.length < 5 && index < 0 && e.target.classList.length ===0) {
                salvoTargets.push(coordinate);
                $(e.target).addClass("targeted");
            } else if (salvoTargets.length <= 5 && index >= 0)  {
                salvoTargets.splice(index,1);
                $(e.target).removeClass("targeted");
            }
    });

    $('#fire-salvo').click( function (){
        $.ajax({ method:"POST",
                 url: "api/games/players/" + gamePlayerId + "/salvoes",
                 contentType:"application/json",
                 data: JSON.stringify ({"targets" : salvoTargets}),
                 success: function(){location.reload()}
        });
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

function checkForUpdates(){
    gamePlayerId = getParameterByName("gp");
    setInterval( function (){
        $.ajax({ url:"/api/game_view/" + gamePlayerId,
                method:"GET",
                success: function(response){
                    if(response.gameState !== 1){
                        window.location.reload();
                    }
                }
        });
   },1000);
}

function renderMainView (player, enemy){
     createGameDisplay ("fleet-display");
     createGameDisplay ("battlefield-display");
     showGamePlayerNames (player, enemy);
}

function renderShipList(shipsAvailable) {
    str="";
    shipsAvailable.forEach(function(ship){
        str += "<li>" + ship.shipType + "</li>";
    });
    return str;
}

function renderShipStatusList(playerFleet) {
    str="";
    Object.keys(playerFleet).forEach(function(key){
         var ship = playerFleet[key];
         var status;
         if (ship.isSunk == false){
             status = "ok";
         } else {
             status ="sunk";
         }
         str += "<li class=" + status + ">" + ship.type + ": " + status + "</li>";
     });
    return str;
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
      if(!isEmpty(enemy)){
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
    if(!isEmpty(enemySalvoes)){
        Object.keys(enemySalvoes).forEach(turn => {
            enemySalvoes[turn].hits.forEach(hit => {
                $('#fleet-display td[data-coordinate=' + hit).addClass("hit").text(turn);
            });
        });
    }
};

//playerSalvoes; keys are turns
function showHits(playerSalvoes){
    if (!isEmpty(playerSalvoes).length > 0){
        Object.keys(playerSalvoes).forEach(turn => {
            playerSalvoes[turn].hits.forEach(hit => {
                $('#battlefield-display td[data-coordinate=' + hit).addClass("ship hit").text(turn);
            });
        });
    }
};

//playerSalvoes; keys are turns
function showMissed(playerSalvoes){
    if (!isEmpty(playerSalvoes)){
        Object.keys(playerSalvoes).forEach(turn => {
            playerSalvoes[turn].missed.forEach(miss => {
                $('#battlefield-display td[data-coordinate=' + miss).addClass("missed").text(turn);
            });
        });
    }
};

function setWaitingDotsAnimation (){
    setInterval(function(){
                cell = $(".dot");
                if (cell.text().length > 3){
                            cell.text('');
                        }else{ cell.append('.')}
            }, 500);
}

function setWaitingDashAnimation(){
    setInterval(function(){
                    cell = $("#battlefield-display tr:nth-of-type(2)  td:first-of-type");
                    cell.css("color","#009900")
                    if (cell.text() == "_"){
                        cell.text('');
                    }else{
                        cell.text('_');
                    }
                }, 500);
 }

//Utility functions

//Check if an object is empty or not
function isEmpty(obj){
    if (Object.keys(obj).length > 0) return false;
    else return true;
}



