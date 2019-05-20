<?php
$dir = __DIR__;
require("$dir/../Control/errors.php");
$EN = "English";
$FR = "Francais";


$dictionary = array($EN=>array(
                                    "LGIN"=>"Login",
                                    "LANG"=>$EN,
                                    "NOT_LOGGED"=>"Currently not logged in",
                                    "LOG_MSG"=>"Currently logged in as",
                                    "NEW_MSG"=>"Create New User",
                                    "LOGOFF_MSG"=>"Log off",
                                    "LANG_MSG"=>"Select language",
                                    "USR"=>"Username",
                                    "PASS"=>"Password",
                                    "EMAIL"=>"Email",
                                    "DEACT"=>"Deactivate User",
                                    "USR_PASS_MSG"=>"Username or password is incorrect",
                                    "USR_TAKEN_MSG"=>"Username is not available",
                                    "EM_ND_MSG"=>"Enter an email address",
                                    "USR_ND_MSG"=>"Must enter a username",
                                    "PASS_ND_MSG"=>"Must enter a password",
                                    "USR_PASS_ENTERED"=>"Username and password must be entered!",
                                    "USR_PASS_EM_ENTERED"=>"Username, password and email must be entered to create a new user!",
                                    "USR_CREATED"=>"New user was successfully created",
                                    "USR_DEACT"=>"User was deactivated",
                                    "SND_TEXT"=>"Send",
                                    "AVATAR"=>"Edit avatar",
                                    "THEME"=>"Change theme"),
                    $FR=>array(
                                    "LGIN"=>"Connexion",
                                    "LANG"=>$FR,
                                    "NOT_LOGGED"=>htmlentities("Aucune utilisateur actuellement connecté"),
                                    "LOG_MSG"=>htmlentities("Utilisateur actuellement connecté"),
                                    "NEW_MSG"=>htmlentities("Nouvel utilisateur"),
                                    "LOGOFF_MSG"=>htmlentities("Se déconnecter"),
                                    "LANG_MSG"=>htmlentities("sélectionner une langue"),
                                    "USR"=>htmlentities("Nom d'utilisateur"),
                                    "PASS"=>htmlentities("Mot de passe"),
                                    "EMAIL"=>"Courriel",
                                    "DEACT"=>htmlentities("Désactiver mon compte"),
                                    "USR_PASS_MSG"=>"Utilisateur ou mot de passe incorrect",
                                    "USR_TAKEN_MSG"=>htmlentities("Nom d'utilisateur déjà pris"),
                                    "EM_ND_MSG"=>"Il faut fournir un nom de courriel",
                                    "USR_ND_MSG"=>"Il faut saisir le nom d'utilisateur",
                                    "PASS_ND_MSG"=>"Il faut saisir le mot de passe",
                                    "USR_PASS_ENTERED"=>"Il faut saisir le nom d'utilisateur et le mot de passe!",
                                    "USR_PASS_EM_ENTERED"=>htmlentities("Il faut saisir le nom d'utilisateur, le mot de passe et le courriel pour créer un compte!"),
                                    "USR_CREATED"=>htmlentities("Nouveau utilisateur créer"),
                                    "USR_DEACT"=>htmlentities("Utilisateur a été désactivé"),
                                    "SND_TEXT"=>"Envoyer",
                                    "AVATAR"=>"Modifier Avatar",
                                    "THEME"=>htmlentities("Change le thème"))
                                    );

$temp = array_keys($dictionary);
$languages = array();

foreach($dictionary as $key => $value){
    $languages[$key] = $key;
}

?>