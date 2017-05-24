package com.macjiji.marcus.shoppinglistjava.objects;

/**
 *
 * @author Marcus
 * @see com.macjiji.marcus.shoppinglistjava.MainActivity
 *
 * Classe permettant de creer une categorie
 *
 */

public class Category {

    private int idCat; // Identifiant de la categorie
    private String nameEn; // Nom en anglais de la categorie
    private String nameFr; // Nom en français de la categorie

    /**
     * Constructeur par defaut
     */
    public Category(){ }

    /**
     * Constructeur prenant en parametres tous les attributs d'une categorie
     * @param idCat L'identifiant de la categorie
     * @param nameEn Le nom en anglais de la categorie
     * @param nameFr Le nom en français de la categorie
     */
    public Category(int idCat, String nameEn, String nameFr){
        this.idCat = idCat;
        this.nameEn = nameEn;
        this.nameFr = nameFr;
    }

    // GETTER
    public int getIdCat(){ return this.idCat; }
    public String getNameEn(){ return this.nameEn; }
    public String getNameFr(){ return this.nameFr; }

    // SETTER
    public void setIdCat(int idCat){ this.idCat = idCat; }
    public void setNameEn(String nameEn){ this.nameEn = nameEn; }
    public void setNameFr(String nameFr){ this.nameFr = nameFr; }

    // Méthode toString()
    @Override
    public String toString() {
        return "Category{" +
                "idCat=" + idCat +
                ", nameEn='" + nameEn + '\'' +
                ", nameFr='" + nameFr + '\'' +
                '}';
    }

    // Méthode equals : Permet de tester notamment si une catégorie existe déjà dans une ArrayList
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (idCat != category.idCat) return false;
        if (nameEn != null ? !nameEn.equals(category.nameEn) : category.nameEn != null)
            return false;
        return nameFr != null ? nameFr.equals(category.nameFr) : category.nameFr == null;

    }

}
