<?php
$test_dir = __DIR__;
require("$test_dir/framework.php");

############################################################################
# Assignment 2 - 
# Create error handling routines specifically for calls to the database
#
# --- REQUIREMENTS:
#
# The file containing your error handlers must be in the directory Model, 
# and it MUST be called DB_error_handling.php
# 
# Write an exception handler, a shutdown handler,
# and an error handler.
#
# Their names must be: 
# "DBexceptionHandler", "DBshutDownFunction" and "DBerrorHandler"
#
# 1) write an error_handler that:
#    a) if a E_USER_WARNING:
#         i) write the error message (and file/line # info) to the log file
#         ii) print or echo 'I am warning you!' to the terminal
#         iii) the above is written independent of the ERROR_REPORTING level
#    b) if a E_USER_ERROR
#         i) write the error message (and file/line # info) to the log file
#         ii) echo 'Sorry, we are experiencing technical difficulties'
#         iii) the above is written independent of the ERROR_REPORTING level
#    c) if anything else (E_WARNING, E_STRICT, E_E_DEPRECATED or E_USER_NOTICE,
#            E_RECOVERABLE_ERROR)
#         i) if ERROR_REPORTING level is set for that error then
#               1) write the error message (and file/line # info) to the log file
#               2) do NOT print anything to the screen
# 2) write an exception handler for all uncaught exceptions
#    a) On any uncaught exceptions, print
#       'Server Error, please try again later'
#    b) Write the exception to the error log file, (with line # and file #)
# 3) write a shutdown function that displays a generic message
#    'an unexpected error has occurred, we should be back on line soon'
#    if (and only if) there was an error just before the shutdown.
#
# I WILL BE USING THIS TEST FILE TO TEST YOUR CODE
# ... PLEASE NOTE: this test file doesn't test absoluteley everything,
#                  read requirements carefully
#
# Put this file, test_framework.php and 
# test_error_handling_inputs.php in your Tests directory
############################################################################

$test_dir = __DIR__;
$log_file = "$test_dir/test_error_handling_log.log";
$shutdown = "DBshutDownFunction";
$errorhandler = "DBerrorHandler";
$exceptionhandler = "DBexceptionHandler";
$input_file = 'test_errors.php';

# --------------------------------------------------------------------------
# Set up initial state
# --------------------------------------------------------------------------
# use a special log file for this
ini_set('error_log',$log_file);
error_reporting(E_ALL);  

# --------------------------------------------------------------------------
# Do I have the required files and functions?
# --------------------------------------------------------------------------
$files_ok = 0;
$functions_ok = 0;

print "\n=====================================\n";
print "Validate that files and functions exist\n";
print "=====================================\n";

$files_ok += include_ok("$test_dir/../Model/DB_error_handling.php", "Include DB error handler");
$files_ok += include_ok("$test_dir/test_inputs/$input_file", "Include test inputs");

if ($files_ok != 2) {
    bail_out();
}

$functions_ok += isa_function($shutdown,"$shutdown shut down function exists");
$functions_ok += isa_function($errorhandler, "$errorhandler error handler exists");
$functions_ok += isa_function($exceptionhandler, "$exceptionhandler exception handler exists");

if ($functions_ok != 3) {
    bail_out();
}

# --------------------------------------------------------------------------
# set up the error levels, and handlers for testing
# --------------------------------------------------------------------------
error_reporting(0);  # suppress all error handling by php
set_error_handler($errorhandler);
set_exception_handler($exceptionhandler);
register_shutdown_function($shutdown);

# --------------------------------------------------------------------------
# check code good when no errors
# --------------------------------------------------------------------------
print "\n=====================================\n";
print "Are input files good\n";
print "=====================================\n";

ok(db_validate_login('x','x'),'validate login true');
is(get_db_connection('prod'),42,'get_db_connection returns 42');
no_output(function(){get_db_connection('prod');},'get_db_connection does not generate output');
no_output(function(){db_validate_login('x','x');},'db_validate_login does not generate output');

# --------------------------------------------------------------------------
# E_USER_WARNING
# --------------------------------------------------------------------------
# run db_validate_login - trigger USER_WARNING
# check output, and validate message to log file
# output = "System is temporarily down, please try again in 5 minutes"
# log = whatever was the message from the USER_WARNING
# --------------------------------------------------------------------------
print "\n=====================================\n";
print "Testing E_USER_WARNING\n";
print "=====================================\n";

if (file_exists($log_file)) {unlink ($log_file);}

print "\nerror_reporting set to 0\n";
error_reporting(0);  # should handle user warnings if error_reportin is set to zero
$output = run_quietly (function(){db_validate_login('x','hacking');});

is($output,'I am warning you!',
    "generic warning message issued to user (\"I am warning you!\")");

ok (file_exists($log_file),'data written to log file for user warning');

file_contains($log_file,"Someone is trying to hack our system",
    "user warning message sent to log file");

file_contains($log_file,"$input_file","file name in msg in log file");
file_contains($log_file,"13","line number 13 in msg in log file");

print "\nerror_reporting set to E_ALL\n";
error_reporting(E_ALL);  # should handle user warnings if error_reportin is set to zero
$output = run_quietly (function(){db_validate_login('x','hacking');});

is($output,'I am warning you!',
    "generic warning message issued to user (\"I am warning you!\")");

ok (file_exists($log_file),'data written to log file for user warning');

file_contains($log_file,"Someone is trying to hack our system",
    "user warning message sent to log file");

file_contains($log_file,"$input_file","file name in msg in log file");
file_contains($log_file,"13","line number 13 in msg in log file");

# --------------------------------------------------------------------------
# E_WARNING
# --------------------------------------------------------------------------
# run db_validate_login - trigger E_WARNING, 
# check output, and validate message is printed to log file
# output = nothing (independent of error_reporting)
# log = warning message if error_reporting set, otherwise nothing
# --------------------------------------------------------------------------

print "\n=====================================\n";
print "Testing E_WARNING\n";
print "=====================================\n";

print "\nerror_reporting set to E_ALL\n";
error_reporting(E_ALL);  # capture this warning
ini_set('display_errors','On');  # behaviour should be independent of display_errors

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){db_validate_login('x','warning');});
is($output,'',"Suppress messages to terminal for E_WARNING");
ok (file_exists($log_file),'data written to log file for warning');
file_contains($log_file,"Division by zero","real warning message sent to log file");
file_contains($log_file,"$input_file","file name in msg in log file");
file_contains($log_file,"18","line number 18 in msg in log file");

print "\nerror_reporting set to 0\n";
error_reporting(0);  # don't capture this warning
ini_set('display_errors','On');  # behaviour should be independent of display_errors

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){db_validate_login('x','warning');});
is($output,'', "No message sent to user for E_WARNING");
ok (! file_exists($log_file),'data not written to log file for warning');

# --------------------------------------------------------------------------
# E_NOTICE
# --------------------------------------------------------------------------
# run get_db_connection - trigger E_NOTICE, 
# check output, and validate message is printed to log file
# output = nothing (independent of error_reporting)
# log = notice message if error_reporting set, otherwise nothing
# --------------------------------------------------------------------------

print "\n=====================================\n";
print "Testing E_NOTICE\n";
print "=====================================\n";

print "\nerror_reporting set to E_ALL\n";
error_reporting(E_ALL);  # capture this notice
ini_set('display_errors','On');  # behaviour should be independent of display_errors

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){get_db_connection('notice');});
is($output,'',"Suppress messages to terminal for E_NOTICE");
ok (file_exists($log_file),'data written to log file for notice');
file_contains($log_file,"Undefined variable","real notice message sent to log file");
file_contains($log_file,"$input_file","file name in msg in log file");
file_contains($log_file,"27","line 27 number in msg in log file");

print "\nerror_reporting set to 0\n";
error_reporting(0);  # don't capture this notice
ini_set('display_errors','On');  # behaviour should be independent of display_errors

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){get_db_connection('notice');});
is($output,'', "No message sent to user for E_NOTICE");
ok (! file_exists($log_file),'data not written to log file for notice');

# --------------------------------------------------------------------------
# E_USER_ERROR
# --------------------------------------------------------------------------
# run get_db_connection - trigger E_USER_ERROR
# check output, and validate message is printed to log file
# output = nothing (independent of error_reporting)
# log = error message if error_reporting set, otherwise nothing
# does not exit!
# --------------------------------------------------------------------------
print "\n=====================================\n";
print "Testing E_USER_ERROR\n";
print "=====================================\n";

print "\nerror_reporting set to 0\n";
error_reporting(0);  # report user errors, even if error reporting is off
ini_set('display_errors','On'); 

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){get_db_connection('error');});
is($output,'Sorry, we are experiencing technical difficulties',"Generic message printed for E_USER_ERROR");
ok (file_exists($log_file),'data written to log file for E_USER_ERROR');
file_contains($log_file,"Trying to connect to a DB that doesn't exist",
    "E_USER_ERROR message sent to log file");
file_contains($log_file,"$input_file","file name in msg in log file");
file_contains($log_file,"37","line number 37 in msg in log file");

print "\nerror_reporting set to E_ALL\n";
error_reporting(E_ALL);  # report user errors, even if error reporting is off
ini_set('display_errors','On'); 

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){get_db_connection('error');});
ok(1,"We did not crash the program (yeah!)");

# --------------------------------------------------------------------------
# EXCEPTIONS
# test cheats... uses try/catch, and calls DBexceptionHandler explicitly
# --------------------------------------------------------------------------
# run get_db_connection - trigger EXCEPTION
# check output, and validate message is printed to log file
# output =  Server Error, please try again later
# log = exception message
# --------------------------------------------------------------------------
print "\n=====================================\n";
print "Testing EXCEPTIONS\n";
print "=====================================\n";

print "\nerror_reporting set to 0\n";
error_reporting(0);  # report exceptions, even if error reporting is off
ini_set('display_errors','On'); 

if (file_exists($log_file)) {unlink ($log_file);}
$output = run_quietly (function(){try{get_db_connection('crashed');}catch(Exception $e){DBexceptionHandler($e);}});
is($output,'Server Error, please try again later',"Generic message printed for Exceptions");
ok (file_exists($log_file),'data written to log file for Exception');
file_contains($log_file,"Very explicit info about what caused the error",
    "Exception message sent to log file");

# --------------------------------------------------------------------------
# print final results
# --------------------------------------------------------------------------
finished();

?>
