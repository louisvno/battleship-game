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
         } else {
         //hide
              $(".join-button").hide();
              $("#player-info").hide();
         //show
              $("#new-game").show();
         }
}

function loadGames(){
    $.ajax({ method:"GET",
             url: "/api/games",
             dataType: "json",
             success: function (data) {
                    renderGamesList(data.games);
                    viewController(data.currentPlayer)
             },
             error: function (){
               $('#games-list').html('<span>Games list unavailable</span>');
             }
    });
}

function renderGamesList(games){
    $('#games-list').html(addGames(games));
}

function addGames(games){
   return games.reduce(function(str,game){
      return  str +
              "<div class='game-card'>" +
                   "<ul>"+
                      "<li>"+ game.id + "</li>" +
                       addPlayers(game) +
                      "<li>created:"+ new Date(game.created) + "</li>" +
                   "</ul>" +
                   addJoinButton(game) +
              "<div>";
    },"");
}

function addJoinButton(game){
    var ids = Object.keys(game.gamePlayers);
    if (ids.length < 2 ){
        return "<button class='join-button'>Join</button>";
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


$(document).on('submit', '#login-form', function(e) {
   e.preventDefault();
    var username = this["0"].value;
    var password = this["1"].value;
    login (username,password);
});

function login (username, password){
    $.ajax({ method:"POST",
           url: "/api/login",
                  data: {username: username,  password: password },
                  dataType: "json",
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
                       password: password
                      // firstname: values.firstname,
                      // lastname: values.lastname
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

