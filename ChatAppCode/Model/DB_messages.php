<?php
$dir = __DIR__;

error_reporting(E_ALL);
ini_set('display_errors','On');
require_once("$dir/DB_user.php");
require_once("$dir/date_format.php");

# =========================================================================
# connect
# =========================================================================
# purpose: Connect to the requested database
# inputs: database name (defaults to "chat")
# outputs: mysqli object (database handle)
# errors: Throws an exception if there is a database error
# ---------------------------------------------------------------------------
# ALREADY IN DB_USER

# =========================================================================
# get_messages_older_than
# =========================================================================
# purpose: get all the messages that are older than the specified id 
#               - (maximum number of messages is 50)
# inputs: database handle, message ID (defaults to 0)
# outputs: array of messages (message id, content, the sender, time it was sent)
#               - Each message is formatted into an associative array:
#                       "content" => string
#                       "id" => integer
#                       "username" => string
#                       "time" = > string
# errors: Throws an exception if:
#               - there is a database error
#               - message ID is not an integer or is negative
# ---------------------------------------------------------------------------
function get_messages_older_than($dbh, $msgID=0){
        global $tz_off;
        $MAX_MESSAGES = 200;
        
        if(!is_int($msgID))
                 throw new exception("Message ID must be an integer");
        if($msgID < 0)
                 throw new exception("Message ID cannot be negative");
                 
        # Get list of messages with user and their avatar  
        $sql = "select id, userID, avatar, content, UNIX_TIMESTAMP(time) as time 
                from messages t1
                inner join users t2 on t1.userID = t2.username
                where id > ? 
                order by id 
                limit ?";

        # bind and prepare
        $sth = $dbh->prepare($sql);
        $sth->bind_param("ii", $msgID, $MAX_MESSAGES);
        $sth->bind_result($id, $user, $avatar, $text, $time);

        $messagesArray=array();
        $sth->execute();
        while($sth->fetch()){
                # Format the date
                $time= empty($tz_off) ? format_time_info($time, 0) : format_time_info($time, $tz_off);
                $next=array(
                       "content" => $text,
                       "id" => $id,
                       "username" => $user,
                       "avatar" => $avatar,
                       "time" => $time);
                array_push($messagesArray, $next);
        }
        return $messagesArray;
}

# =========================================================================
# save_message
# =========================================================================
# purpose: a new message is added to the "message" table in database 
# inputs: database handle, sender's username, the contents of the message
# outputs: null
# errors: Throws an exception if:
#               - there is a database error
#               - Message is too long (Max 5000 characters)
#               - Message is empty
#               - User doesn't exist/is empty
# ---------------------------------------------------------------------------
function save_message($dbh, $user, $content){
        $MAX_LEN = 5000;
        if(empty($user) || !user_exists($dbh, $user))
                throw new exception("Must have existing username");
        if(empty($content))
                throw new exception("Must have non empty message");
        if(strlen($content) > $MAX_LEN)
                throw new exception("Message is too long");
           
        $sql = "insert into messages (userID, content, time) 
                values (?, ?, NOW())";
        
        # Prepare and bind params
        $sth = $dbh->prepare($sql);
        $sth->bind_param("ss", $user,$content);
        
        # add the message
        $sth->execute();
}

# =========================================================================
# change_user_avatar
# =========================================================================
# purpose: the user's avatar blob is updated in the database
# inputs: database handle, username, the new image blob
# outputs: null
# errors: Throws an exception if:
#               - there is a database error
#               - blob is empty
#               - User doesn't exist/is empty
# ---------------------------------------------------------------------------
function change_user_avatar($dbh, $user, $image){
        if(empty($user) || !user_exists($dbh, $user))
                throw new exception("Must have existing username");
        if(empty($image))
                throw new exception("Must have non empty avatar blob");
                
                   
        $sql = "update users set avatar=?
                where username=?";
        
        # Prepare and bind params
        $sth = $dbh->prepare($sql);
        $sth->bind_param("ss", $image, $user);
        
        # update the record
        $sth->execute();
}

function set_time_offset($offset){
        global $tz_off;
        $tz_off = $offset;
}

?>