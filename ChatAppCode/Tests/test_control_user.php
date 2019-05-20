<?php
$test_dir = __DIR__;
require("$test_dir/framework.php");
require("$test_dir/../Model/DB_user.php");

$log_file = "$test_dir/test_user_log.log";

# =====================================================================
# Testing user.php
# =====================================================================
print "\n\n--------------------------------------------------------\n";
echo "TESTING Control/user.php\n";
print "--------------------------------------------------------\n";
# Note: don't need to explicitly test if JSON string is returned,
#       because if it isn't, this test program would crash,
#       indicating an error
    
# ---------------------------------------------------------------------

print "\n--------------------------------------------------------\n";
print "Setting up database\n";
print "--------------------------------------------------------\n";

# set up the test databases dev and dev
`mysql -u root < $test_dir/create_db.sql`;

# connect to the database 'chattest';
try {
    $dbh_bad = connect('baddb');
}
catch (Exception $e) {
    bail_out('Could not connect to baddb - Aborting tests');
}

print "\n--------------------------------------------------------\n";
print "apache up and running?\n";
print "--------------------------------------------------------\n";

# is apapche up and running?
$result = post(array('action'=>'xxxx',
                        'user_name' => 'alex',
                        'pass' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'xxxx'),
                    "$test_dir/../Control/user.php");
ok($result,"apache server is running");
if (!$result) {
    bail_out("Please start your apache server, OR, you forgot to print the json string!");
}

if (strpos($result,"Sign-in") != false) {
    bail_out("You need to set your 'Share' applications to 'public'");
}

# ---------------------------------------------------------------------

# ---------------------------------------------------------------------
# create new user - valid
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Create new user\n";
print "--------------------------------------------------------\n";
$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Create new user - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Create new user - no error");
is ($obj['username'],"alex","Create new user - 'user' set");

$result = post(array('action'=>'create_user',
                        'username' => 'sandy',
                        'password' => 'sandy',
                        'email' => '',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Create new user - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Create new user with empty email - no error");
is ($obj['username'],"sandy","Create new user - 'user' set");

# ---------------------------------------------------------------------
# create user - invalid
# ---------------------------------------------------------------------
print "\n";
$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"User already exists - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],1,"Create new user - User already exists error number");
ok (!$obj['username'],"There was error - so no user set");

# ---------------------------------------------------------------------
# create user - missing email/username/password
# ---------------------------------------------------------------------
$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Missing email - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],3,"email not set error number");
ok (!$obj['username'],"There was error - so no user set");
print "\n";

$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'email' => '',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Missing password - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"password not set error number");
ok (!$obj['username'],"There was error - so no user set");
print "\n";

$result = post(array('action'=>'create_user',
                        'email' => '',
                        'password' => 'bob',
                        'database' => 'chattest',
                        'pass' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Missing username - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"username not set error number");
ok (!$obj['username'],"There was error - so no user set");
print "\n";
                    

$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'email' => '',
                        'password' => '',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Password empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"password not set error number");
ok (!$obj['username'],"There was error - so no user set");
print "\n";

$result = post(array('action'=>'create_user',
                        'email' => '',
                        'password' => 'bob',
                        'database' => 'chattest',
                        'pass' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Username empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"username not set error number");
ok (!$obj['username'],"There was error - so no user set");
print "\n";
                    


# --------------------------------------------------------------------------
# logging in
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Logging in\n";
print "--------------------------------------------------------\n";
$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Logging in, valid - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"Logging in, valid - no error message");
is ($obj['username'],"alex","Logging in, valid - 'user' set");

$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => 'alexx'),
                        "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, invalid username - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],2,"Logging in, 'not valid' error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");
print "\n";
                        
$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alexx',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, invalid username - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],2,"Logging in, 'not valid' error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");

$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'username' => 'alexx'),
                        "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, missing password - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"Logging in, missing password error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");
print "\n";
                        
$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alexx',
                        ),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, missing username - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"Logging in, missing username error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");
print "\n";
                        
$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'username' => 'alexx',
                        'password' => ''),
                        "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, password empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"Logging in, missing password error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");
print "\n";
                        
$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alexx',
                        'username' => ''
                        ),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logging in, username empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"Logging in, missing username error number");
ok (!$obj['username'],"Logging in, invalid - 'user' not set");
print "\n";
                        
# ---------------------------------------------------------------------
# delete user
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "deactivating user\n";
print "--------------------------------------------------------\n";
$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => ''),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user, username empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"missing username error number");
ok (!$obj['username'],"'user' not set");
print "\n";

$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => '',
                        'username' => 'alse'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user, password empty string - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"missing password error number");
ok (!$obj['username'],"'user' not set");
print "\n";


$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user, missing username - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],4,"missing username error number");
ok (!$obj['username'],"'user' not set");
print "\n";

$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => '',
                        'username' => 'alse'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user, missing password - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],5,"missing password error number");
ok (!$obj['username'],"'user' not set");
print "\n";

$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => 'alex1',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user alex, invalid password - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],2,"invalid username/password");
ok (!$obj['username'],"'user' not set");
print "\n";

$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");

$obj = json_decode($result,true);
is ($obj['username'],"alex","Logged in alex, (he still exists) ");

$result = post(array('action'=>'deactivate_user',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"delete user alex, valid password - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],0,"no error");
ok (!$obj['username'],"'user' not set");
print "\n";

$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'alex',
                        'username' => 'alex'),
                    "$test_dir/../Control/user.php");

$obj = json_decode($result,true);
ok (!$obj['username'],"Couldn't log in alex, (he's been deactivated) ");
is ($obj['error_number'],2,"invalid username/password error");

# ---------------------------------------------------------------------
# invalid database (mimics server is down)
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "testing database failures\n";
print "--------------------------------------------------------\n";
$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'xxxx'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Invalid database - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],99,
                    "Experiencing technical difficulties");
print "\n";

$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'baddb'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"create_user: Invalid database table - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],99,
                    "Experiencing technical difficulties");
print "\n";

$result = post(array('action'=>'create_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'baddb'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Create user Alex");

$result = post(array('action'=>'login',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'baddb'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Login: Invalid database table - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],99,
                    "Experiencing technical difficulties");

$result = post(array('action'=>'deactivate_user',
                        'username' => 'alex',
                        'password' => 'alex',
                        'email' => 'alex@alex',
                        'database' => 'baddb'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Deactivate: Invalid database table - JSON string");
$obj = json_decode($result,true);
is ($obj['error_number'],99,
                    "Experiencing technical difficulties");
print "\n";


# ---------------------------------------------------------------------
# validating log in, timeouts, etc
# ---------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "testing persistant login, timeouts, etc\n";
print "--------------------------------------------------------\n";

# remove cookies.txt file just in case
unlink ('/home/ubuntu/workspace/cookies.txt');

# no cookies, new session, no one logged in
$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
ok (!$obj['username'],"No user logged in");

# create new user, is automatically logged in
$result = post(array('action'=>'create_user',
                        'username' => 'Bob',
                        'password' => 'bob',
                        'email' => '',
                        'database' => 'chattest'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Create new user - JSON string");
$obj = json_decode($result,true);
is (strtolower($obj['username']),"bob","Create new user bob - 'user' set");

$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
is (strtolower($obj['username']),"bob","Logged in as bob");

# log off
$result = post(array('action'=>'logoff'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged out, valid - JSON string");
$obj = json_decode($result,true);
ok (!$obj['username'],"Logged off");
is ($obj['error_number'],0,"Logged off, no error message");

$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
ok (!$obj['username'],"No one is logged in");

# specifically log in

$result = post(array('action'=>'login',
                        'database' => 'chattest',
                        'password' => 'bob',
                        'username' => 'bob'),
                    "$test_dir/../Control/user.php");

is_valid_JSON($result,"Logging in, valid - JSON string");
$obj = json_decode($result,true);
is (strtolower($obj['username']),"bob","Logging in as bob");

$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
is (strtolower($obj['username']),"bob","Logged in as bob");

$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
is (strtolower($obj['username']),"bob","Still logged in as bob");

# time out
print "\n\nPlease be patient, waiting for timeout\n";
for ($i=0;$i<7;$i++) {
    for ($j=0;$j<10;$j++) {
        sleep(1);
        print ".";
    }
    print "\n";
}

$result = post(array('action'=>'logged_in_as'),
                    "$test_dir/../Control/user.php");
is_valid_JSON($result,"Logged_in_as, valid - JSON string");
$obj = json_decode($result,true);
ok (!$obj['username'],"Timed out - No one is logged in");



# --------------------------------------------------------------------------
# Print Report
# --------------------------------------------------------------------------
finished();
exit();
