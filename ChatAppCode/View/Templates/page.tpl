<!DOCTYPE html>
<html>
    <head>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
        <script src="Control/clientjs.js"></script>
        <script src="Control/messagesjs.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="./View/gridstyle.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title></title>
    </head>
    <body>
        
        <div class="site">
            
            <div class="header">
                {include file="$header_div"}
            </div>
            
            <div class="main">
                {include file="$main_div"}
            </div>
            
            <div class="sidebar">
                {include file="$sidebar_div"}
            </div>
            
            <div class="footer">
                {include file="$footer_div"}
            </div>
        </div>
    </body>
</html>