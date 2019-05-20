<?php
/* Smarty version 3.1.33, created on 2018-12-13 05:45:18
  from '/home/ubuntu/workspace/development/asg8/View/Templates/login.tpl' */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.33',
  'unifunc' => 'content_5c11f1ee8770a0_02463155',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '16ec4693fff6ef4833c6c62bb1eab5f0e0508a09' => 
    array (
      0 => '/home/ubuntu/workspace/development/asg8/View/Templates/login.tpl',
      1 => 1544679915,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_5c11f1ee8770a0_02463155 (Smarty_Internal_Template $_smarty_tpl) {
?><div class="login">
    <div class="innerText">
        <h2><?php echo $_smarty_tpl->tpl_vars['LGIN']->value;?>
</h2>
        </br>
    </div>
            
    <div class="textboxes" class="loginInfo">
                
        <p class="err" id="usrPassNDErr"><?php echo $_smarty_tpl->tpl_vars['USR_PASS_ENTERED']->value;?>
</p>
        <p class="err" id="usrPassEmailNDErr"><?php echo $_smarty_tpl->tpl_vars['USR_PASS_EM_ENTERED']->value;?>
</p>
        <p class="err" id="1"><?php echo $_smarty_tpl->tpl_vars['USR_TAKEN_MSG']->value;?>
</p>
        <p class="err" id="usrPassErr"><?php echo $_smarty_tpl->tpl_vars['USR_PASS_MSG']->value;?>
</p>
        <p class="err" id="enterEmail"><?php echo $_smarty_tpl->tpl_vars['EM_ND_MSG']->value;?>
</p>
                
        <p class="success" id="newUser"><?php echo $_smarty_tpl->tpl_vars['USR_CREATED']->value;?>
</p>
        <p class="success" id="deactivated"><?php echo $_smarty_tpl->tpl_vars['USR_DEACT']->value;?>
</p>
                    
        <p><?php echo $_smarty_tpl->tpl_vars['USR']->value;?>
:</p> 
        <input type="text" id="usertxt">
        <p><?php echo $_smarty_tpl->tpl_vars['PASS']->value;?>
:</p> 
        <input type="password" id="passtxt">
        <p id="emaillabel"><?php echo $_smarty_tpl->tpl_vars['EMAIL']->value;?>
:</p> 
        <input type="text" id="emailtxt">
    </div>
            
    <div class="btn1" class="loginInfo">
            <button type="button" id="loginbtn" onclick="login()"><i class="fa fa-sign-in"> <?php echo $_smarty_tpl->tpl_vars['LGIN']->value;?>
 </i></button>
    </div>
            
    <div class="btn2" class="loginInfo">
        <button type="button" id="createbtn" onclick="createNew()"><i class="fa fa-plus"> <?php echo $_smarty_tpl->tpl_vars['NEW_MSG']->value;?>
</i></button>
    </div>
        
    <div class="btn3" class="loginInfo">
        <button type="button" id="deactbtn" onclick="deactivate()"> <i class="fa fa-ban"> <?php echo $_smarty_tpl->tpl_vars['DEACT']->value;?>
 </i></button>
    </div>
</div><?php }
}
