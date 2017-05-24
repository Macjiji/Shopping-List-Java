package com.macjiji.marcus.shoppinglistjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.macjiji.marcus.shoppinglistjava.adapters.ShoppingListAdapter;
import com.macjiji.marcus.shoppinglistjava.app.AppController;
import com.macjiji.marcus.shoppinglistjava.db.Database;
import com.macjiji.marcus.shoppinglistjava.objects.Category;
import com.macjiji.marcus.shoppinglistjava.objects.Item;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Marcus
 * @version 1.0
 *
 * Classe permettant de gerer la vue d'accueil de l'application
 *
 */
public class MainActivity extends AppCompatActivity {

    protected Database baseDeDonnees;
    protected ListView listesCourses;
    protected Button ajouterListe;
    protected Button majListe;

    private ArrayList<Category> listCategories = new ArrayList<>();
    private ArrayList<Item> listItems = new ArrayList<>();
    private ArrayList<ShoppingList> shoppingLists = new ArrayList<>();

    private final static String URL_GET_DATABASE_DATAS = "http://192.168.1.63/shopping/getDatabaseDatas.php";

    /**
     * Methode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseBaseDeDonnees();
        initialiseBoutons();
        initialiseListesCourses();
    }

    /**
     * Methode permettant d'initialiser la base de donnees
     */
    private void initialiseBaseDeDonnees(){
        baseDeDonnees = new Database(this);
        baseDeDonnees.open();
        listCategories = baseDeDonnees.getAllCategories();
        listItems = baseDeDonnees.getAllItems();
        shoppingLists = baseDeDonnees.getAllShoppingList();
    }

    /**
     * Methode permettant d'initialiser les listes de courses presentes en base de donnees
     */
    private void initialiseListesCourses(){
        listesCourses = (ListView)findViewById(R.id.list_shopping);
        // On crée l'adapter par rapport aux données présentes dans la liste
        ShoppingListAdapter adapter = new ShoppingListAdapter(this, shoppingLists);
        // On attache l'adapter
        listesCourses.setAdapter(adapter);
        // On créer enfin la méthode qui va détecter le clic sur un item en particulier
        listesCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent apercuList = new Intent(MainActivity.this, ApercuListeActivity.class);
                apercuList.putExtra("name", shoppingLists.get(i).getName()); // On ajoute le nom de la liste à l'Intent
                apercuList.putExtra("id", shoppingLists.get(i).getId()); // On ajouter l'id à l'Intent
                startActivity(apercuList);
            }
        });
    }

    /**
     * Methode permettant d'initialiser les boutons
     */
    private void initialiseBoutons(){
        ajouterListe = (Button)findViewById(R.id.creer_liste);
        ajouterListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AjouterListe.class));
            }
        });

        majListe = (Button)findViewById(R.id.maj_produit);
        majListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                majListe();
            }
        });

    }

    /**
     * Methode permettant de mettre à jour la liste des items et categories a partir du serveur
     */
    private void majListe(){

        final String tag_string_req = "getDatabaseDatas";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_GET_DATABASE_DATAS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // Réponse reçue du serveur
                try {
                    Log.d("jOBJ", "Valeurs : " + response);
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error){

                        // Etape 1 : On récupère les arrays Categories et Items
                        JSONArray categories = jObj.getJSONArray("categories");
                        JSONArray items = jObj.getJSONArray("items");

                        // Etape 2 : On insère en base de données les catégories
                        Log.d("getDatas", "Valeurs contenues dans les categories : " + jObj.getJSONArray("categories").toString());
                        if(listCategories == null){ // S'il n'existe pas encore de données dans la BDD, on insère toutes les catégories
                            for(int i = 0; i < categories.length(); i++){
                                Category category = new Category();
                                category.setIdCat(categories.getJSONObject(i).getInt("id_cat"));
                                category.setNameEn(categories.getJSONObject(i).getString("name_cat_en"));
                                category.setNameFr(categories.getJSONObject(i).getString("name_cat_fr"));
                                baseDeDonnees.createCategory(category);
                            }
                        } else { // Sinon, on insère seulement celles qui n'existe pas
                            for(int i = 0; i < categories.length(); i++){
                                Category category = new Category();
                                category.setIdCat(categories.getJSONObject(i).getInt("id_cat"));
                                category.setNameEn(categories.getJSONObject(i).getString("name_cat_en"));
                                category.setNameFr(categories.getJSONObject(i).getString("name_cat_fr"));
                                if(!listCategories.contains(category)){ // Si la catégorie n'existe pas, on l'insère en BDD
                                    baseDeDonnees.createCategory(category);
                                }
                            }
                        }

                        // Etape 3 : On met à jour les valeurs présentes dans la liste des catégories
                        listCategories = baseDeDonnees.getAllCategories();

                        // Etape 4 : On insère en base de données les items
                        Log.d("getDatas", "Valeurs contenues dans les items : " + jObj.getJSONArray("items").toString());
                        if(listItems == null){ // S'il n'existe pas encore de données dans la BDD, on insère tous les items
                            for(int i = 0; i < items.length(); i++){
                                Item item = new Item();
                                item.setIdItem(items.getJSONObject(i).getInt("id_item"));
                                item.setIdCat(items.getJSONObject(i).getInt("id_cat"));
                                item.setName_en(items.getJSONObject(i).getString("name_item_en"));
                                item.setName_fr(items.getJSONObject(i).getString("name_item_fr"));
                                baseDeDonnees.createItem(item);
                            }
                        } else { // Sinon, on insère seulement ceux qui n'existe pas
                            for(int i = 0; i < items.length(); i++){
                                Item item = new Item();
                                item.setIdItem(items.getJSONObject(i).getInt("id_item"));
                                item.setIdCat(items.getJSONObject(i).getInt("id_cat"));
                                item.setName_en(items.getJSONObject(i).getString("name_item_en"));
                                item.setName_fr(items.getJSONObject(i).getString("name_item_fr"));
                                if(!listItems.contains(item)){ // Si l'item n'existe pas, on l'insère en BDD
                                    baseDeDonnees.createItem(item);
                                }
                            }
                        }

                        // Etape 5 : On met à jour les valeurs présentes dans la liste des items
                        listItems = baseDeDonnees.getAllItems();

                    } else {
                        Log.d("getDatas", "Une erreur est survenue : " + jObj.getString("error_msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("getDatas", "Response : " + response);
                    Log.d("getDatas", "Une erreur est survenue : " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() { // Erreur reçue entre le serveur
            @Override
            public void onErrorResponse(VolleyError error) { }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




}
