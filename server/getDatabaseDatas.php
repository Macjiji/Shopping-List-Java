<?php

	// Etape 0 : On cr�er une r�f�rence � la base de donn�es
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	
	// Etape 1 : On cr�e un message d'erreur contenue dans le fichier JSON, par d�faut � FALSE
	$response = array("error" => FALSE);
		
	// Etape 2 : On trouve les cat�gories pr�sentes dans la base de donn�es
	$categories = $db->getShoppingCategories();
	if($categories != NULL){
		$response["categories"] = $categories;
	} else {
		$response["categories"] = null;
		$response["error"] = TRUE;
		$response["error_msg"] = "Il n'y a pas de cat�gories !";
	}
	
	// Etape 3 : On trouve les items pr�sents dans la base de donn�es
	$items = $db->getShoppingItems();
	if($items != NULL){
		$response["items"] = $items;	
	} else {
		$response["items"] = null;
		$response["error"] = TRUE;
		$response["error_msg"] = "Il n'y a pas d'items !";
	}
			
	// Etape 4 : On renvoie la r�ponse � l'application !
	echo utf8_encode(json_encode($response));
	

	
?>