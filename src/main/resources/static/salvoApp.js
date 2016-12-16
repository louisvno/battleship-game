
 //send an Ajax request to /games
 //pass the data that is eventually returned to a function that creates LI elements for each game date
 //and stores the list in the OL element on the web page. See the Resources for how to create a list from a list using map.

 // pass http service to the controller
function viewController($http, $scope) {
  $scope.games;

  $http.get("/api/games")
     .then(function(response) {
        tempGames = response.data;
        tempGames.forEach (function(game){
            game.created = getDate(game.created);
        })
        $scope.games = tempGames;
     });
}

angular
    .module('salvoGame', [])
    .controller('viewController', viewController);

//auxiliary functions
function getDate(dateString){
    date = new Date (dateString);
    return date.toTimeString();
}
