<?php

	// Etape 0 : On créer une référence à la base de données
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	
	// Etape 1 : On crée un message d'erreur contenue dans le fichier JSON, par défaut à FALSE
	$response = array("error" => FALSE);
		
	// Etape 2 : On trouve les catégories présentes dans la base de données
	$categories = $db->getShoppingCategories();
	if($categories != NULL){
		$response["categories"] = $categories;
	} else {
		$response["categories"] = null;
		$response["error"] = TRUE;
		$response["error_msg"] = "Il n'y a pas de catégories !";
	}
	
	// Etape 3 : On trouve les items présents dans la base de données
	$items = $db->getShoppingItems();
	if($items != NULL){
		$response["items"] = $items;	
	} else {
		$response["items"] = null;
		$response["error"] = TRUE;
		$response["error_msg"] = "Il n'y a pas d'items !";
	}
			
	// Etape 4 : On renvoie la réponse à l'application !
	echo utf8_encode(json_encode($response));
	

	
?>