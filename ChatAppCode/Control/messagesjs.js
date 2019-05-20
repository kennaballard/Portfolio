var LOGGED_IN;
var JSON_ERR = "INVALID JSON";
var USER;
var DARK_MODE;

window.onload = function(){
    $.getScript("Control/clientjs.js", function(){
        // For Login page
        userSetup();
    });
    
    // For logged in page
    messageSetup();
    USER = $("#log_user").text();
    
    // Default theme
    DARK_MODE=false;
    
    // Set a timer to check for new messages every second
    setInterval(getMessagesTimer, 1000);
}

/*****************************************
 * MESSAGE SETUP
 * Gets all messages on window load
 * Sends ajax request, calls 
 * processGetMessages
 *****************************************/
function messageSetup(){
    // get all the messages when a user logs in
    var data = {};
    data.action = "get_all_messages";
    data.database = "chattest";
    data.tz_off = (new Date().getTimezoneOffset());
    
     // Send request
    $.post("Control/messages.php", data, processGetMessages);
}

/*****************************************
 * GET MESSAGES TIMER
 * Gets new messages on timer
 * Sends ajax request, calls 
 * processGetMessages
 *****************************************/
function getMessagesTimer(){
    // Setup object for request
    var data = {};
    data.action = "get_messages";
    data.database = "chattest";
    data.tz_off = (new Date().getTimezoneOffset());
 
    // Send request
    $.post("Control/messages.php", data, processGetMessages);
}

/*****************************************
 * PROCESS GET MESSAGES
 * Parses the returned json
 * If no errors, display all the new
 * messages
 *****************************************/
function processGetMessages($data, $status){
    var scroll = isAtBottom();
    
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert($data);
        return;
    }
    var errNum = results.error_number;
    
    // Display message fails if any error is returned
    if(errNum != 0){
        if(errNum != 6)
            alert(errNum + ": " + results.error_string);
        return;
    }
    
    var messages = results.result;
    
    // If no messages, nothing to do
    if(messages == null)
        return;
        
    $.each(messages, displayMessages);
    updateScroll(scroll);
}

/*****************************************
 * DISPLAY MESSAGES
 * Takes a message object
 * Appends the message in the chatroom div
 *****************************************/
function displayMessages(key, value){
    // Determine if message belongs to logged in user
    var floatValue = value.username == USER ? "right" : "left";
    
    // format any image or text links in the message
    value.content = checkForLinks(value.content);
    
    // Display content
    var messageFormat = `<div class='message' style="float:` + floatValue + `">
                            <div class='messageHeader'>`;
                            
    // If user has no avatar, set avatar img src to nothing
    var imgTag = value.avatar == null ? `<img src='' id="avatar"/> `: `<img src=` + value.avatar + ` id="avatar"/> `;
                    
    messageFormat += imgTag                
                    + value.username 
                    + " ---------- " + value.time 
                    + ` </div> 
                        <br> 
                        <div class='messageContent'>` + value.content 
                    + ` </div>
                    </div>
                    <br>`;
    
    // Add the newly formatted message to the chatroom                    
    $(".messages").append(messageFormat);
}

/*****************************************
 * SEND MESSAGE
 * Takes message and user
 * Sends ajax request, calls 
 * processSendMessage
 *****************************************/
function sendMessage(){
    var msg = $("#sendtxt").val();
    
    // Do not let user send message that is empty
    if(!msg)
        return;
        
    var data = {};
    data.database = "chattest";
    data.action = "save_message";
    data.content = $('<div />').text(msg).html()
    data.username = USER;
    
    // Send request
    $.post("Control/messages.php", data, processSendMessage);
}

/*****************************************
 * PROCESS SEND MESSAGE
 * Parses return json
 * Verifies no error, empties message 
 * text box after message has been sent
 *****************************************/
function processSendMessage($data, $status){
    var scroll = isAtBottom();
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert($data);
        return;
    }
    
    if(results.error_number != 0){
        alert(results.error_string);
        return;
    }
        
    if(!results.result)
        alert("Error saving message");
        
    updateScroll(scroll);
    $("#sendtxt").val("");
}

/*****************************************
 * CHOOSE AVATAR
 * Triggers click event for file input
 * opens file selector
 *****************************************/
function chooseAvatar(){
    $("#choose_avatar").trigger('click');
}

/*****************************************
 * SAVE IMAGE
 * Get the image when a different one is 
 * chosen, save the image as user's avatar
 * sends ajax request containing image as blob,
 * calls processChooseAvatar
 ******************************************/
function saveImage(input){
    console.log(input);
    if (input.files && input.files[0]) {
        // Create a new filereader, get the selected image
        var reader = new FileReader();
        reader.readAsDataURL(input.files[0]);
            
        reader.onload = function (e) {
            // Make the ajax request to update the user's avatar
            var data = {};
            data.database = "chattest";
            data.action = "change_avatar";
            data.image = e.target.result;
            data.username = USER;
    
            // Send request
            $.post("Control/messages.php", data, processChooseAvatar);
        }
    }
}

/*****************************************
 * PROCESS CHOOSE AVATAR
 * opens file selector
 * If a file is chosen, sends ajax request,
 * calls processChooseAvatar
 *****************************************/
function processChooseAvatar($data, $status){
    try{
         var results = JSON.parse($data);
    }
    catch(err){
        alert($data);
        return;
    }
    var errNum = results.error_number;
    if(errNum != 0){
        if(errNum != 6)
            alert(errNum + ": " + results.error_string);
        return;
    }
    
    // Reload the page to see updated avatar
    location.reload();
}
/*****************************************
 * UPDATE SCROLL 
 * If the scroll is at the bottom, 
 * update the scroll to stay at the bottom
 * when a new message is received
 *****************************************/
function updateScroll(isScrolledToBottom){
    if(isScrolledToBottom){
        // Update the scroll 
        var messageDiv = document.getElementById("messagediv");
        messageDiv.scrollTop = messageDiv.scrollHeight - messageDiv.clientHeight;
    }
}

function isAtBottom(){
    var messageDiv = document.getElementById("messagediv");
    return messageDiv != null ? (messageDiv.scrollHeight - messageDiv.clientHeight) <= (messageDiv.scrollTop + 1) : false;
}


/*****************************************
 * FORMAT LINKS
 * Display links in messages as actual links
 * If an image link is displayed, display
 * the image
 *****************************************/
// For links
function checkForLinks(text){
    text = replaceUrlWithImage(text);
    text = replaceUrlWithHtml(text);
    return text;
}

function replaceUrlWithHtml(text) {
    var exp = /(\b(?<!<img src=')(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    return text.replace(exp,"<a href='$1'>$1</a>");
}

// For image links
function replaceUrlWithImage(text){
    var exp = /\b((http(s?):)([/|.|\S|-])*\.(jpg|gif|png))/ig;
    return text.replace(exp,"<img src='$1'/>")
}


// Switch the theme between a dark and light background
function changeTheme(){
    if(DARK_MODE)
        $("body").css("background-color", "#DCDCDC");
    else
        $("body").css("background-color", "#2e324c");
        
    DARK_MODE = !DARK_MODE;
}