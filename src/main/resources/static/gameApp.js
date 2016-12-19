//UI controller

//get url game number parameter
function getGameViewId(){
    var url = window.location.href;
    var gameViewId = url.split("?").pop();
    return gameViewId.split("=").pop();
 }

 // pass http service to the controller
function loadGameData(gameViewId){
 $.get("/api/game_view/" + gameViewId, function(response){
    createBattleField (10,10);
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






function createBattleField (rows,cols){

  var table = document.createElement("table"); //create table element
  var firstRow = document.createElement("tr"); //create empty first row
  var th;
  var td;
  var tr;
  var headers = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]

      //fill the first row with a header for every column starting at 0 and ending at "cols"
  for (i=0; i<=cols;i++){
    th =document.createElement("th");
    th.textContent=i;
    firstRow.appendChild(th);
  }

  table.appendChild(firstRow); //append first row to table

  //create other rows and fill them with table data
  for(i=1;i<=rows;i++){
    tr = document.createElement("tr"); //create new row
    th = document.createElement("th");  //create a header for every row
    th.textContent=headers[i-1];                      //add number to header
    tr.appendChild(th);                    //append header to row

    //add the table data
    for(j=1;j<=cols;j++){
      td = document.createElement("td");
      td.classList.add(headers[i-1]+j);
      tr.appendChild(td);
      }

    table.appendChild(tr);    //append row to table
    }
  document.getElementById("battle-field").appendChild(table); //append table to body
}


loadGameData(getGameViewId());