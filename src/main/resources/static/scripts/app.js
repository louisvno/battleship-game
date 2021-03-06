

$(function() {

  // display text in the output area
  function showOutput(text) {
    $("#output").text(text);
  }

  //Load and display JSON sent by server for /players
  function loadData() {
    $.get("/players")
    .done(function(data) {
      showOutput(JSON.stringify(data, null, 2));
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  // handler for when user clicks add person
     function addPlayer() {
       var firstName = $("#firstName").val();
       var lastName = $("#lastName").val();
       if (firstName && lastName) {
         postPlayer(firstName,lastName);
       }
     }

  // Code to post a new player using AJAX to the backend. On the back end
  // the Java application has a Person class to hold that data
  // on success, reload and data from server
  function postPlayer(firstName, lastName) {
    $.post({
      headers: {
          'Content-Type': 'application/json'
      },
      dataType: "text",
      url: "/players",
      data: JSON.stringify({
        "firstName": firstName,
        "lastName": lastName
       })
    })
    .done(function( ) {
      showOutput( "Saved -- reloading");
      loadData();
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  $("#add_player").on("click", addPlayer);

  loadData();
});

