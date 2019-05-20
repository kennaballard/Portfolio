<div class="chatroom">
    <div class="toolbar">
       <button class="dark_mode" onclick="changeTheme()"><i class="fa fa-paint-brush"><br>{$THEME}</i></button>
        <input type=file class="avatar" id="choose_avatar" accept="image/jpg, image/jpeg, image/JPG" onchange="saveImage(this)"/>
        <button class="avatar" onclick="chooseAvatar()"><i class="fa fa-user"><br>{$AVATAR}</i></button>
    </div>
    <div class="messages" id="messagediv">

    </div>
    <div class="sendbar">
        <input type="text" id="sendtxt">
        
        <button id="sendbtn" onclick="sendMessage()"><i class="fa fa-share"> {$SND_TEXT}</i></button>
    </div>
</div>