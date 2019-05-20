<?php
/* Smarty version 3.1.33, created on 2018-12-13 18:02:31
  from '/home/ubuntu/workspace/development/asg9_10/View/Templates/sidebar.tpl' */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.33',
  'unifunc' => 'content_5c129eb7be0180_36238346',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'b93d499f95362401c5750a64479fd95b84a11763' => 
    array (
      0 => '/home/ubuntu/workspace/development/asg9_10/View/Templates/sidebar.tpl',
      1 => 1544679716,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_5c129eb7be0180_36238346 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->_checkPlugins(array(0=>array('file'=>'/home/ubuntu/workspace/smarty/libs/plugins/function.html_options.php','function'=>'smarty_function_html_options',),));
?>
<div class="disconnect">
    <?php if ($_smarty_tpl->tpl_vars['user']->value != '') {?>
         <h4 class="logged_in_as"><?php echo $_smarty_tpl->tpl_vars['LOG_MSG']->value;?>
: <div id="log_user"><?php echo $_smarty_tpl->tpl_vars['user']->value;?>
</div></h4>
    <?php }?>

</div>
            
<div class="disconnBtn">
    <br>
    <?php if ($_smarty_tpl->tpl_vars['user']->value != '') {?>
        <button type="button" id="logoffbtn" onclick="logoff()"><i class="fa fa-sign-out"> <?php echo $_smarty_tpl->tpl_vars['LOGOFF_MSG']->value;?>
</i></button>
    <?php }?>
</div>
            
<div class="language">
    <h3><?php echo $_smarty_tpl->tpl_vars['LANG_MSG']->value;?>
</h3>
</div>
            
<div class="langSelect" id="languageSelect">
    <br>
    <?php echo smarty_function_html_options(array('name'=>'lang','id'=>'language','onchange'=>"location = 'main.php?lang=' + this.value",'options'=>$_smarty_tpl->tpl_vars['languages']->value,'selected'=>$_smarty_tpl->tpl_vars['lang']->value),$_smarty_tpl);?>

</div><?php }
}
