package com.macjiji.marcus.shoppinglistjava;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.macjiji.marcus.shoppinglistjava.adapters.ExpandableListAdapter;
import com.macjiji.marcus.shoppinglistjava.db.Database;
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
 * Classe permettant de gerer la vue d'aperçu d'une liste de course
 *
 */
public class ApercuListeActivity extends AppCompatActivity {

    // Attributs pour les composants graphique et la BDD
    protected FloatingActionButton ajouterArticle;
    protected ExpandableListView listArticles;
    protected ExpandableListAdapter listAdapter;
    protected Database baseDeDonnees;

    // Attributs contenant l'ensemble des données de la BDD
    private ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
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
        setContentView(R.layout.activity_apercu_liste);

        initialiseBaseDeDonnees();
        initialiseBoutons();
        initialiseListArticles();

    }

    /**
     * Methode permettant d'initialiser la base de donnees
     */
    private void initialiseBaseDeDonnees(){
        baseDeDonnees = new Database(this);
        baseDeDonnees.open();
        shoppingListItems = baseDeDonnees.getAllShoppingListItemsWithId(getIntent().getIntExtra("id", -1));
        items = baseDeDonnees.getAllItems();
    }

    /**
     * Methode permettant d'initialiser le boutons d'ajout d'un item à la liste de courses
     */
    private void initialiseBoutons(){
        ajouterArticle = (FloatingActionButton)findViewById(R.id.ajouter_item);
        ajouterArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ajouterArticle = new Intent(ApercuListeActivity.this, AjouterItemActivity.class);
                ajouterArticle.putExtra("name", getIntent().getStringExtra("name")); // On ajoute le nom de la liste à l'Intent
                ajouterArticle.putExtra("id", getIntent().getIntExtra("id", -1)); // On ajouter l'id à l'Intent
                startActivity(ajouterArticle);
            }
        });
    }

    /**
     * Methode permettant d'initialiser la liste de courses
     */
    private void initialiseListArticles(){
        listArticles = (ExpandableListView)findViewById(R.id.apercu_liste_shopping);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listArticles.setAdapter(listAdapter);
        listArticles.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, final int childPosition, long id) {
                for(Item item : items){
                    if(item.getName_fr().equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                        new AlertDialog.Builder(ApercuListeActivity.this)
                                .setTitle("Suppression d'un items de la liste") // Titre de la boite de dialogue
                                .setMessage("Voulez-vous réellement supprimer l'item : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) + "de la liste ?") // Contenu du message
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() { // Bouton de confirmation
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(Item item : items){
                                            if(item.getName_fr().equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                                                baseDeDonnees.deleteItemIntoShoppingList(getIntent().getIntExtra("id", -1),item.getIdItem());
                                            }
                                        }
                                        shoppingListItems = baseDeDonnees.getAllShoppingListItemsWithId(getIntent().getIntExtra("id", -1));
                                        initialiseListArticles();
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener(){ // Bouton d'annulation
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show(); // On affiche la boite de dialogue
                    }
                }
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        if(shoppingListItems != null){ // On vérifie qu'il existe bien des valeurs dans une liste de courses

            // Etape 1 : On récupère dans un premier temps les catégories à afficher dans la liste de vues
            for(ShoppingListItem shoppingListItem : shoppingListItems){
                if(listDataHeader.size() == 0){ // On teste si la liste ne possède pas encore de catégories (Evite d'ajouter deux fois une catégorie)
                    listDataHeader.add(baseDeDonnees.getCategoryNameWithId(shoppingListItem.getIdCat()));
                    listDataChild.put(baseDeDonnees.getCategoryNameWithId(shoppingListItem.getIdCat()), baseDeDonnees.getPersonnalItemsFromShoppingList(getIntent().getIntExtra("id", -1), shoppingListItem.getIdCat()));
                } else {
                    if(!listDataHeader.contains(baseDeDonnees.getCategoryNameWithId(shoppingListItem.getIdCat()))){ // Si la liste des Headers ne possède pas encore le nom de la catégorie, on l'inclus dans ces cas là !
                        listDataHeader.add(baseDeDonnees.getCategoryNameWithId(shoppingListItem.getIdCat()));
                        listDataChild.put(baseDeDonnees.getCategoryNameWithId(shoppingListItem.getIdCat()), baseDeDonnees.getPersonnalItemsFromShoppingList(getIntent().getIntExtra("id", -1), shoppingListItem.getIdCat()));
                    }
                }
            }
        }

    }

}
