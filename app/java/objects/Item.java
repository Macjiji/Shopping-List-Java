package com.macjiji.marcus.shoppinglistjava.objects;

/**
 *
 * @author Marcus
 * @see com.macjiji.marcus.shoppinglistjava.MainActivity
 *
 * Classe permettant de creer un item
 *
 */

public class Item {

    private int idItem; // Identifiant de l'item
    private int idCat; // Identifiant de la categorie
    private String name_en; // Nom en anglais de l'item
    private String name_fr; // Nom en français de l'item

    /**
     * Constructeur par defaut
     */
    public Item(){ }

    /**
     * Constructeur prenant en parametres tous les attributs d'un item
     * @param idItem L'identifiant de l'item
     * @param idCat L'identifiant de la categorie
     * @param name_en Le nom en anglais
     * @param name_fr Le nom en français
     */
    public Item(int idItem, int idCat, String name_en, String name_fr){
        this.idItem = idItem;
        this.idCat = idCat;
        this.name_en = name_en;
        this.name_fr = name_fr;
    }

    // GETTER
    public int getIdItem(){ return this.idItem; }
    public int getIdCat(){ return this.idCat; }
    public String getName_en(){ return this.name_en; }
    public String getName_fr(){ return this.name_fr; }

    // SETTER
    public void setIdItem(int idItem){ this.idItem = idItem; }
    public void setIdCat(int idCat){ this.idCat = idCat; }
    public void setName_en(String name_en){ this.name_en = name_en; }
    public void setName_fr(String name_fr){ this.name_fr = name_fr; }

    // Méthode toString()
    @Override
    public String toString() {
        return "Item{" +
                "idItem=" + idItem +
                ", idCat=" + idCat +
                ", name_en='" + name_en + '\'' +
                ", name_fr='" + name_fr + '\'' +
                '}';
    }

    // Méthode equals : Permet de tester notamment si un item existe déjà dans une ArrayList
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (idItem != item.idItem) return false;
        if (idCat != item.idCat) return false;
        if (name_en != null ? !name_en.equals(item.name_en) : item.name_en != null) return false;
        return name_fr != null ? name_fr.equals(item.name_fr) : item.name_fr == null;

    }

}
