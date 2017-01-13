//TODO hide join button if player is already in game
$(function(){
    loadGames()
});

function viewController(currentPlayer){
    if(currentPlayer){
         //hide
             $("#login").hide();
             $("#signup").hide();
         //show
             $(".join-button").show();
             $("#logout").show();
             $("#new-game").show();

         } else {
         //hide
              $(".join-button").hide();
              $("#player-info").hide();
              $("#new-game").hide();
              $("#new-game").hide();
              $("#logout").hide();
         }
}

function loadGames(){
    $.ajax({ method:"GET",
             url: "/api/games",
             dataType: "json",
             success: function (data) {
                    renderGamesList(data);

                    setUserInfo(data.currentPlayer);
                    viewController(data.currentPlayer)

             },
             error: function (){
               $('#games-list').html('<span>Games list unavailable</span>');
             }
    });
}

function renderGamesList(data){
    $('#games-list').html(addGames(data));
    setButtonEvents();
}

function addGames(data){
   str="";
   data.games.forEach(function(game){
        str +=
              "<div class='game-card'>" +
                   "<ul>"+
                      "<li>"+ game.id + "</li>" +
                       addPlayers(game) +
                      "<li>created:"+ new Date(game.created) + "</li>" +
                   "</ul>" +
                   addJoinButton(game) +
                   addContinueGameButton(game,data.currentPlayer) +
              "</div>";
    });
    return str;
}

function addContinueGameButton(game,currentPlayer){
        var gamePlayerIds = Object.keys(game.gamePlayers);
        var str="";

        if(currentPlayer && gamePlayerIds.length === 2 ){
             gamePlayerIds.forEach(function(id){
                if(game.gamePlayers[id].player["id"] === currentPlayer.id){
                    str = "<a href='game.html?gp=" + id + "'" + " class='join-button'>Continue Game</a>";
             }})
        };
        return str;
 }

function addJoinButton(game){
     var ids = Object.keys(game.gamePlayers);
     if (ids.length < 2 ){
         return "<button data-gameid=" + game.id +" class='join-button'>Join Game</button>";
     } else
     return "";
 }

function addPlayers (game){
    var str = "";
      Object.keys(game.gamePlayers).forEach(key => {
           str +=
           "<li>" +
             game.gamePlayers[key].player.userName +
           "</li>"
      })
    return str;
}

function setUserInfo(currentPlayer){
    if(currentPlayer){
    $("#player-info").html("<span>Hi there</span> " + currentPlayer.firstName)
    };
}

$(document).on('submit', '#login-form', function(e) {
   e.preventDefault();
    var username = $(this).find('input[name="username"]').val();
    var password = $(this).find('input[name="password"]').val();
    login (username,password);
});

function login (username, password){
    $.ajax({ method:"POST",
           url: "/api/login",
                  data: {username: username,  password: password },
                  statusCode: {200: function() {
                                  location.reload();
                                  }
                  }
           });
}

$(document).on('submit', '#signup-form', function(e) {
    e.preventDefault();
    var username = $(this).find('input[name="username"]').val();
    var password = $(this).find('input[name="password"]').val();
    $.ajax({ method:"POST",
                url: "/api/players",
                data: {
                       username: username,
                       password: password,
//                       firstname: values.firstname,
//                       lastname: values.lastname
                       },
                dataType: "json",
                statusCode: {201: function() {
                                console.log("Username:" + username);
                                login (username,password);
                            }
                }
    });
});

$("#logout-button").click( function(e) {
    e.preventDefault();
    $.ajax({
        method:"POST",
        url: "/api/logout",
        dataType: "json",
        statusCode: {200: function() {location.reload();}
        }
    });
});

$("#new-game").click( function(e) {
    e.preventDefault();
    $.ajax({
        method:"POST",
        url: "/api/games",
        dataType: "json",
        statusCode: {201: function(response) {window.location="/game.html?gp="+ response.id;}
        }
    });
});

function setButtonEvents(){
    $("button.join-button").on("click", function(e) {
        e.preventDefault();
        var gameId = e.target.getAttribute("data-gameId");
        $.ajax({
            method:"POST",
            url: "/api/game/" + gameId + "/players",
            dataType: "json",
            statusCode: {201: function(response) {window.location="/game.html?gp="+ response.id;}
            }
        });
    });
}

