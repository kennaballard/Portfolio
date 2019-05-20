<?php

# Constants
define('CLOUD_9_APACHE_ROOT','/home/ubuntu/workspace');

# ============================================================================
# Sandy's test framework
# ============================================================================
# This file provides functions that can be used to simplify unit testing
# - it is NOT robust by any means, but does what it needs to
#
# Example use:
#   require 'test_framework.php';
#   $x = foo(33);
#   is ($x, 15, 'tested function foo');
#   is ($sandy, 'wonderful', "Gotta pass this test!")
#   finished();
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Global variables
# ----------------------------------------------------------------------------
$TESTS_PASSED = 0;
$TESTS_RUN = 0;

# ----------------------------------------------------------------------------
# function ok ($boolean, $comment)
# - if $boolean true, test passed, else test fails
# ----------------------------------------------------------------------------
function ok ($boolean,$comment) {
    if ($boolean) {
        return _print_ok($comment);
    }
    else {
        return _print_not_ok($comment,debug_backtrace());
    }
    
}

# ----------------------------------------------------------------------------
# function bail_out()
# - stops execution of the test suite
# ----------------------------------------------------------------------------
function bail_out($comment = "") {
    $trace = debug_backtrace(); 
    print "-------------------------\n\n";
    print "*********** BAILED OUT *********\n";
    print "at ".$trace[0]['file']." line: ".$trace[0]['line']."\n";
    if ($comment) {
        print "REASON: $comment\n";
    }
    print "\n\n";
    exit;
}

# ----------------------------------------------------------------------------
# function include_ok($file, $comment)
# - tries to inlcude $file
#   - if $file doesn't exist, or if $file has an error - test fails
#   - else includes $file, and test passes
# ----------------------------------------------------------------------------
$errmsg = "";
function include_ok($file,$comment) {
    
    global $errmsg;
    
    # fail if file does not exist
    if (! file_exists($file)) {
        return _print_not_ok($comment,debug_backtrace());
    }
    
    # turn off all error reporting for now and set error handler
    # capture error message (but don't print it)
    $old_error_reporting = error_reporting();
    $errmsg = "";
    error_reporting(0);
    set_error_handler("_nothing");
    
    # include the file
    include("$file");
    
    # return error reporting back to what it was
    error_reporting($old_error_reporting);
    
    # if an error message was set (via the error handler 'nothing')
    # then there was an error in the include file, so this test fails
    if ($errmsg) {
        return _print_not_ok($comment."\n\t$errmsg",debug_backtrace());
    }
    
    # finally, if we got here, this test passed
    else {
        return _print_ok($comment);
    }
}
function _nothing ($num,$msg,$file,$line) {
    global $errmsg;
    $errmsg = "$msg in file $file on line $line";
}

# ----------------------------------------------------------------------------
# function isa_function ($function, $comment)
# - if function exists, test passes, else fails
# ----------------------------------------------------------------------------
function isa_function ($function,$comment) {
    if (function_exists($function)) {
        return _print_ok($comment);
    }
    else {
        return _print_not_ok($comment,debug_backtrace());
    }
    
}

# ----------------------------------------------------------------------------
# function is( $input, $should_be, $comment)
# if $input === $should_be, then test passes, otherwise fails
# ----------------------------------------------------------------------------
function is($input,$should_be,$comment) {
    if ($input === $should_be) {
        return _print_ok($comment);
    }
    else {
        $comment .= "\n\tExpected: $should_be\n\tGot: $input";
        return _print_not_ok($comment,debug_backtrace());
    }
}

# ----------------------------------------------------------------------------
# function no_output ( $callback, $comment)
# - runs $callback routine
#   - if no output is printed to screen, test passes, otherwise fails
# ----------------------------------------------------------------------------
function no_output($callback,$comment) {
    
    # get text that would normally be printed to the screen
    $output = run_quietly($callback);

    # if there was output, test fails
    if ($output) {
        $comment = $comment . "\n\tOutput was:\n\t\t'$output'";
        return _print_not_ok($comment,debug_backtrace());
    }
    
    # else test passes
    else {
        return _print_ok($comment);
    }
}

# ----------------------------------------------------------------------------
# function is_output ( $callback, $should_be, $comment)
# - run $callback routine
#   - compare printed output to $should_be
#   - if printed output === $should_be, then test passes, otherwise fails
# ----------------------------------------------------------------------------
function is_output($callback,$should_be,$comment) {
    
    # get text that would normally be printed to the screen
    $output = run_quietly($callback);

    # if output is what is should be, test passed
    if ($output == $should_be) {
        return _print_ok($comment);
    }
    
    # otherwise test fails
    else {
        $comment = $comment . "\n\tOutput was:\n\t\t'$output'";
        return _print_not_ok($comment,debug_backtrace());
    }
}

# ----------------------------------------------------------------------------
# function run_quietly ($callback)
# - runs callback so that no text is printed to screen, instead it is 
#   returned to the caller
# - RETURNS: output that would normally be printed to the screen
# ----------------------------------------------------------------------------
function run_quietly($callback) {
    ob_start();
    $callback();
    $output = ob_get_contents();
    ob_end_clean();
    return $output;
}

# ----------------------------------------------------------------------------
# function file_contains ($file, $str, $comment)
# - if $file does not exist, test fails
# - reads file $file and looks for $str (using regular expressions)
#   if (case insensitive) $str is found in $file, test passes, otherwise fails
# ----------------------------------------------------------------------------
function file_contains ($file,$str,$comment) {
    
    # if file doesn't exist, fail
    if (! file_exists($file)) {
        $comment = $comment ."\n\tFile <$file> does not exist";
        return _print_not_ok($comment,debug_backtrace());
    }
    
    # read content of file all at once (dangerous if large files!)
    $content = file_get_contents($file);
    
    # if regex string can be found, test passes
    if (preg_match("/$str/i",$content)) {
        return _print_ok($comment);
    }
    
    # else test fails
    else {
        return _print_not_ok($comment,debug_backtrace());
    }
}

# ----------------------------------------------------------------------------
# function exception_ok ($callback, $comment)
# - Running the callback should generate an exception
# - if no exception is raised - fails test, otherwise passes
# ----------------------------------------------------------------------------
function exception_ok ($callback, $comment) {
    $trace = debug_backtrace();
    
    try {
        $callback();
        return _print_not_ok($comment,$trace);
    }
    catch (Exception $e){
        return _print_ok($comment);
    }
}

# ----------------------------------------------------------------------------
# function regex_is ($string,$pattern,$comment)
# if $string matches $pattern, then test passes, otherwise fails
# ----------------------------------------------------------------------------
function regex_is ($string, $pattern, $comment) {

    if (preg_match($pattern,$string) === 1) {
        return _print_ok($comment);
    }
    else {
        $comment .= "\n\tPattern: $pattern\n\tString: $string";
        return _print_not_ok($comment,debug_backtrace());
    }

}

# ----------------------------------------------------------------------------
# function regex_isnt ($string,$pattern,$comment)
# if $string does NOT matche $pattern, then test passes, otherwise fails
# ----------------------------------------------------------------------------
function regex_isnt ($string, $pattern, $comment) {
        
    if (preg_match($pattern,$string) === 0) {
        return _print_ok($comment);
    }
    else {
        $comment .= "\n\tPattern: $pattern\n\tString: $string";
        return _print_not_ok($comment,debug_backtrace());
    }

}

# ----------------------------------------------------------------------------
# function post_result_is($post_data,$file,$result,$comment)
# - Converts $file to proper url, create post_context, read $file
# - Compare output from reading file to $result.  If same, test passed
# - NOTE: Using regex (case insensitive) comparison
# ----------------------------------------------------------------------------
function post_result_is ($post_data, $file, $result, $comment) {
        
    $content = post($post_data,$file);
    
    # if regex string can be found, test passes
    if (preg_match('/\Q'.$result.'/i',$content)) {
        return _print_ok($comment);
    }
    
    # else test fails
    else {
        $comment .= "\n\tURL: $url\n\tExpected: $result\n\tReturned:$content";
        return _print_not_ok($comment,debug_backtrace());
    }

}

# ----------------------------------------------------------------------------
# function post($post_data,$file)
# - Converts $file to proper url, create post_context, read $file
# - returns the $file contents
# ----------------------------------------------------------------------------
function post($data, $file, $cookie_jar = '/home/ubuntu/workspace/cookies.txt') {
        
    # create the url from the file name.
    $url = get_url_from_file($file);

    // curl object with options
    $c = curl_init($url);
    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($c, CURLOPT_COOKIEFILE, $cookie_jar);
    curl_setopt($c, CURLOPT_USERAGENT,'Testing');
    curl_setopt($c, CURLOPT_COOKIEJAR, $cookie_jar);
    curl_setopt($c, CURLOPT_POST, 1);
    curl_setopt($c, CURLOPT_POSTFIELDS, http_build_query($data));

    // get the page via curl
    $page = curl_exec($c);
    curl_close($c);
    return $page;

}

# ----------------------------------------------------------------------------
# function get_url_from_file($file)
# - Converts $file (local drive) to proper url
# ----------------------------------------------------------------------------
function get_url_from_file ($file) {
        $url = "http://".preg_replace(':\Q'.CLOUD_9_APACHE_ROOT.':i',
    getenv("C9_HOSTNAME"), $file);
//    $url = preg_replace(" ","%20",$url);
    return $url;
}
# ----------------------------------------------------------------------------
# function is_valid_JSON ($string,$comment)
# - if $string is valid JSON, passes, otherwise fails
# ----------------------------------------------------------------------------
function is_valid_JSON($string,$comment) {
    json_decode($string);
    if (json_last_error() === JSON_ERROR_NONE) {
        _print_ok($comment);
    }
    else {
        $comment .= "\n\tString: $string";
        _print_not_ok($comment,debug_backtrace());
    }
}

# ----------------------------------------------------------------------------
# function finished ()
# - print summary of all test results
# ----------------------------------------------------------------------------
function finished () {
    global $TESTS_PASSED, $TESTS_RUN;
    print "---------------------------\n\n";
    if ($TESTS_RUN == $TESTS_PASSED) {
        print "Everything Good! All $TESTS_RUN tests passed\n\n";
    }
    else {
        print "****** NOT GOOD *******\n";
        print ($TESTS_RUN - $TESTS_PASSED). " of $TESTS_RUN failed!\n\n";
    }
    
}

# ----------------------------------------------------------------------------
# private functions for printing results
# ----------------------------------------------------------------------------
function _print_ok ($comment) {
    global $TESTS_PASSED, $TESTS_RUN;
    $TESTS_RUN++;
    $TESTS_PASSED++;
    print sprintf("%4d  ok    %s\n",$TESTS_RUN,$comment);
    return 1;
}

function _print_not_ok ($comment,$trace) {
    global $TESTS_PASSED, $TESTS_RUN;
    $TESTS_RUN++;
    print sprintf("%4d  FAIL  %s\n",$TESTS_RUN,$comment);
    print $trace[0]['file']." line: ".$trace[0]['line'];
    print "\n\n";
    return 0;
}
    

?>