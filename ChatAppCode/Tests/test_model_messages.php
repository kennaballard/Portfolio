<?php
$test_dir = __DIR__;
require("$test_dir/framework.php");
require("$test_dir/../Model/DB_messages.php");
require("$test_dir/test_inputs/test_message.php");

$log_file = "$test_dir/test_model_log.log";
$connect = "connect";
$save_message = "save_message";
$get_messages_older_than = "get_messages_older_than";
$database = "chattest";

# --------------------------------------------------------------------------
# Setup
# --------------------------------------------------------------------------
ini_set('error_log',$log_file);
error_reporting(E_ALL);  

# =====================================================================
# Testing Model/DB_messages.php
# =====================================================================
print "\n\n";
print "========================================================\n";
print "TESTING Model/DB_messages.php\n";
print "========================================================\n";
    
# --------------------------------------------------------------------------
# Do I have the required files and functions
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Files and functions exist?\n";
print "--------------------------------------------------------\n";

$functions_ok = 0;
$functions_ok += isa_function($connect,"'$connect' function exists");
$functions_ok += isa_function($save_message,"'$save_message' function exists");
$functions_ok += isa_function($get_messages_older_than,"'$get_messages_older_than' function exists");

if ($functions_ok != 3) {
    bail_out('Missing some functions');
}

# --------------------------------------------------------------------------
# SETUP - EMPTY DATABASE OF ALL DATA
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Setting up database\n";
print "--------------------------------------------------------\n";

# set up the test databases 
`mysql -u root < $test_dir/chattest.sql`;

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
# SAVE MESSAGE
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Save_message\n";
print "--------------------------------------------------------\n";

# ------------ Good data ------------- 
print "\nGood data\n";
$dbh = connect($database);
try{
    save_message($dbh,'sandy','A Message Test');
    ok (true,"Save message from sandy");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data");
}

try{
    save_message($dbh,'asda','Yet another Message Test');
    ok (true,"Save message from asda");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data");
}

try{
    save_message($dbh,'alex','Another Message Test');
    ok (true,"Save message from alex");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data");
}

# ------------ bad data -------------

print "\nBad data\n";
# ------------ bad user -------------
try{
    save_message($dbh,'','A Message Test');
    ok (false,"Exception was NOT thrown when username was empty");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when username was empty");
}

try{
    save_message($dbh,'nonexistantuser','A Message Test');
    ok (false,"Exception was NOT thrown when username didn't exist");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when username didn't exist");
}

# ------------ bad content -------------
try{
    save_message($dbh,'sandy','');
    ok (false,"Exception was NOT thrown when content was empty");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when content was empty");
}

try{
    save_message($dbh,'sandy',"$long_message");
    ok (false,"Exception was NOT thrown when content was too long");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when content was too long");
}

# ------------ bad database -------------
print "\nBad database\n";
$bad_dbh=connect();
try{
    save_message($bad_dbh, 'sandy', "baddb test");
 ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e){
    ok (true, "Exception was thrown when database was bad (".$e->getMessage().")");
}

# --------------------------------------------------------------------------
# GET MESSAGES OLDER THAN
# --------------------------------------------------------------------------
print "\n--------------------------------------------------------\n";
print "Get_messages_older_than\n";
print "--------------------------------------------------------\n";

# ------------ Good data ------------- 
print "\nGood data\n";
$dbh = connect($database);
try{
    $msgs=get_messages_older_than($dbh);
    ok (true,"Get all messages executed with no error");
    if(contains_id(1, $msgs) && contains_id(2, $msgs) && contains_id(3, $msgs) && contains_id(4, $msgs) && contains_id(5, $msgs) && contains_id(6, $msgs))
        ok(true, "All messages were returned in return all messages");
    else
        ok(false, "Some messages are missing from return all messages");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data (get all messages)");
}

try{
    $msgs=get_messages_older_than($dbh, 6);
    ok (true,"Get messages executed with no error");
    
    if(empty($msgs))
        ok(true, "There were no new messages; none were returned");
    else
        ok(false, "Some messages were returned when no new messages");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown instead of returning no messages (get messages)");
}

try{
    $msgs=get_messages_older_than($dbh, 8);
    ok (true,"Get messages executed with no error");
    
    if(empty($msgs))
        ok(true, "No messages returned (last seen message is higher than database entries)");
    else
        ok(false, "Some messages were returned when no new messages");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown instead of returning no messages (get messages)");
}

try{
    $msgs=get_messages_older_than($dbh,5);
    ok (true,"Get messages executed with no error");
    if(contains_id(6, $msgs) && count($msgs) == 1)
        ok(true, "All messages were returned in return messages (one result)");
    else
        ok(false, "Some messages are missing from return messages");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data (get messages)");
}

try{
    $msgs=get_messages_older_than($dbh,4);
    ok (true,"Get messages executed with no error");
    if(contains_id(5, $msgs) && contains_id(6, $msgs) && count($msgs) == 2)
        ok(true, "All messages were returned in return messages (two results)");
    else
        ok(false, "Some messages are missing from return messages");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (false, "Exception was thrown with good data (get messages)");
}

# ------------ bad data -------------
print "\nBad data\n";
try{
    $msgs=get_messages_older_than($dbh,-2);
    ok (false,"Exception was NOT thrown when last message seen was negative");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when last message seen was negative");
}

try{
    $msgs=get_messages_older_than($dbh,"Not a number");
    ok (false,"Exception was NOT thrown when last message seen was not a number");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when last message seen was not a number");
}

try{
    $msgs=get_messages_older_than($dbh,2.5);
    ok (false,"Exception was NOT thrown when last message seen was not an integer");
}
catch(Exception $e) {
    $output = $e->getMessage();
    ok (true, "Exception was thrown when last message seen was not an integer");
}

# ------------ bad database -------------
print "\nBad database\n";
$bad_dbh=connect();
try{
    get_messages_older_than($bad_dbh);
 ok (false,"Exception was NOT thrown when database was bad");
}
catch(Exception $e){
    ok (true, "Exception was thrown when database was bad (".$e->getMessage().")");
}

# ============================================================
# END OF TESTS
# ============================================================

finished();
exit();

# ===========================================================
# CONTAINS ID
# Returns whether or not a message is in the returned data
function contains_id($id, $data){
    return in_array($id, array_column($data, 'id'));
}
?>