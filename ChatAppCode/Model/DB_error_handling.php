<?php
# Kennedy Ballard
# 1649618

# Exception handler for all uncaught exceptions
# ---> Displays message
# ---> logs exception

function DBexceptionHandler(Exception $ex){
    # print message to terminal
    print "Server Error, please try again later";
    
    # write exception to log file (line #, file #)
    $logMsg = ($ex->getMessage()) . " File: " . ($ex->getFile()) . " Line number " . ($ex->getLine());
    error_log($logMsg, 0);
}

# ShutDown Handler
# ---> Displays generic message
# ---> Does not log the shutdown

function DBshutDownFunction() {
    # Stop from logging or displaying errors
    ini_set('log_errors', 'Off');
    ini_set('display_errors','Off');
    $err = error_get_last();
    if(!is_null($err))
        # print message to terminal
        print "An unexpected error has occurred, we should be back online soon";
}


# Error handler
# ---> handles errors based on the type of error that occured
# ---> specific message for E_USER_WARNING and E_USER_ERROR
#      => Logs the error
# ---> No message for all other errors
#      => Only logs error if ERROR_REPORTING is set

function DBerrorHandler($errno, $errstr, $errfile, $errline) {
    ini_set('display_errors','Off');
    $logMsg = $errstr . " File: " . $errfile . " Line number " . $errline;
    switch($errno){
        case E_USER_WARNING:
            # write error msg to log file
            error_log($logMsg,0);
            
            # print message to terminal
            echo 'I am warning you!';
            break;
        case E_USER_ERROR:
            # write error msg to log file
            error_log($logMsg,0);
            
            #print message to terminal
            echo 'Sorry, we are experiencing technical difficulties';
            break;
        default:
            # if ERROR_REPORTING is set for error
            if(error_reporting() & $errno){
                # write msg to log file
                error_log($logMsg,0);
            }
    }
}

?>