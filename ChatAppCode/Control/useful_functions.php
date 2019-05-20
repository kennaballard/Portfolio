<?php
    # ========================================================================
    # any code that deals with sessions has a function in here so that 
    # we can easily find any SESSION key in ONE file
    #
    # other shared functionality is also in this file
    # ========================================================================
    $dir = __DIR__;
    $workspace = "$dir/..";
    
    # ========================================================================
    # initialization
    # ========================================================================
    session_start();   # note this must be called before ANYTHING is printed

    # ------------------------------------------------------------------------
    # read the dictionary
    # ------------------------------------------------------------------------
    include_once("$workspace/View/dictionary.php");
    
    # ------------------------------------------------------------------------
    # set the idle time
    # ------------------------------------------------------------------------
    define('ONE_HOUR', 60*60);  // 60 minutes * 60 seconds
    $MAX_IDLE_TIME = ONE_HOUR;
    
    // change time out for testing purposes
    if ($_SERVER['HTTP_USER_AGENT'] === "Testing") {
        $MAX_IDLE_TIME = 60;
    }
    
    # ========================================================================
    # function is_logged_in
    # ========================================================================
    function is_logged_in() {

        global $MAX_IDLE_TIME;
        if (empty($_SESSION['last_activity'])) {
            $_SESSION['last_activity'] = 0;
        }
    
        // session name is set, and not timed out
        if (!empty ($_SESSION['username']) 
            && (time()-$_SESSION['last_activity']) < $MAX_IDLE_TIME) {
            return true;
        }
        return false;
    }
    # ========================================================================
    # function logged_in_as
    # ========================================================================
    function logged_in_as () {
        if (is_logged_in() ) {
            return $_SESSION['username'];
        }
        else {
            return "";
        }
    }
    
    # ========================================================================
    # function save_login_info
    # ========================================================================
    function save_login_info ($user) {
                            $_SESSION['username'] = $user;
                    $_SESSION['last_activity'] = time();
    }
    
    # ========================================================================
    # function reset_login_info
    # ========================================================================
    function reset_login_info () {
                    $_SESSION['username'] = '';
                    $_SESSION['last_activity'] = 0;
        
    }

    # ========================================================================
    # get and set language
    # ========================================================================
    function get_set_language() {
        global $dictionary;
        $lang = 'English';
        $lang_temp = $lang;
        $languages = array_keys($dictionary);
        if (!empty($_SESSION['lang'])){
            $lang_temp = $_SESSION['lang'];
        }
        if (!empty($_GET['lang'])) {
            $lang_temp = $_GET['lang'];
        }
        if (in_array($lang_temp,$languages)){
            $lang = $lang_temp;
        }
        $_SESSION['lang']=$lang;
        return $lang;
    }

?>