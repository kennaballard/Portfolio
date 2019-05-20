<?php
/* Smarty version 3.1.33, created on 2018-10-01 20:16:10
  from '/home/ubuntu/workspace/development/asg3/View/main.tpl' */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.33',
  'unifunc' => 'content_5bb2808a467752_76232449',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '79201403e6a04c9d5942678a275c41d22df171de' => 
    array (
      0 => '/home/ubuntu/workspace/development/asg3/View/main.tpl',
      1 => 1538424968,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_5bb2808a467752_76232449 (Smarty_Internal_Template $_smarty_tpl) {
?><html>
    <head>
        <link rel="stylesheet" type="text/css" href="View/gridstyle.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title></title>
    </head>
    <body>
        <div class="header">
            <h1>CompSci Chat Room <?php echo $_smarty_tpl->tpl_vars['headText']->value;?>
</h1>
        </div>
        
        <div class="main">
            <h3><?php echo $_smarty_tpl->tpl_vars['title']->value;?>
</h3>
            </br>
            <p><?php echo $_smarty_tpl->tpl_vars['message']->value;?>
: </p> 
            </br>
            <?php echo $_smarty_tpl->tpl_vars['user']->value;?>
: <input type="text" id="user">
            </br></br>
            <?php echo $_smarty_tpl->tpl_vars['pass']->value;?>
: <input type="text" id="pass">
            </br></br>
            <button type="button"><?php echo $_smarty_tpl->tpl_vars['title']->value;?>
</button>
            <button type="button"><?php echo $_smarty_tpl->tpl_vars['new']->value;?>
</button>
        </div>
        
        <div class="sidebar">
            <p><?php echo $_smarty_tpl->tpl_vars['message']->value;?>
</p>
            </br>
            <button type="button"><?php echo $_smarty_tpl->tpl_vars['logOff']->value;?>
</button>
        </div>
    </body>
</html><?php }
}
