$(function() {
  // Initialize form validation on the registration form.
  // It has the name attribute ""
  $("form[name='signupform']").validate({
    // Specify validation rules
    rules: {
      lastname: "required",
//      email: {
//        required: true,
//        // Specify that email should be validated
//        // by the built-in "email" rule
//        email: true
//      },
      password: {
        required: true,
        minlength: 5
      }
    },
    // Specify validation error messages
    messages: {
      firstname: "Please enter your firstname",
      lastname: "Please enter your lastname",
      password: {
        required: "Please provide a password",
        minlength: "Your password must be at least 5 characters long"
      } //,
//      email: "Please enter a valid email address"
    },
    // Make sure the form is submitted to the destination defined
    // in the "action" attribute of the form when valid
    submitHandler: function(form) {
      form.submit();
      //TODO call login
    }
  });
});

$(document).on('submit', '#login-form', function(e) {
   e.preventDefault();
    var username = this["0"].value;
    var password = this["1"].value;
    $.ajax({ method:"POST",
                url: "/api/login",
                data: {username: username,  password: password },
                dataType: "json",
                statusCode: {
                            200: function() {
                              $.get("/api/games");
                            }
                }
    });
});

$("#logout-button").click( function(e) {
   e.preventDefault();
    $.ajax({
        method:"POST",
        url: "/api/logout",
        dataType: "json"
    });
});

