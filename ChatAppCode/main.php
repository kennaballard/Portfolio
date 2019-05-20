<?php
    $dir = __DIR__;
    $workspace = "$dir";
    session_start();

    include_once('/home/ubuntu/workspace/smarty/libs/Smarty.class.php');
    include_once("$workspace/View/dictionary.php");
    include_once("$workspace/Control/useful_functions.php");
    //include_once("$dir/Control/useful_functions.php");

    # Language setup
    $lang = get_set_language();
    global $dictionary;
    
    
    
    # Setup Smarty
    $smarty = new Smarty;
    
    $smarty->assign($dictionary[$lang]);
    $smarty->assign('lang',htmlentities($lang));
    $smarty->assign("languages",$languages);
    
    $smarty->assign('header_div',"$workspace/View/Templates/header.tpl");
    $smarty->assign('sidebar_div',"$workspace/View/Templates/sidebar.tpl");
    
    if (is_logged_in()) {
       $smarty->assign('main_div',"$workspace/View/Templates/chat.tpl");
       $smarty->assign('user', $_SESSION['username']);
    }
    else {
        $smarty->assign('main_div',"$workspace/View/Templates/login.tpl");
        $smarty->assign('user', "");
    } 
   
    $smarty->assign('footer_div',"$workspace/View/Templates/footer.tpl");
    
    $smarty->display("$workspace/View/Templates/page.tpl");

?>