package com.macjiji.marcus.shoppinglistjava.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macjiji.marcus.shoppinglistjava.R;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingList;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.shoppinglistjava.MainActivity
 *
 * Classe Adapter permettant d'afficher l'ensemble de nos liste de courses sur l'ecran d'accueil
 *
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> shoppingListArrayList) {
        super(context, 0, shoppingListArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Etape 1 : On récupère une liste de courses particulière
        ShoppingList shoppingList = getItem(position);

        // Etape 2 : On utilise le LayoutInflater pour inclure le layout list_item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Etape 3 : On récupère la référence du champ de texte shoppingListName
        TextView shoppingListName = (TextView) convertView.findViewById(R.id.shoppingListName);

        // Etape 4 : On inclus le nom de la liste et la couleur de la liste sur la vue texte
        shoppingListName.setText(shoppingList.getName());
        shoppingListName.setTextColor(Color.argb(255, shoppingList.getRed(), shoppingList.getGreen(), shoppingList.getBlue()));

        // Etape 5 : On retournne la vue créée
        return convertView;
    }

}
