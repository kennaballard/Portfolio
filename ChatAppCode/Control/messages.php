<?php
$dir = __DIR__;
// Timeout
define(ONE_HOUR, (60*60)); // 60 minutes * 60 seconds
$MAX_IDLE_TIME = ONE_HOUR;

// change timeout for testing purposes
if ($_SERVER['HTTP_USER_AGENT'] === "Testing") {
    $MAX_IDLE_TIME = 60;
}

session_start();
require_once("$dir/../Model/DB_messages.php");
require_once("$dir/../Control/errors.php");

error_reporting(E_ALL);
ini_set('display_errors','On');

 # ========================================================================
    # messages.php
    # ========================================================================
    # PURPOSE:
    # Verifies that all information that is required for message interaction is
    # present, performs the 'action', and returns info to the client via
    # a JSON string.
    #
    # SESSION DATA
    # if a message is saved, 'last_activity' is set to time()
    # if messages are retrieved, update 'last_message_seen'
    #
    # INPUTS: by POST (they may not be set - you need to check)
    # action: "save_message" or "get_messages" or "" or "get_all_messages"
    # username: "username"
    # timezone offset: "tz_off" (optional - defaults to 0)
    # database: (optional - defaults to 'chat')
    #
    # VALID ACTIONS:
    # save_message
    # errors: username is null or empty string,
    # not logged in,
    # message is too long or empty
    # session: verifies 'username' is the same as that passed in
    #
    # get_messages
    # errors: not logged in
    # session: sets 'last_message_seen'
    #
    # get_all_messages
    # errors: not logged in
    # session: sets 'last_message_seen'
    #
    # PRINTS (JSON string):
    # result: boolean (if save_message) or list of messages (get_messages and get_all_messages), "" if an error
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
    # 6 user not logged in
    # 7 message is empty
    # 8 message is too long
    # 9 error saving message
    #
    # Exception Handling
    # * if there is an exception, this will return a JSON string
    # error_string: "We are experiencing technical difficulties"
    # error_number: 99
    # * Exception message & other info will be written to the log file
    # ========================================================================
    
$info = $_POST;

    
# Check if action is set
if(!array_key_exists("action", $info))
    # Exit
    errors($ACTION_CODE, $ACTION_MSG);
    
# Set the offset --> assume 0 if array key doesn't exist    
if(array_key_exists("tz_off", $info))
    set_time_offset($info["tz_off"]);

    
# Determine the action
switch($info["action"]){
    case "save_message":
        saveMessage($info);
        break;
    case "get_messages":
        getMessages($info);
        break;
    case "get_all_messages":
        getAllMessages($info);
        break;
    case "change_avatar":
        changeAvatar($info);
        break;
    default:
        errors($ACTION_CODE, $ACTION_MSG);
        break;
}

# -------------------------------------------------------
# SAVE MESSAGE
# POST MUST CONTAIN username, content for this action to work
# Validate user is still logged in using session: username and last activity
# save message to model, update last activity session variable
# errors:   user not logged in
#           message text empty or too long
#           exception thrown from model
# returns to client: JSON
# error number (integer)
# error string (string)
# Msg saved (bool)
# -------------------------------------------------------
function saveMessage($data){
    # Error codes and messages
    global $NOT_LOGIN_CODE, $NOT_LOGIN_MSG, $TXT_ND_CODE, $TXT_ND_MSG, $MAX_LEN, $TXT_LONG_CODE, $TXT_LONG_MSG, $TECH_CODE, $TECH_MSG, $USR_ND_CODE, $USR_ND_MSG;
    
    if(!array_key_exists("username", $data) || empty($data['username']))
        errors($USR_ND_CODE, $USR_ND_MSG, false);
    # Verify still logged in 
    if(!isLoggedIn() || $_SESSION['username'] != $data['username'])
        errors($NOT_LOGIN_CODE, $NOT_LOGIN_MSG, false);
        
    # content is within desired length
    if(!array_key_exists("content", $data) || empty($data["content"]))
        errors($TXT_ND_CODE, $TXT_ND_MSG, false);
    if(strlen($data["content"]) > $MAX_LEN)
        errors($TXT_LONG_CODE, $TXT_LONG_MSG, false);
        
    # Connect and save message
    try{
        $dbh = array_key_exists("database", $data) ? connect($data["database"]) : connect();
        save_message($dbh, $data["username"], htmlentities($data["content"]));
        
        # set last activity
        $_SESSION['last_activity'] = time();
        
        # Return to user that message was saved
        errors(0, "", true);
    }
    catch(exception $e){
        errors($TECH_CODE, $TECH_MSG, false);
    }
}

# -------------------------------------------------------
# GET MESSAGES
# validates user is still logged in
# gets all messages from model that the user has not yet seen
# updates session variable last message seen
# converts array of messages to array of html strings using Smarty template messages tpl
# returns to client: msgs (array of messages)
# error number (integer)
# error string (string)
# errors:   user not logged in
#           exception thrown by model
# -------------------------------------------------------
function getMessages($data){
    global $NOT_LOGIN_CODE, $NOT_LOGIN_MSG, $TECH_CODE, $TECH_MSG;
    
    # Verify still logged in 
    if(!isLoggedIn())
        errors($NOT_LOGIN_CODE, $NOT_LOGIN_MSG, false);
        
    # Connect and get messages
    try{
        $dbh = array_key_exists("database", $data) ? connect($data["database"]) : connect();
        $messageArr = (empty($_SESSION['last_message_seen'])) ? get_messages_older_than($dbh) : get_messages_older_than($dbh, $_SESSION['last_message_seen']);
        # Messages are already up to date
        
        if(empty($messageArr))
            errors(0, "", null);
        
        # last read message is now highest id of fetched messages
        $_SESSION['last_message_seen'] = max(array_column($messageArr, 'id'));
        # Return list of messages to the user
        errors(0, "", $messageArr);
    }
    catch(exception $e){
        errors($TECH_CODE, $TECH_MSG);
    }
}

# -------------------------------------------------------
# GET ALL MESSAGES
# validates user is still logged in
# gets all messages from model
# updates session variable last message seen
# converts array of messages to array of html strings using Smarty template messages tpl
# returns to client: msgs (array of messages)
# error number (integer)
# error string (string)
# errors:   user not logged in
#           exception thrown by model
# -------------------------------------------------------
function getAllMessages($data){
    global $NOT_LOGIN_CODE, $NOT_LOGIN_MSG, $TECH_CODE, $TECH_MSG;
    
    # user must be logged in 
    if(!isLoggedIn())
        errors($NOT_LOGIN_CODE, $NOT_LOGIN_MSG, false);
    # Connect and get all messages
    try{
        $dbh = array_key_exists("database", $data) ? connect($data["database"]) : connect();
    
        $messageArr=get_messages_older_than($dbh);
        
        # No messages to return
        if(empty($messageArr))
            errors(0, "", null);
        
        # last read message is now highest id of fetched messages
        $_SESSION['last_message_seen'] = max(array_column($messageArr, 'id'));
        # Return list of messages to the user
        errors(0, "", $messageArr);
    }
    catch(exception $e){
        errors($TECH_CODE, $TECH_MSG);
    }
}

# -------------------------------------------------------
# CHANGE AVATAR
# validates user is still logged in
# updates the user's avatar
# returns to client: JSON
# error number (integer)
# error string (string)
# avatar updated (bool)
# errors:   user not logged in
#           exception thrown by model
# -------------------------------------------------------
function changeAvatar($data){
    # User who's avatar we are updating must be defined
    if(!array_key_exists("username", $data) || empty($data['username']))
        errors($USR_ND_CODE, $USR_ND_MSG, false);
    # Verify still logged in 
    if(!isLoggedIn() || $_SESSION['username'] != $data['username'])
        errors($NOT_LOGIN_CODE, $NOT_LOGIN_MSG, false);
    
    # New avatar must be defined    
    if(!array_key_exists("image", $data) || empty($data["image"]))
        errors($TXT_ND_CODE, $TXT_ND_MSG, false);
        
    # Connect and update avatar
     try{
        $dbh = array_key_exists("database", $data) ? connect($data["database"]) : connect();
        
        change_user_avatar($dbh, $data["username"], htmlentities($data["image"]));
        # set last activity
        $_SESSION['last_activity'] = time();
        
        # Return to user that avatar was updated
        errors(0, "", true);
     }
     catch(exception $e){
        errors($TECH_CODE, $TECH_MSG);
    }
}

# -------------------------------------------------------
# Sets up the JSON string
# Returns to the user
# Inputs:   Error code (0 if no error)
#           Error message ("" if no error)
#           Return value ("" if there was error)
# -------------------------------------------------------
function errors($errorCode, $errorMsg, $returnVal = ""){
    $jsonStr = array(
        "error_number" => $errorCode,
        "error_string" => $errorMsg,
        "result" => $returnVal);
        
    # encode json, print
    $json=json_encode($jsonStr);
    
    echo $json;
    exit;
}

# -------------------------------------------------------
# Returns whether or not user is logged in
# if session username isn't set or last activity is past idle time
#   --> false
# If user is logged in
#   --> true
# -------------------------------------------------------
function isLoggedIn(){
    global $MAX_IDLE_TIME;
    # username is not set or is timed out
    if (empty($_SESSION['username']) || (time() - $_SESSION['last_activity']) > $MAX_IDLE_TIME)
        return false;
    
    # All is good, user logged in 
    return true;
}
?>