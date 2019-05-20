<?php
$test_dir = __DIR__;
require("$test_dir/framework.php");
require("$test_dir/../Model/DB_messages.php");
require("$test_dir/test_inputs/test_message.php");

$log_file = "$test_dir/test_message_log.log";
$FILE = "$test_dir/../Control/messages.php";

# Timezone offset for testing purposes
$TIME_OFFSET = 5 * 60 * 60;

# =====================================================================
# Testing messages.php
# =====================================================================
print "\n\n--------------------------------------------------------\n";
echo "TESTING Control/messages.php\n";
print "--------------------------------------------------------\n";
# Note: don't need to explicitly test if JSON string is returned,
#       because if it isn't, this test program would crash,
#       indicating an error
    
# ---------------------------------------------------------------------

print "\n--------------------------------------------------------\n";
print "Setting up database\n";
print "--------------------------------------------------------\n";

# set up the test databases dev and dev
`mysql -u root < $test_dir/chattest.sql`;

# connect to the database 'chattest'
try {
    $dbh_bad = connect('chattest');
}
catch (Exception $e) {
    bail_out('Could not connect to chattest - Aborting tests');
}

try {
    $dbh_bad = connect('baddb');
}
catch (Exception $e) {
    bail_out('Could not connect to baddb - Aborting tests');
}

print "\n--------------------------------------------------------\n";
print "apache up and running?\n";
print "--------------------------------------------------------\n";
# is apache running
$result = post(array('action'=>'xxxx',
                        'user_name' => 'alex',),
                    $FILE);
ok($result,"apache server is running");
if (!$result) {
    bail_out("Please start your apache server, OR, you forgot to print the json string!");
}

if (strpos($result,"Sign-in") != false) {
    bail_out("You need to set your 'Share' applications to 'public'");
}

# ---------------------------------------------------------------------

# ---------------------------------------------------------------------
# Save message 
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Save new message\n";
print "--------------------------------------------------------\n";
# LOGIN
login();
# ------------ bad database -------------
print "\nBad database\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Test save message from control!',
                        'database' => 'baddb',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
                    
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],99,"Save new message - 'Technical difficulties' error with bad database");
# ---------------------------------------------------------------------
# Valid
# --------------------------------------------------------------------- 
print "\nGood data\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Test save message from control!',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Save new message - no error");
is ($obj['result'],true,"Save new message - message saved");

# ---------------------------------------------------------------------
# Invalid
# ---------------------------------------------------------------------
# ------------------------ TOO LONG ------------------------
print "\nBad data\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => "$long_message",
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],8,"Save new message - 'Message too long' error number");



# ------------------------ EMPTY ------------------------
# No content
print "\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],7,"Save new message - 'Message empty' error number");

# Content is empty string
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => "",
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET
                        ),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],7,"Save new message - 'Message empty' error number");

# ------------------------ NO USER ------------------------
print "\n";
$result = post(array('action'=>'save_message',
                        'content' => "something",
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"Save new message - 'User not defined' error number");

                    
# ------------------------ NOT LOGGED IN ------------------------
print "\n";
# Different user
$result = post(array('action'=>'save_message',
                        'username' => 'alex',
                        'content' => 'Not logged in test',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Save new message - 'Not logged in' error number");

# Logoff
logoff();
                    
# Test not logged in with "correct" user
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Not logged in test',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Save new message - 'Not logged in' error number");

# ---------------------------------------------------------------------

# ---------------------------------------------------------------------
# Get messages 
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Get messages\n";
print "--------------------------------------------------------\n";
# ---------------------------------------------------------------------
# Invalid - not logged in
# ---------------------------------------------------------------------
print "\nBad data\n";
$result = post(array('action'=>'get_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Get messages - 'Not logged in' error number");
# ---------------------------------------------------------------------
# Valid
# ---------------------------------------------------------------------
print "\nGood data\n";
# LOGIN
login();
                    
$result = post(array('action'=>'get_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Get messages - no error");

// Check content of results
is(count($obj['result']),4,"Get messages - returned correct # of messages");
is(contains_all_ids($obj['result'],0,4),true, "Get messages - contains correct messages (all)");

# Save a new message, test that only most recent is returned
print "\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'A new message to check get messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
 
$result = post(array('action'=>'get_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Get messages - no error");

// Check content of results
is(count($obj['result']),1,"Get messages - returned correct # of messages");
is(contains_all_ids($obj['result'],4,5),true, "Get messages - contains correct messages (new one)");   

# ------------ bad database -------------
print "\nBad database\n";
$result = post(array('action'=>'get_messages',
                        'database' => 'baddb'),
                    $FILE);
is_valid_JSON($result,"Get messages - JSON string");
$obj = json_decode($result,true); 
is ($obj['error_number'],99,"Get messages - 'Technical difficulties' error with bad database");

# ---------------------------------------------------------------------

# ---------------------------------------------------------------------
# Get All messages 
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Get all messages\n";
print "--------------------------------------------------------\n";

# ------------ bad database -------------
print "\nBad database\n";
$result = post(array('action'=>'get_all_messages',
                        'database' => 'baddb'),
                    $FILE);
is_valid_JSON($result,"Get all messages - JSON string");
$obj = json_decode($result,true); 
is ($obj['error_number'],99,"Get all messages - 'Technical difficulties' error with bad database");

# ---------------------------------------------------------------------
# Valid
# --------------------------------------------------------------------- 
print "\nGood data\n";
$result = post(array('action'=>'get_all_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
                    
is_valid_JSON($result,"Get all messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Get all messages - no error");

// Check content of results
is(count($obj['result']),5,"Get all messages - returned correct # of messages");
is(contains_all_ids($obj['result'],0,5),true, "Get all messages - contains correct messages");

# Test Max messages - save more messages
print "\n";
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Message for testing max messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Message 2 for testing max messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Final message for testing max messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);

$result = post(array('action'=>'get_all_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);                    
is_valid_JSON($result,"Get all messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Get all messages - no error");

// Check content of results
is(count($obj['result']),7,"Get all messages - returned correct # of messages (max number of messages)");
is(contains_all_ids($obj['result'],1,8),true, "Get all messages - contains correct messages (most recent)");

# ---------------------------------------------------------------------
# Invalid - not logged in
# ---------------------------------------------------------------------
print "\nBad data\n";
# Logoff
logoff();
                    
$result = post(array('action'=>'get_all_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get all messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Get all messages - 'Not logged in' error number");


# ---------------------------------------------------------------------

# ---------------------------------------------------------------------
# Timeouts 
# ---------------------------------------------------------------------
login();
timeout(70);

# Make sure save, get messages and get all messages fail after a timeout
# Save
$result = post(array('action'=>'save_message',
                        'username' => 'alex',
                        'content' => 'Not logged in test',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Save new message - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Save new message - 'Not logged in' error number after timeout");
print "\n";

# Get Messages
$result = post(array('action'=>'get_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Get messages - 'Not logged in' error number after timeout");
print "\n";

# Get all messages
$result = post(array('action'=>'get_all_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
is_valid_JSON($result,"Get all messages - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],6,"Get all messages - 'Not logged in' error number after timeout");

# Test that session times out if user is only getting messages
login();
timeout(40);

# Get messages
$result = post(array('action'=>'get_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
                    
$result = post(array('action'=>'get_all_messages',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
timeout(30);

# User should be logged out
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Result message for testing timeout',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
$obj = json_decode($result,true);
is($obj['error_number'], 6, "Not logged in when only get_messages or get_all_messages was called between timeouts");

# Test that session DOES NOT time out if user saves a message
login();
timeout(40);

# Save a message
$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Message for testing timeout',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
timeout(30);

$result = post(array('action'=>'save_message',
                        'username' => 'sandy',
                        'content' => 'Result message for testing timeout',
                        'database' => 'chattest',
                        'tz_off' => $TIME_OFFSET),
                    $FILE);
                    
# User should NOT be logged out
$obj = json_decode($result,true);
is ($obj['error_number'], 0, "Logged in when save_message was called between timeouts");

# ============================================================
# END OF TESTS
# ============================================================

finished();
exit();

# =====================================================================================
# =============== LOGIN, LOGOFF AND CONTAINS - for testing purposes ===================
# =====================================================================================
function logoff(){
    global $test_dir;
    $result = post(array('action'=>'logoff',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");
}

function login(){
    global $test_dir;
    $result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'sandy',
                        'username' => 'sandy'),
                    "$test_dir/../Control/user.php");
}

function contains_all_ids($data, $start, $finish){
    $containsAll = true;
    for($i=$start + 1; $i <= $finish; $i++){
        $containsAll = (in_array($i, array_column($data, 'id')) && $containsAll);
    }
    return $containsAll;
}

function timeout($seconds){
    $outerLoop = $seconds/10;
    $innerLoop = $seconds/$outerLoop;
    
    print "\n\nPlease be patient, waiting for timeout\n";
    for ($i=0;$i<$outerLoop;$i++) {
        for ($j=0;$j<$innerLoop;$j++) {
            sleep(1);
            print ".";
        }
        print "\n";
    }
    print "\n";
}
?>