<?php
# ==================================================================
# This is a dummy file for generating errors, and throwing
# exceptions
# ==================================================================

function db_validate_login ($n,$p,$schema = 'prod') {
    
    $dbh = get_db_connection($schema);
    
    
    if ($p=='hacking') {
        trigger_error("Someone is trying to hack our system?",E_USER_WARNING);
        return false;
    }
    
    if ($p == 'warning') {
        $y = 1/0;  # generate an 'unintentional warning';
    }
    
    return true;
}

function get_db_connection ($schema) {
    switch (strtolower($schema)) {
        case 'notice':
            $x = $y + 1; # // generates a NOTICE error
            break;
        case 'prod':
            break;
        case 'crashed':
            throw new Exception("Very explicit info about what caused the error!");
            break;
        case 'bug':
            call_to_unknown_function('Hello World');
        default:
            trigger_error("You are trying to connect to a DB that doesn't exist",E_USER_ERROR);
    }
    return 42;  # 42 is the answer to life, the  universe, and everything
}

?>
