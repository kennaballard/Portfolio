<div class="login">
    <div class="innerText">
        <h2>{$LGIN}</h2>
        </br>
    </div>
            
    <div class="textboxes" class="loginInfo">
                
        <p class="err" id="usrPassNDErr">{$USR_PASS_ENTERED}</p>
        <p class="err" id="usrPassEmailNDErr">{$USR_PASS_EM_ENTERED}</p>
        <p class="err" id="1">{$USR_TAKEN_MSG}</p>
        <p class="err" id="usrPassErr">{$USR_PASS_MSG}</p>
        <p class="err" id="enterEmail">{$EM_ND_MSG}</p>
                
        <p class="success" id="newUser">{$USR_CREATED}</p>
        <p class="success" id="deactivated">{$USR_DEACT}</p>
                    
        <p>{$USR}:</p> 
        <input type="text" id="usertxt">
        <p>{$PASS}:</p> 
        <input type="password" id="passtxt">
        <p id="emaillabel">{$EMAIL}:</p> 
        <input type="text" id="emailtxt">
    </div>
            
    <div class="btn1" class="loginInfo">
            <button type="button" id="loginbtn" onclick="login()"><i class="fa fa-sign-in"> {$LGIN} </i></button>
    </div>
            
    <div class="btn2" class="loginInfo">
        <button type="button" id="createbtn" onclick="createNew()"><i class="fa fa-plus"> {$NEW_MSG}</i></button>
    </div>
        
    <div class="btn3" class="loginInfo">
        <button type="button" id="deactbtn" onclick="deactivate()"> <i class="fa fa-ban"> {$DEACT} </i></button>
    </div>
</div>