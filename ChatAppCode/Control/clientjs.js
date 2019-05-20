var LOGGEDIN_TXT;
var JSON_ERR = "INVALID JSON";
var TAKEN_ERR = 1;
var USER_PASS_ERR = 2;

function userSetup(){
    LOGGEDIN_TXT = $("#logged").text();
    
    hideEmail();
    hideErrors();
    hideSuccess();
}

/*************************
 * HELPER FUNCTIONS
 *************************/
function getUser(){
    return document.getElementById("usertxt").value;
}
function getPass(){
    return document.getElementById("passtxt").value;
}
function getEmail(){
    return document.getElementById("emailtxt").value;
}

function hideErrors(){
    $('.err').css("display", "none");
}

function hideSuccess(){
    $('.success').css("display", "none");
}

function hideEmail(){
    $("#emailtxt").val("");
    $("#emailtxt").css("display", "none");
    $("#emaillabel").css("display", "none");
}

function emptyTextboxes(){
    $("#usertxt").val("");
    $("#passtxt").val("");
}

/*****************************************
 * LOGIN
 * Takes username and password
 * Sends ajax request, calls processLogin
 *****************************************/

function login(){
    hideErrors();
    hideEmail();
    hideSuccess();
    
    var user = getUser();
    var pass = getPass();
    // Error checking --> make sure pass and username are entered
    if(user == "" || pass == ""){
        // Display to user
        $('#usrPassNDErr').css("display", "block");
        return;
    }
    
    // Setup object for request
    var data = {};
    data.username = user;
    data.password = pass;
    data.action = "login";
    data.database = "chattest";
    
    // Send request
    $.post("Control/user.php", data, processLogin);
}

/*****************************************
 * LOGOFF
 * Sends ajax request, calls processLogoff
 *****************************************/
function logoff(){
    hideErrors();
    hideEmail();
    hideSuccess();
    
    // Setup object for request
    var data = {};
    data.action = "logoff";
    data.database = "chattest";
    // send request
    $.post("Control/user.php", data, processLogoff);
}

/*****************************************
 * DEACTIVATE
 * Takes username and password
 * Sends ajax request, calls processDeact
 *****************************************/
function deactivate(){
    hideErrors();
    hideEmail();
    hideSuccess();
    
    var user = getUser();
    var pass = getPass();
    
    // Verify username and password provided
    if(user == "" || pass == ""){
        // Notify user
         $('#usrPassNDErr').css("display", "block");
        return;
    }
    
    // Setup object for request
    var data = {};
    data.username = user;
    data.password = pass;
    data.action = "deactivate_user";
    data.database = "chattest";
    
    logoff();
    // send request, notify user that request was sent
    $.post("Control/user.php", data, processDeact);
}

/*****************************************
 * CREATE A NEW USER
 * Takes username, password and email
 * Sends ajax request, calls processCreate
 *****************************************/
function createNew(){
    hideErrors();
    hideSuccess();
    
    // Display email box
    var textbox = document.getElementById("emailtxt");
    var label = document.getElementById("emaillabel");
    if(textbox.style.display == "none"){
        textbox.style.display = "block";
        label.style.display = "block";
        $('#enterEmail').css("display", "block");
        return;
    }
    
    // Verify that email, password and username are filled out
    var user = getUser();
    var pass = getPass();
    var email = getEmail();
    if(email == "" || user == "" || pass == ""){
        // Alert user
        $('#usrPassEmailNDErr').css("display", "block");
        return;
    }
    
    // Setup object for request
    var data = {};
    data.username = user;
    data.password = pass;
    data.email = email;
    data.action = "create_user";
    data.database = "chattest";
    $.post("Control/user.php", data, processCreate);
}

/*****************************************
 * PROCESS LOGIN
 * handles json for login action
 *****************************************/
function processLogin($data, $status) {
    hideSuccess();
    hideErrors();
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert($data);
        return;
    }
    var errNum = results.error_number;
    // The login fails if any error is returned
    if(errNum != 0){
        if(errNum == USER_PASS_ERR)
            $('#usrPassErr').css("display", "block");
        else
            alert(results.error_string);
        return;
    }
    
    // display the correct info if the login was successful
    $('.notLogged').css("display", "none");
    $('.logged_in_as').html(LOGGEDIN_TXT + results.username);
    
    
    
    window.location.reload();
}

/*****************************************
 * PROCESS LOGOFF
 * handles json for logoff action
 *****************************************/
 function processLogoff($data, $status){
    hideSuccess();
    hideErrors();
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert(JSON_ERR);
        return;
    }
    
    if(results.error_number != 0){
        alert(results.error_string);
    }
    
    $('.logged_in_as').css("display", "none");
    $('.notLogged').css("display", "block");
    
    emptyTextboxes();
    window.location.reload();
 }
 
 /*****************************************
 * PROCESS DEACTIVATE USER
 * handles json for deactivate action
 *****************************************/
 function processDeact($data, $status){
    hideSuccess();
    hideErrors();
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert(JSON_ERR);
        return;
    }
    
    var errNum = results.error_number;
    // The login fails if any error is returned
    if(errNum != 0){
        if(errNum == USER_PASS_ERR)
            $('#usrPassErr').css("display", "block");
        else
            alert(results.error_string);
        return;
    }
    
    // Show success
    $('#deactivated').css("display", "block");
    
    emptyTextboxes();
 }
 
 /*****************************************
 * PROCESS CREATE NEW USER
 * handles json for create user action
 *****************************************/
 function processCreate($data, $status){
    hideSuccess();
    hideErrors();
    try{
        var results = JSON.parse($data);
    }
     catch(err){
        alert(JSON_ERR);
        return;
    }
     
    var errNum = results.error_number;
    if(errNum != 0){
        if(errNum == TAKEN_ERR)
           $("#1").css("display", "block");
        else
            alert(results.error_string);
        return;
    }
        
    // Show success
    $('#newUser').css("display", "block");
    
    hideEmail();
    emptyTextboxes();
 }
 