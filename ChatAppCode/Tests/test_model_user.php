<?php
$test_dir = __DIR__;
require("$test_dir/framework.php");
require("$test_dir/../Model/DB_user.php");

$log_file = "$test_dir/test_model_log.log";
$login = "validate_user";
$connect = "connect";
$create_user = "create_user";
$user_exists = "user_exists";
$delete_user = "deactivate_user";
$database = "chattest";

# --------------------------------------------------------------------------
# Set up initial state
# --------------------------------------------------------------------------
# use a special log file for this
ini_set('error_log',$log_file);
error_reporting(E_ALL);  

    
# =====================================================================
# Testing Model/DB_User.php
# =====================================================================
print "\n\n";
print "========================================================\n";
print "TESTING Model/DB_User.php\n";
print "========================================================\n";
    
# --------------------------------------------------------------------------
# Do I have the required files and functions?
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Files and functions exist?\n";
print "--------------------------------------------------------\n";

$functions_ok = 0;

$functions_ok += isa_function($connect,"'$connect' function exists");
$functions_ok += isa_function($login,"'$login' function exists");
$functions_ok += isa_function($create_user, "'$create_user' function exists");
$functions_ok += isa_function($user_exists, "'$user_exists function exists");
$functions_ok += isa_function($delete_user, "'$delete_user function exists");

if ($functions_ok != 5) {
    bail_out('Missing some functions');
}


# --------------------------------------------------------------------------
# SETUP - EMPTY DATABASE OF ALL DATA
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Setting up database\n";
print "--------------------------------------------------------\n";

# set up the test databases 
`mysql -u root < $test_dir/create_db.sql`;

# connect to the bad database;
try {
    $dbh_bad = connect('baddb');
}
catch (Exception $e) {
    bail_out('Could not connect to baddb - Aborting tests');
}

# connect to the database;
try {
    $dbh = connect($database);
}
catch (Exception $e) {
    bail_out('Could not connect to $database - Aborting tests');
}

# --------------------------------------------------------------------------
# CREATE USERS
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "create_user\n";
print "--------------------------------------------------------\n";

$result = create_user($dbh,'sandy','sandy','sandy@sandy');
ok ($result,"Create user 'sandy/sandy'");

$result = create_user($dbh,'sandy','sandy','sandy@sandy');
ok (!$result, "Cannot create user 'sandy/sandy', name already exists");

$result = create_user($dbh,'Sandy','sandy','sandy@sandy');
ok (!$result,"Cannot create user 'Sandy/sandy', name already exists (case insensitive)");

$result = create_user($dbh,'Bette','bette','');
ok ($result,"create user 'Bette/bette' with empty email string");

# throws exceptions if username or password not set
try{
    create_user($dbh,'Bob','','bob@bob');
    ok (false, "Exception was NOT thrown when password was empty");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when password was empty");
}

try{
    create_user($dbh,'','bob','bob@bob');
    ok (false, "Exception was NOT thrown when username was empty");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when username was empty");
}

# throws exceptions if the database is bad
$dbh_bad = connect('baddb');
try{ create_user($dbh_bad,'Bob','Bob','bob@bob');
    ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e) {
    ok (true, "Exception was thrown when database was bad");
}


# --------------------------------------------------------------------------
# validating user
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "validate_user\n";
print "--------------------------------------------------------\n";

$dbh = connect($database);
ok (validate_user($dbh,'sandy','sandy'),"'sandy/sandy' validated");
ok (validate_user($dbh,'Bette','bette'),"'Bette/bette' validated");
ok (!validate_user($dbh,'sandy','abcde'),"'sandy/abcde' invalid");
ok (validate_user($dbh,'SaNdY','sandy'),"'saNdY/sandy' validated - name is case insensitive");
ok (!validate_user($dbh, 'sandy','Sandy'),"'sandy/Sandy' invalid - password IS case sensitive");
ok (!validate_user($dbh, 'bob','bob'),"'bob/bob' invalid");

# throws exceptions if the database is bad
$dbh_bad = connect('baddb');
try{
    validate_user($dbh_bad,'sandy','sandy');
    ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e){
    ok (true, "Exception was thrown when database was bad (".$e->getMessage().")");
}

# --------------------------------------------------------------------------
# user exists?
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "user_exists\n";
print "--------------------------------------------------------\n";

$dbh = connect($database);
ok (user_exists($dbh,'sandy'),"'sandy exists");
ok (user_exists($dbh,'Bette'),"'Bette' exists");
ok (user_exists($dbh,'SaNdY'),"'saNdY' exists - name is case insensitive");
ok (!user_exists($dbh, 'Santa'),"'Santa' does not exist (sorry)");

# throws exceptions if the database is bad
$dbh_bad = connect('baddb');
try{
    user_exists($dbh_bad,'sandy');
    ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e){
    ok (true, "Exception was thrown when database was bad (".$e->getMessage().")");
}


# --------------------------------------------------------------------------
# deactivate_user
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "deactivate_user\n";
print "--------------------------------------------------------\n";

$dbh = connect($database);
ok (deactivate_user($dbh,'sandy'),"Delete 'sandy'");
ok (deactivate_user($dbh,'sandy'),"'sandy no longer exists");
ok (deactivate_user($dbh,''),"returns true with empty name");
ok (deactivate_user($dbh,'Santa'),"delete Santa even though he doesn't exist - return true");

# throws exceptions if the database is bad
$dbh_bad = connect('baddb');
try{
    deactivate_user($dbh_bad,'sandy');
    ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e){
    ok (true, "Exception was thrown when database was bad");
}




# --------------------------------------------------------------------------
# Print Report
# --------------------------------------------------------------------------
finished();
exit();

    
    

    

?>