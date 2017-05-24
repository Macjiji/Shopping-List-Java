package com.macjiji.marcus.shoppinglistjava.objects;

/**
 * Created by Marcus on 09/05/2017.
 */

public class ShoppingListItem {

    private int id;
    private int idList;
    private int idCat;
    private int idItem;

    /**
     * Constructeur par defaut
     */
    public ShoppingListItem(){ }

    /**
     * Constructeur prenant en parametre tous les attributs d'un item d'une liste de courses
     * @param id Identifiant de l'item de la liste de courses
     * @param idList Identifiant de la liste de courses
     * @param idCat Identifiant de la categorie
     * @param idItem Identifiant de l'item
     */
    public ShoppingListItem(int id, int idList, int idCat, int idItem){
        this.id = id;
        this.idList = idList;
        this.idCat = idCat;
        this.idItem = idItem;
    }

    // GETTER
    public int getId() { return id; }
    public int getIdList() { return idList; }
    public int getIdCat() { return idCat; }
    public int getIdItem() { return idItem; }

    // SETTER
    public void setId(int id) { this.id = id; }
    public void setIdList(int idList) { this.idList = idList; }
    public void setIdCat(int idCat) { this.idCat = idCat; }
    public void setIdItem(int idItem) { this.idItem = idItem; }


    // Methode toString()
    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "id=" + id +
                ", idList=" + idList +
                ", idCat=" + idCat +
                ", idItem=" + idItem +
                '}';
    }
}
