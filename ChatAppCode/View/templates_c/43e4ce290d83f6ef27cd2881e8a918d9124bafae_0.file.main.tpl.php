<?php
/* Smarty version 3.1.33, created on 2018-11-29 04:19:00
  from '/home/ubuntu/workspace/development/asg8/View/main.tpl' */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.33',
  'unifunc' => 'content_5bff68b42521c1_77956931',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '43e4ce290d83f6ef27cd2881e8a918d9124bafae' => 
    array (
      0 => '/home/ubuntu/workspace/development/asg8/View/main.tpl',
      1 => 1543430299,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_5bff68b42521c1_77956931 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->_checkPlugins(array(0=>array('file'=>'/home/ubuntu/workspace/smarty/libs/plugins/function.html_options.php','function'=>'smarty_function_html_options',),));
?>
<html>
    <head>
        <?php echo '<script'; ?>
 src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"><?php echo '</script'; ?>
>
        <?php echo '<script'; ?>
 src="./Control/clientjs.js"><?php echo '</script'; ?>
>
        <link rel="stylesheet" type="text/css" href="gridstyle.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title></title>
    </head>
    <div class="site">
    <body>
        <div class="header">
            <h1>CompSci Chat Room <?php echo $_smarty_tpl->tpl_vars['headText']->value;?>
</h1>
        </div>
        
        <div class="main">
            
            <div class="innerText">
                <h2><?php echo $_smarty_tpl->tpl_vars['title']->value;?>
</h2>
                </br>
                <p class="notLogged"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['NOT_LOGGED'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                
                <p class="logged_in_as" id="logged"><?php echo $_smarty_tpl->tpl_vars['message']->value;?>
: </p>
            </div>
            
            <div class="textboxes" class="loginInfo">
                
                <p class="err" id="usrPassNDErr"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_PASS_ENTERED'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                <p class="err" id="usrPassEmailNDErr"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_PASS_EM_ENTERED'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                <p class="err" id="1"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_TAKEN_MSG'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                <p class="err" id="usrPassErr"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_PASS_MSG'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                <p class="err" id="enterEmail"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['EM_ND_MSG'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                
                <p class="success" id="newUser"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_CREATED'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                <p class="success" id="deactivated"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['USR_DEACT'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</p>
                
                <p><?php echo $_smarty_tpl->tpl_vars['user']->value;?>
:</p> 
                <input type="text" id="usertxt">
                <p><?php echo $_smarty_tpl->tpl_vars['pass']->value;?>
:</p> 
                <input type="text" id="passtxt">
                <p id="emaillabel"><?php echo $_smarty_tpl->tpl_vars['email']->value;?>
:</p> 
                <input type="text" id="emailtxt">
            </div>
            
            <div class="btn1" class="loginInfo">
                <button type="button" id="loginbtn"><?php echo $_smarty_tpl->tpl_vars['title']->value;?>
</button>
            </div>
            
            <div class="btn2" class="loginInfo">
                <button type="button" id="createbtn"><?php echo $_smarty_tpl->tpl_vars['new']->value;?>
</button>
            </div>
            
            <div class="btn3" class="loginInfo">
                <button type="button" id="deactbtn"><?php echo $_smarty_tpl->tpl_vars['deactivate']->value;?>
</button>
            </div>
        </div>
        
        <div class="sidebar">
            <div class="disconnect">
                <h3><?php echo $_smarty_tpl->tpl_vars['not']->value;?>
</h3>
                <h3 class="notLogged"><?php echo mb_convert_encoding(htmlspecialchars($_smarty_tpl->tpl_vars['dictionary']->value[$_smarty_tpl->tpl_vars['lang']->value]['NOT_LOGGED'], ENT_QUOTES, 'UTF-8', true), "HTML-ENTITIES", 'UTF-8');?>
</h3>
                <h3 class="logged_in_as"><?php echo $_smarty_tpl->tpl_vars['message']->value;?>
: </h3>
            </div>
            
            <div class="disconnBtn">
                <br>
                <button type="button" id="logoffbtn"><?php echo $_smarty_tpl->tpl_vars['logOff']->value;?>
</button>
            </div>
            
            <div class="language">
                <h3><?php echo $_smarty_tpl->tpl_vars['langMsg']->value;?>
</h3>
            </div>
            
            <div class="langSelect" id="languageSelect">
                <br>
                <?php echo smarty_function_html_options(array('name'=>'lang','id'=>'language','onchange'=>"location = 'main.php?lang=' + this.value",'options'=>$_smarty_tpl->tpl_vars['language']->value,'selected'=>$_smarty_tpl->tpl_vars['lang']->value),$_smarty_tpl);?>

            </div>
            
        </div>
        
        <div class="footer">
            <p><small>2018 &#169 Kennedy Ballard</small></p>
        </div>
        
    </body>
    </div>
</html><?php }
}
