<?php
/* Smarty version 3.1.33, created on 2018-12-13 18:03:18
  from '/home/ubuntu/workspace/development/asg9_10/View/Templates/chat.tpl' */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.33',
  'unifunc' => 'content_5c129ee6c95d43_31221009',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'f86eca363b47d9ab2212667eb2870deda1a3dedc' => 
    array (
      0 => '/home/ubuntu/workspace/development/asg9_10/View/Templates/chat.tpl',
      1 => 1544680602,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_5c129ee6c95d43_31221009 (Smarty_Internal_Template $_smarty_tpl) {
?><div class="chatroom">
    <div class="toolbar">
       <button class="dark_mode" onclick="changeTheme()"><i class="fa fa-paint-brush"><br><?php echo $_smarty_tpl->tpl_vars['THEME']->value;?>
</i></button>
        <input type=file class="avatar" id="choose_avatar" accept="image/jpg, image/jpeg, image/JPG" onchange="saveImage(this)"/>
        <button class="avatar" onclick="chooseAvatar()"><i class="fa fa-user"><br><?php echo $_smarty_tpl->tpl_vars['AVATAR']->value;?>
</i></button>
    </div>
    <div class="messages" id="messagediv">

    </div>
    <div class="sendbar">
        <input type="text" id="sendtxt">
        
        <button id="sendbtn" onclick="sendMessage()"><i class="fa fa-share"> <?php echo $_smarty_tpl->tpl_vars['SND_TEXT']->value;?>
</i></button>
    </div>
</div><?php }
}
