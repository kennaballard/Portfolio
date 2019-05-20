<?php 
# Kennedy Ballard
# 169618

    $dir = __DIR__;
    // Timeout
    define(ONE_HOUR, (60*60)); // 60 minutes * 60 seconds
    $MAX_IDLE_TIME = ONE_HOUR;
    // change timeout for testing purposes
    if ($_SERVER['HTTP_USER_AGENT'] === "Testing") {
        $MAX_IDLE_TIME = 60;
    }
    session_start();
    require_once("$dir/../Model/DB_user.php");
    require_once("$dir/../Control/errors.php");
    
    error_reporting(E_ALL);
    ini_set('display_errors','On');
    # ========================================================================
    # user.php
    # ========================================================================
    # PURPOSE:
    # Verifies that all information that is required for user interaction is
    # present, performs the 'action', and returns info to the client via
    # a JSON string.
    #
    # SESSION DATA
    # if user logs in, then set session 'username' to logged in user
    # if user logs out, then set session 'username' to ''
    # if user deactivates themselves, then set username to ''
    # if anything happens with a user, set "last_activity" to time()
    #
    # INPUTS: by POST (they may not be set - you need to check)
    # action: "login" or "create_user" or "deactivate_user" or "logoff"
    # username: "username"
    # password: "password"
    # email: "email"
    # database: (optional - defaults to 'chat')
    #
    # VALID ACTIONS:
    # login
    # errors: username is null or empty string,
    # password is null or empty string,
    # username/password is invalid
    # session: sets 'username' and 'last_activity'
    #
    # create_user
    # errors: user with that name already exists,
    # username is null or empty string,
    # password is null or empty string
    # email is null
    # session: sets 'username' and 'last_activity'
    #
    # deactivate_user - can only deactivate your own account
    # errors: username is null or empty string
    # password is null or empty string
    # username/password is invalid (can only delete yourself)
    # session: sets 'username' to ""
    #
    # logoff
    # session: sets 'username' to ""
    #
    # PRINTS (JSON string):
    # username: username (if user logged off, set to "", else username)
    # error_string: error string (set to empty string if no error)
    # error_number: integer (integer uniquely defining the error)
    #
    # Error Numbers & Messages:
    # (error number 0 and error message '' indicates no errors)
    #
    # 1 Username is already taken
    # 2 Invalid username/password
    # 99 Experiencing technical difficulties
    # 3 Email address is not defined
    # 4 Username is not defined
    # 5 Password is not defined
    # 6 Could not deactivate user
    # 98 Requested action is not available
    #
    # Exception Handling
    # * if there is an exception, this will return a JSON string
    # error_string: "We are experiencing technical difficulties"
    # error_number: 99
    # * Exception message & other info will be written to the log file
    # ========================================================================
    
    $info = $_POST;
    
    # JSON string prep
    $jsonStr = array(
        "username" => "",
        "error_string" => "",
        "error_number" => 0 );
    
    # Check if action is set
    if(!array_key_exists("action", $info))
        # Exit
        errors($ACTION_CODE, $ACTION_MSG, $jsonStr);
        
    # Complete action or set error if invalid action choice 
    switch ($info["action"]){
        case "login":
            login($info, $jsonStr);
            break;
        case "logoff":
            logoff($info, $jsonStr);
            break;
        case "logged_in_as":
            logged_in_as($jsonStr, $MAX_IDLE_TIME);
            break;
        case "create_user":
            createUser($info, $jsonStr);
            break;
        case "deactivate_user":
            deactivateUser($info, $jsonStr);
            break;
        default:
            errors($ACTION_CODE, $ACTION_MSG, $jsonStr);
    }
  
#------------------------------------------
# LOGIN
# --> Login a valid user
# --> set error if
#           - Username or password not sent
#           - Username/password not a match
#           - User not active
#           - database error
# --> Change session username and last active
#------------------------------------------
function login($params, $jsn){
    // GLOBALS 
    global $USR_ND_CODE, $USR_ND_MSG, $PASS_ND_CODE, $PASS_ND_MSG, $USR_PASS_CODE, $USR_PASS_MSG, $TECH_CODE, $TECH_MSG;
    
    # Verify that username and password are set
    if(!array_key_exists("username", $params) || $params["username"] == "")
        errors($USR_ND_CODE, $USR_PASS_MSG, $jsn);
    if(!array_key_exists("password", $params) || $params["password"] == "")
        errors($PASS_ND_CODE, $PASS_ND_MSG, $jsn);
    
    # To catch database exceptions
    try{
         $dbh = array_key_exists("database", $params) ? connect($params["database"]) : connect();
        
        # Check that username and password match
        if(!validate_user($dbh, htmlentities($params["username"]), $params["password"]))
            errors($USR_PASS_CODE, $USR_PASS_MSG, $jsn);
        else{
            # User is good, set session info
            $_SESSION['username'] = htmlentities($params["username"]);
            $_SESSION['last_activity'] = time();
            
            # Set username & error codes
            errors(0, "", $jsn, htmlentities($params["username"]));
        }
    }
    catch(Exception $e){
        errors($TECH_CODE, $TECH_MSG, $jsn);
    }
    
}

#------------------------------------------
# LOGOFF
# Change session username
#------------------------------------------
function logoff($params, $jsn){
    $_SESSION['username'] = "";
    $_SESSION['last_message_seen'] = 0;
    errors(0, "", $jsn);
}

#------------------------------------------
# LOGGED IN AS
# get session username --> who is logged in
#------------------------------------------
function logged_in_as($jsn, $MAX_IDLE_TIME){
    // GLOBALS 
    global $TIMEOUT_CODE, $TIMEOUT_MSG;
    
    // session name is set, and not timed out
    if (! empty ($_SESSION['username']) 
    && ( time() - $_SESSION['last_activity'] ) < $MAX_IDLE_TIME) {
        // … code for logged in user …
        errors(0, "", $jsn, htmlentities($_SESSION['username']));
    } else {
        // … code for not logged in user …
        errors($TIMEOUT_CODE, $TIMEOUT_MSG, $jsn);
    }
}

#------------------------------------------
# CREATE USER
# --> set error if
#           - Email, username or password not sent
#           - User exists
#           - Database error
# --> Change session username and last active
#------------------------------------------
function createUser($params, $jsn){
    // GLOBALS
     global $USR_ND_CODE, $USR_ND_MSG, $PASS_ND_CODE, $PASS_ND_MSG, $USR_TAKEN_CODE, $USR_TAKEN_MSG, $TECH_CODE, $TECH_MSG, $EM_ND_CODE, $EM_ND_MSG;
     
    # Verify that email, username and password are set
    if(!array_key_exists("username", $params) || $params["username"] == "")
        errors($USR_ND_CODE, "$USR_ND_MSG", $jsn);
    if(!array_key_exists("password", $params) || $params["password"] == "")
        errors($PASS_ND_CODE, "$PASS_ND_MSG", $jsn);
    if(!array_key_exists("email", $params))
        errors($EM_ND_CODE, "$EM_ND_MSG", $jsn);
        
    # To catch database exceptions
    try{
         $dbh = array_key_exists("database", $params) ? connect($params["database"]) : connect();
         
         if(!create_user($dbh, htmlentities($params["username"]), htmlentities($params["password"]), htmlentities($params["email"])))
             errors($USR_TAKEN_CODE, $USR_TAKEN_MSG, $jsn);
         else{
            # User is good, set session info
            $_SESSION['username'] = $params["username"];
            $_SESSION['last_activity'] = time();
            
            # Set username & error codes
            errors(0, "", $jsn, $params["username"]);
         }
    }
    catch(Exception $e){
         errors($TECH_CODE, $e->getMessage(), $jsn);
    }
}

#------------------------------------------
# DEACTIVATE USER
# --> Deactivate a user
# --> set error if
#           - username or password not sent
#           - username/password invalid
#           - database error
#------------------------------------------
function deactivateUser($params, $jsn){
    // GLOBALS
    global $USR_ND_CODE, $USR_ND_MSG, $PASS_ND_CODE, $PASS_ND_MSG, $USR_PASS_CODE, $USR_PASS_MSG, $TECH_CODE, $TECH_MSG;
    
    # Verify that username and password are set
    if(!array_key_exists("username", $params) || $params["username"] == "")
        errors($USR_ND_CODE, "$USR_ND_MSG", $jsn);
    if(!array_key_exists("password", $params) || $params["password"] == "")
        errors($PASS_ND_CODE, "$PASS_ND_MSG", $jsn);
    
    # To catch database exceptions
    try{
        $dbh = array_key_exists("database", $params) ? connect($params["database"]) : connect();
        
        # Check that username and password match
        if(!check_pass($dbh, $params["username"], $params["password"]))
            errors($USR_PASS_CODE, $USR_PASS_MSG, $jsn);    
        else{
            # Deactivate the user
            deactivate_user($dbh, $params["username"]);
            
            # Set session info
            $_SESSION['username'] = "";
            # Set username & error codes
            errors(0, "", $jsn);
        }
    }
    catch(Exception $e){
        errors($TECH_CODE, $TECH_MSG, $jsn);
    }
}

#-----------------------------------------------------
# Set the values for the JSON string to be returned
#-----------------------------------------------------
function errors($code, $message, $jsn, $name = ""){
    $jsn["username"] = $name;
    $jsn["error_number"] = $code;
    $jsn["error_string"] = $message;
    exitProgram($jsn);
}   

#------------------------------------------
# Setup JSON
# Return value
#------------------------------------------
function exitProgram($jsn){
    # encode json, print
    $json=json_encode($jsn);
    
    echo $json;
    exit;
}
     
?>