package com.macjiji.marcus.shoppinglistjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.macjiji.marcus.shoppinglistjava.adapters.ExpandableListAdapter;
import com.macjiji.marcus.shoppinglistjava.db.Database;
import com.macjiji.marcus.shoppinglistjava.objects.Category;
import com.macjiji.marcus.shoppinglistjava.objects.Item;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Marcus
 * @version 1.0
 *
 * Classe permettant de gerer la vue d'ajout d'un item a une liste de course
 *
 */

public class AjouterItemActivity extends AppCompatActivity {

    // Attributs pour les composants graphique et la BDD
    protected ExpandableListView listArticles;
    protected ExpandableListAdapter listAdapter;
    protected Database baseDeDonnees;

    // Attributs contenant l'ensemble des données de la BDD
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    // Attributs permettant de gérer la liste de courses
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    /**
     * Methode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_item);

        initialiseBaseDeDonnees();
        initialiseListArticles();

    }

    /**
     * Methode permettant d'initialiser la base de donnees
     */
    private void initialiseBaseDeDonnees(){
        baseDeDonnees = new Database(this);
        baseDeDonnees.open();
        categories = baseDeDonnees.getAllCategories();
        items = baseDeDonnees.getAllItems();
    }

    /**
     * Methode permettant d'initialiser la liste des articles
     */
    private void initialiseListArticles(){
        listArticles = (ExpandableListView)findViewById(R.id.liste_items);
        prepareListData(); // On prépare la liste des données
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild); // On ajoute ces données à l'adpater
        listArticles.setAdapter(listAdapter); // On rattache l'adapter à la vue ExpandableView
        listArticles.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) { // Et on oublie pas le listener pour gérer le clic sur les différents items
                for(Item item : items){
                    if(item.getName_fr().equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                        baseDeDonnees.createShoppingListItem(new ShoppingListItem(0, getIntent().getIntExtra("id", -1), item.getIdCat(), item.getIdItem())); // On crée le lien entre la liste de courses et l'item sélectionné
                        Intent retourListe = new Intent(AjouterItemActivity.this, ApercuListeActivity.class); // Pour on redirige vers l'aperçu de la liste de courses
                        retourListe.putExtra("id", getIntent().getIntExtra("id", -1));
                        startActivity(retourListe);
                    }
                }
                return false;
            }
        });
    }

    /**
     * Methode permettant de preparer la liste des donnees a afficher dans les propositions.
     *      -> Ici, on a juste besoin de toutes les afficher avec tous leurs items.
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for(Category category : categories){ // Pour chaque categorie
            listDataHeader.add(category.getNameFr()); // On ajouter le nom de la catégorie
            ArrayList<String> listItemsIntoCategory = new ArrayList<>(); // On initialise une liste qui va nous permettra d'afficher tous les items d'une catégorie
            for(Item item : items){ // Et pour chaque items
                if(category.getIdCat() == item.getIdCat()){ // On teste si l'identifiant de la catégorie correspond à la catégorie d'un items
                    listItemsIntoCategory.add(item.getName_fr()); // Le cas échéant, on l'ajoute
                }
            }
            listDataChild.put(category.getNameFr(), listItemsIntoCategory); // On ajoute enfin l'ensemble de nos données
        }

    }

}
