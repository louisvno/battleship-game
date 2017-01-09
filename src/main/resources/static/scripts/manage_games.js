
$(function(){
    loadGames()
  });

function loadGames(){
    $.ajax({ method:"GET",
             url: "/api/games",
             dataType: "json",
             success: function (data) {
                    renderGamesList(data.games);
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
        return "<button>Join</button>";
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