package com.macjiji.marcus.shoppinglistjava.objects;

/**
 *
 * @author Marcus
 * @see com.macjiji.marcus.shoppinglistjava.MainActivity
 *
 * Classe permettant de creer une liste de courses
 *
 */

public class ShoppingList {

    private int id;
    private String name;
    private int red;
    private int green;
    private int blue;

    /**
     * Constructeur par defaut
     */
    public ShoppingList(){ }

    /**
     * Constructeur prenant en parametres tous les attributs d'une liste de courses
     * @param id L'identifiant de la liste de courses
     * @param name le nom de la liste de courses
     * @param red Teinte rouge de la liste de courses
     * @param green Teinte verte de la liste de courses
     * @param blue Teinte bleue de la liste de courses
     */
    public ShoppingList(int id, String name, int red, int green, int blue){
        this.id = id;
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    // GETTER
    public int getId() { return id; }
    public String getName() { return name; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }

    // SETTER
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRed(int red) { this.red = red; }
    public void setGreen(int green) { this.green = green; }
    public void setBlue(int blue) { this.blue = blue; }

    // Méthode toString()

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

}
