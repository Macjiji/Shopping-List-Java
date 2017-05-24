package com.macjiji.marcus.shoppinglistjava;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.macjiji.marcus.shoppinglistjava.db.Database;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingList;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see Database
 * @see ShoppingList
 *
 * Classe permettant de gerer la vue d'ajout d'une nouvelle liste de courses
 *
 */
public class AjouterListe extends AppCompatActivity {

    protected Database baseDeDonnees;
    protected EditText nomListe;
    protected TextView apercuCouleur;
    protected Button ajouterListe;
    protected SeekBar red;
    protected SeekBar green;
    protected SeekBar blue;
    protected ArrayList<ShoppingList> shoppingLists = new ArrayList<>();

    /**
     * Methode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_liste);

        initialiseBaseDeDonnees();
        initialiseTextView();
        initialiseSeekBar();
        initialiseEditText();
        initialiseBoutons();
    }

    /**
     * Methode d'initialisation de la base de donnees
     */
    private void initialiseBaseDeDonnees(){
        baseDeDonnees = new Database(this);
        baseDeDonnees.open();
        shoppingLists = baseDeDonnees.getAllShoppingList();
    }

    /**
     * Methode d'initialisation de l'aperçu de couleur
     */
    private void initialiseTextView(){
        apercuCouleur = (TextView)findViewById(R.id.apercu_couleur);
    }

    /**
     * Methode d'initialisation des SeekBar
     */
    private void initialiseSeekBar(){
        red = (SeekBar)findViewById(R.id.red);
        green = (SeekBar)findViewById(R.id.green);
        blue = (SeekBar)findViewById(R.id.blue);

        // Etape 1 : On attribue une valeur par défaut aux SeekBar
        red.setProgress(125);
        green.setProgress(125);
        blue.setProgress(125);

        // Etape 2 : On crée les différents listener sur les SeekBar, en changeant la couleur de l'aperçu de la couleur de liste
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                apercuCouleur.setBackgroundColor(Color.argb(255, i, green.getProgress(), blue.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                apercuCouleur.setBackgroundColor(Color.argb(255, red.getProgress(), i, blue.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                apercuCouleur.setBackgroundColor(Color.argb(255, red.getProgress(), green.getProgress(), i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Etape 3 : On attribue à l'aperçu une couleur par défaut grace aux valeur définit en étape 1
        apercuCouleur.setBackgroundColor(Color.argb(255, red.getProgress(), green.getProgress(), blue.getProgress()));

    }

    /**
     * Methode d'initialisation du champ d'édition du nom de la liste
     */
    private void initialiseEditText(){
        nomListe = (EditText)findViewById(R.id.nom_liste);
    }

    /**
     * Methode d'initialisation des boutons
     */
    private void initialiseBoutons(){
        ajouterListe = (Button)findViewById(R.id.creer_liste);
        ajouterListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nomListe.getText().toString().trim().isEmpty()){ // On vérifie que le nom a été renseigné
                    boolean verifExistance = false;

                    if(shoppingLists != null){ // On vérifie dans un premier si des listes existent déjà !
                        for(ShoppingList shoppingList : shoppingLists){ // On vérifie également que le nom n'existe pas déjà !
                            if(shoppingList.getName().equals(nomListe.getText().toString())){
                                nomListe.setError("Cette liste existe déjà");
                                verifExistance = true;
                            }
                        }
                    }

                    if(!verifExistance){ // Si le nom n'existe pas déjà, on l'insère en base de données
                        baseDeDonnees.createShoppingList(new ShoppingList(0, nomListe.getText().toString(), red.getProgress(), green.getProgress(), blue.getProgress()));
                        startActivity(new Intent(AjouterListe.this, MainActivity.class));
                    }
                } else {
                    nomListe.setError("Vous devez renseigner un nom de liste !");
                }
            }
        });
    }

}
