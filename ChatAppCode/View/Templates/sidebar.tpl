<div class="disconnect">
    {if $user != ""}
         <h4 class="logged_in_as">{$LOG_MSG}: <div id="log_user">{$user}</div></h4>
    {/if}

</div>
            
<div class="disconnBtn">
    <br>
    {if $user != ""}
        <button type="button" id="logoffbtn" onclick="logoff()"><i class="fa fa-sign-out"> {$LOGOFF_MSG}</i></button>
    {/if}
</div>
            
<div class="language">
    <h3>{$LANG_MSG}</h3>
</div>
            
<div class="langSelect" id="languageSelect">
    <br>
    {html_options name=lang id=language onchange="location = 'main.php?lang=' + this.value" options=$languages selected=$lang}
</div>