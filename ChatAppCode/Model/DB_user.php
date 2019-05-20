<?php
# Kennedy Ballard
# 1649618

error_reporting(E_ALL);
ini_set('display_errors','On');

    # =========================================================================
    # connect
    # =========================================================================
    # purpose: Connect to the requested database
    # inputs: database name (defaults to "chat")
    # outputs: mysqli object (database handle)
    # errors: Throws an exception if there is a database error
    # ---------------------------------------------------------------------------
    function connect($database="chat"){
        # Set database error reporting --> throws exception if database error
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
        
        # Connection info
        $server=getenv("IP");
        $username=getenv("C9_USER");
        $password="";
        $dbport="3306";
        
        # Connect to database, throw exception if issue
        $dbh=new mysqli($server, $username, $password, $database, $dbport);
        return $dbh;
    }
    
    # ===========================================================================
    # validate_user
    # ===========================================================================
    # purpose: validates the username/password combination of an active user
    # inputs: database handle
    # user name (case insensitive)
    # user password (case sensitive)
    # Note:
    # passwords are sent in as plain text, but compared to encrypted
    # passwords stored in database using php password_verify()
    #
    # returns: true if the credentials are correct, false otherwise
    # errors: Throws an exception _only_ if there is a database error
    # ---------------------------------------------------------------------------
    function validate_user($dbh, $user, $pass){
        if(!user_exists($dbh, $user))
            return false;
            
        # Select password for provided username & must be active user    
        $sql = "select password from users where username = ?";

        # bind and prepare
        $sth = $dbh->prepare($sql);
        $sth->bind_param("s", $user);
        $sth->bind_result($db_encryptedPass);

        $sth->execute();
        
        # Get encrypted password
        if ($sth->fetch()){
            # Compare against provided password --> true if match, otherwise false
            return password_verify($pass, $db_encryptedPass);
        }
        
        # Entry not found
        return false;
    }
    
    
    # ===========================================================================
    # create_user
    # ===========================================================================
    # purpose: creates a new user IF that user does not already exist,
    # username must be unique
    # inputs: database handle
    # new user name (case insensitive, non-empty string)
    # new user password (case sensitive, non-empty string)
    # new user email (case insensitive)
    #
    # Note:
    # passwords are sent in as plain text, encrypted using php
    # password_hash() function, using the PASSWORD_DEFAULT flag
    # for the type of encryption
    # The encrypted password is saved in the database
    #
    # returns:
    # true if the user was created,
    # false if username already exists
    # errors: Throws an exception if there is a database error
    # Throws an exception if the username and/or password is an empty string
    # ---------------------------------------------------------------------------
    function create_user($dbh, $user, $pass, $email){
        # Throw exception if no username or no password
        if(empty($user) || empty($pass))
            throw new exception("Must have username and password");
        
        # Check if user exists already/is active
        if(user_exists($dbh, $user)){
            return false;
        }
        
        # All good to create new user
        # flag set to one --> 0 is deactivated
        $encryptedPass = password_hash($pass, PASSWORD_DEFAULT);
        $sql = "insert into users (username, email, password) 
                values (?, ?, ?)";
        
        # Prepare and bind params
        $sth = $dbh->prepare($sql);
        $sth->bind_param("sss", $user,$email,$encryptedPass);
        
        # Create user
        $sth->execute();
        
        return true;
    }
    
    # =========================================================================
    # user_exists
    # =========================================================================
    # purpose: does the user exist in the database and is an active account
    # inputs: database handle
    # user name (case insensitive)
    # returns: true or false
    # errors: Throws an exception if there is a database error
    # -------------------------------------------------------------------------
    function user_exists($dbh, $username){
        # Select password for provided username    
        $sql = "select 1 from users where username = ?";

        # bind and prepare
        $sth = $dbh->prepare($sql);
        $sth->bind_param("s", $username);
        $sth->bind_result($flag);

        $sth->execute();
        
        # return true if there is a record & is active (0 is not active)
        # --> otherwise return false
        if ($sth->fetch()) 
                return ($flag != 0);
        return false;
    }
    
    # =========================================================================
    # deactivate_user
    # =========================================================================
    # purpose: removes user from list of valid users
    # - username will be available for new users
    # inputs: database handle
    # user name (case insensitive)
    # returns: true if there is no active account for user
    # true if user’s account is deactivated
    # false otherwise
    # errors: Throws an exception _only_ if there is a database error
    # -------------------------------------------------------------------------
    function deactivate_user($dbh, $username){
        # Check that user exists
        # If not --> return true (No active user for this username)
        if(!user_exists($dbh, $username))
            return true;
            
        # User is active --> deactivate
        $sql = "delete from users
                where username=?";
        
        # Prepare and bind
        $sth = $dbh->prepare($sql);
        $sth->bind_param("s", $username);

        $sth->execute();
        $sth->close();
        
        return true;
    }
    
    #--------------------------------------------------------------------------
    # CHECK PASSWORD
    # --> Verifies that username and password match
    # --> Does not check for deactivation like Login
    #--------------------------------------------------------------------------
    function check_pass($dbh, $user, $pass){
        # Select password for provided username    
        $sql = "select password from users where username = ?";

        # bind and prepare
        $sth = $dbh->prepare($sql);
        $sth->bind_param("s", $user);
        $sth->bind_result($db_encryptedPass);

        $sth->execute();
        
        # Get encrypted password
        if ($sth->fetch()){
            # Compare against provided password --> true if match, otherwise false
            return password_verify($pass, $db_encryptedPass);
        }
        
        # Entry not found
        return false;
    }
?>