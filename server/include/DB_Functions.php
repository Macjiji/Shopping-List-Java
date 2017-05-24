<?php
 
 
class DB_Functions {
 
    private $conn; // Changer en private lorsque tous les tests seront faits
 
    // Constructeur
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // Destructeur
    function __destruct() {
         
    }
    
    // M�thode permettant de r�cup�rer les cat�gories dans la base de donn�es
    public function getShoppingCategories(){
    	$stmt = $this->conn->prepare("SELECT * FROM categories");
    	if ($stmt->execute()) {   		
    		$results = $stmt->get_result();
    		$i = 0;
    		$categories = [];
    		while ($row = $results->fetch_assoc()) {
    			$categories[$i] = $row;
    			$i++;
    		}
    		$stmt->close();
    		return $categories;
    	} else {
    		$stmt->close();
    		return NULL;
    	}
    }
    
    // M�thode permettant de r�cup�rer les items dans la base de donn�es
    public function getShoppingItems(){
    	$stmt = $this->conn->prepare("SELECT * FROM items");
    	if ($stmt->execute()) {
    		$results = $stmt->get_result();
    		$i = 0;
    		$items = [];
    		while ($row = $results->fetch_assoc()) {
    			$items[$i] = $row;
    			$i++;
    		}
    		$stmt->close();
    		return $items;
    	} else {
    		$stmt->close();
    		return NULL;
    	}
    }
    
 
}
 
?>