package com.macjiji.marcus.shoppinglistjava.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.macjiji.marcus.shoppinglistjava.objects.Category;
import com.macjiji.marcus.shoppinglistjava.objects.Item;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingList;
import com.macjiji.marcus.shoppinglistjava.objects.ShoppingListItem;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @see com.macjiji.marcus.shoppinglistjava.MainActivity
 *
 * Classe permettant de gerer les interaction avec la base de donnees de l'application
 *
 */

public class Database {

    private Database.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    private static final String DATABASE_NAME = "shoppinglist.db"; // Nom de la base de données
    private static final int DATABASE_VERSION = 1; // Version de la base de données

    // Nom des différentes tables de la base de données
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_PERSONNAL_SHOPPING = "personnalshopping";
    private static final String TABLE_PERSONNAL_ITEMS_SHOPPING = "personnalshoppingitems";


    // Nom des colonnes de la table CATEGORY
    private static final String COL_CATEGORY_ID="_id";
    private static final String COL_CATEGORY_NOM_FR="nom_fr";
    private static final String COL_CATEGORY_NOM_EN="nom_en";

    // Nom des colonnes de la table ITEMS
    private static final String COL_ITEM_ID="_id";
    private static final String COL_ITEM_ID_CAT="id_cat";
    private static final String COL_ITEM_NOM_FR="nom_fr";
    private static final String COL_ITEM_NOM_EN="nom_en";

    // Nom des colonnes de la table PERSONNAL SHOPPING
    private static final String COL_PERSONNAL_SHOPPING_ID = "_id";
    private static final String COL_PERSONNAL_SHOPPING_NAME = "nom";
    private static final String COL_PERSONNAL_SHOPPING_RED = "red";
    private static final String COL_PERSONNAL_SHOPPING_GREEN = "green";
    private static final String COL_PERSONNAL_SHOPPING_BLUE = "blue";

    // Nom des colonnes de la table PERSONNAL SHOPPING ITEMS
    private static final String COL_PERSONNAL_SHOPPING_ITEMS_ID = "_id";
    private static final String COL_PERSONNAL_SHOPPING_ITEMS_LIST = "id_list";
    private static final String COL_PERSONNAL_SHOPPING_ITEMS_CAT = "id_cat";
    private static final String COL_PERSONNAL_SHOPPING_ITEMS_ITEM = "id_item";

    // Chaine "CREATE TABLE" pour la table CATEGORY
    private static final String CREATE_TABLE_CATEGORY
            = "CREATE TABLE " + TABLE_CATEGORY  + " ( "
            + COL_CATEGORY_ID + " INTEGER PRIMARY KEY NOT NULL, " // ID de la categorie
            + COL_CATEGORY_NOM_EN + " TEXT NOT NULL, " // Nom de la categorie en anglais
            + COL_CATEGORY_NOM_FR + " TEXT NOT NULL);"; // Nom de la categorie en français


    // Chaine "CREATE TABLE" pour la table ITEMS
    private static final String CREATE_TABLE_ITEMS
            = "CREATE TABLE " + TABLE_ITEMS  + " ( "
            + COL_ITEM_ID + " INTEGER PRIMARY KEY NOT NULL, " // ID de l'item
            + COL_ITEM_ID_CAT + " INTEGER NOT NULL, " // ID de la categorie
            + COL_ITEM_NOM_EN + " TEXT NOT NULL, " // Nom de l'item en anglais
            + COL_ITEM_NOM_FR + " TEXT NOT NULL);"; // Nom de l'item en français

    // Chaine "CREATE TABLE" pour la table PERSONNAL SHOPPING
    private static final String CREATE_TABLE_PERSONNAL_SHOPPING
            = "CREATE TABLE " + TABLE_PERSONNAL_SHOPPING + " ( "
            + COL_PERSONNAL_SHOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " // ID de la liste de courses
            + COL_PERSONNAL_SHOPPING_NAME + " TEXT NOT NULL, " // Nom de la liste de courses
            + COL_PERSONNAL_SHOPPING_RED + " INTEGER NOT NULL, " // Teinte rouge de la liste de courses
            + COL_PERSONNAL_SHOPPING_GREEN + " INTEGER NOT NULL, " // Teinte verte de la liste de courses
            + COL_PERSONNAL_SHOPPING_BLUE + " INTEGER NOT NULL);"; // Teinte bleue de la liste de courses


    // Chaine "CREATE TABLE" pour la table PERSONNAL SHOPPING ITEMS
    private static final String CREATE_TABLE_PERSONNAL_SHOPPING_ITEMS
            = "CREATE TABLE " + TABLE_PERSONNAL_ITEMS_SHOPPING  + " ( "
            + COL_PERSONNAL_SHOPPING_ITEMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " // Id de la ligne
            + COL_PERSONNAL_SHOPPING_ITEMS_LIST + " INTEGER NOT NULL, " // Id de la liste de courses
            + COL_PERSONNAL_SHOPPING_ITEMS_CAT + " INTEGER NOT NULL, " // Id de la categorie
            + COL_PERSONNAL_SHOPPING_ITEMS_ITEM + " INTEGER NOT NULL);"; // Id de l'item






    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        // Méthode de création des tables
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_CATEGORY);
            db.execSQL(CREATE_TABLE_ITEMS);
            db.execSQL(CREATE_TABLE_PERSONNAL_SHOPPING);
            db.execSQL(CREATE_TABLE_PERSONNAL_SHOPPING_ITEMS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONNAL_ITEMS_SHOPPING);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONNAL_SHOPPING);
            onCreate(db);
        }

    }





    /**
     * Constructeur par défaut, permettant la création d'une instance de la base de données sur une activité.
     */
    public Database(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Méthode permettant l'ouverture de la base de données
     */
    public Database open() throws SQLException {
        mDbHelper = new Database.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Méthode permettant la fermeture de la base de données
     */
    public void close() {
        mDbHelper.close();
    }





    /**
     * Methode permettant de creer une categorie en base de donnees
     * @param category La categorie a creer en base de donnees
     * @return Une valeur superieure a 0 si insertion reussi
     * @throws SQLException Exception
     */
    public long createCategory(Category category) throws SQLException{
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_CATEGORY_ID, category.getIdCat());
        initialValues.put(COL_CATEGORY_NOM_EN, category.getNameEn());
        initialValues.put(COL_CATEGORY_NOM_FR, category.getNameFr());
        return mDb.insert(TABLE_CATEGORY, null, initialValues);
    }

    /**
     * Methode permettant de creer un item en base de donnees
     * @param item L'item a inserer en base de donnees
     * @return Une valeur superieure a 0 si insertion reussi
     * @throws SQLException Exception
     */
    public long createItem(Item item) throws SQLException{
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ITEM_ID, item.getIdItem());
        initialValues.put(COL_ITEM_ID_CAT, item.getIdCat());
        initialValues.put(COL_ITEM_NOM_EN, item.getName_en());
        initialValues.put(COL_ITEM_NOM_FR, item.getName_fr());
        return mDb.insert(TABLE_ITEMS, null, initialValues);
    }

    /**
     * Methode permettant de creer une liste de courses en base de donnees
     * @param shoppingList La liste a creer
     * @return Une valeur superieure a 0 si insertion reussi
     * @throws SQLException Exception
     */
    public long createShoppingList(ShoppingList shoppingList) throws SQLException{
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_PERSONNAL_SHOPPING_NAME, shoppingList.getName());
        initialValues.put(COL_PERSONNAL_SHOPPING_RED, shoppingList.getRed());
        initialValues.put(COL_PERSONNAL_SHOPPING_GREEN, shoppingList.getGreen());
        initialValues.put(COL_PERSONNAL_SHOPPING_BLUE, shoppingList.getBlue());
        return mDb.insert(TABLE_PERSONNAL_SHOPPING, null, initialValues);
    }

    /**
     * Methode permettant d'inserer un nouvel item dans une liste de courses
     * @param shoppingListItem L'item a inserer dans la liste de courses
     * @return Une valeur superieure a 0 si insertion reussi
     * @throws SQLException Exception
     */
    public long createShoppingListItem(ShoppingListItem shoppingListItem) throws SQLException{
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_PERSONNAL_SHOPPING_ITEMS_LIST, shoppingListItem.getIdList());
        initialValues.put(COL_PERSONNAL_SHOPPING_ITEMS_CAT, shoppingListItem.getIdCat());
        initialValues.put(COL_PERSONNAL_SHOPPING_ITEMS_ITEM, shoppingListItem.getIdItem());
        return mDb.insert(TABLE_PERSONNAL_ITEMS_SHOPPING, null, initialValues);
    }




    /**
     * Méthode permettant de renvoyer l'ensemble des categories disponibles dans la base de donnees
     * @return Les categories enregistrees en base de donnees, null sinon
     */
    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        Cursor mCursor =
                mDb.query(TABLE_CATEGORY, null, null, null, null, null, null);
        if(mCursor != null && mCursor.moveToFirst()){
            for(int i=0; i<mCursor.getCount(); i++){
                Category category = new Category();
                category.setIdCat(mCursor.getInt(mCursor.getColumnIndex(COL_CATEGORY_ID)));
                category.setNameEn(mCursor.getString(mCursor.getColumnIndex(COL_CATEGORY_NOM_EN)));
                category.setNameFr(mCursor.getString(mCursor.getColumnIndex(COL_CATEGORY_NOM_FR)));
                categories.add(category);
                mCursor.moveToNext();
            }
            mCursor.close();
            return categories;
        } else {
            return null;
        }
    }

    /**
     * Methode permettant de renvoyer l'ensemble des items presents en base de donneees
     * @return Les items enregistrees en base de donnees, null sinon
     */
    public ArrayList<Item> getAllItems(){
        ArrayList<Item> items = new ArrayList<>();
        Cursor mCursor =
                mDb.query(TABLE_ITEMS, null, null, null, null, null, null);
        if(mCursor != null && mCursor.moveToFirst()){
            for(int i=0; i<mCursor.getCount(); i++){
                Item item = new Item();
                item.setIdItem(mCursor.getInt(mCursor.getColumnIndex(COL_ITEM_ID)));
                item.setIdCat(mCursor.getInt(mCursor.getColumnIndex(COL_ITEM_ID_CAT)));
                item.setName_en(mCursor.getString(mCursor.getColumnIndex(COL_ITEM_NOM_EN)));
                item.setName_fr(mCursor.getString(mCursor.getColumnIndex(COL_ITEM_NOM_FR)));
                items.add(item);
                mCursor.moveToNext();
            }
            mCursor.close();
            return items;
        } else {
            return null;
        }
    }

    /**
     * Methode permettant de renvoyer la liste des listes de courses presentes en base de donnees
     * @return Les listes de courses enregistrees en base de donnees, null sinon
     */
    public ArrayList<ShoppingList> getAllShoppingList(){
        ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
        Cursor mCursor =
                mDb.query(TABLE_PERSONNAL_SHOPPING, null, null, null, null, null, null);
        if(mCursor != null && mCursor.moveToFirst()){
            for(int i=0; i<mCursor.getCount(); i++){
                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setId(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ID)));
                shoppingList.setName(mCursor.getString(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_NAME)));
                shoppingList.setRed(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_RED)));
                shoppingList.setGreen(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_GREEN)));
                shoppingList.setBlue(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_BLUE)));
                shoppingLists.add(shoppingList);
                mCursor.moveToNext();
            }
            mCursor.close();
            return shoppingLists;
        } else {
            return null;
        }
    }

    /**
     * Methode permettant de renvoyer la liste des items present dans une liste de courses
     * @param listId L'identifiant de la liste de courses
     * @return Les items presents dans la liste de courses, null sinon
     */
    public ArrayList<ShoppingListItem> getAllShoppingListItemsWithId(final int listId){
        ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
        Cursor mCursor =
                mDb.query(TABLE_PERSONNAL_ITEMS_SHOPPING, null,  COL_PERSONNAL_SHOPPING_ITEMS_LIST + "='" + listId + "'", null, null, null, null);
        if(mCursor != null && mCursor.moveToFirst()){
            for(int i=0; i<mCursor.getCount(); i++){
                ShoppingListItem shoppingListItem = new ShoppingListItem();
                shoppingListItem.setId(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ITEMS_ID)));
                shoppingListItem.setIdList(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ITEMS_LIST)));
                shoppingListItem.setIdCat(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ITEMS_CAT)));
                shoppingListItem.setIdItem(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ITEMS_ITEM)));
                shoppingListItems.add(shoppingListItem);
                mCursor.moveToNext();
            }
            mCursor.close();
            return shoppingListItems;
        } else {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer le nom d'une catégorie à partir de son identifiant
     *      -> Pour avoir la valeur en anglais, changer COL_CATEGORY_NOM_FR en COL_CATEGORY_NOM_EN
     * @param id L'identifiant de la catégorie
     * @return Le nom de la catégorie
     * @throws SQLException Exception
     */
    public String getCategoryNameWithId(final int id) throws SQLException{
        Cursor mCursor =
                mDb.query(true, TABLE_CATEGORY, new String[] {COL_CATEGORY_NOM_FR}, COL_CATEGORY_ID + "='" + id + "'", null,
                        null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            String res = mCursor.getString(mCursor.getColumnIndex(COL_CATEGORY_NOM_FR));
            mCursor.close();
            return res;
        } else {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer le nom d'un item à partir de son identifiant
     *      -> Pour avoir la valeur en anglais, changer COL_ITEM_NOM_FR en COL_ITEM_NOM_EN
     * @param id L'identifiant de l'item
     * @return Le nom de l'item
     * @throws SQLException Exception
     */
    private String getItemNameWithId(final int id) throws SQLException{
        Cursor mCursor =
                mDb.query(true, TABLE_ITEMS, new String[] {COL_ITEM_NOM_FR}, COL_ITEM_ID + "='" + id + "'", null,
                        null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            String res = mCursor.getString(mCursor.getColumnIndex(COL_ITEM_NOM_FR));
            mCursor.close();
            return res;
        } else {
            return null;
        }
    }

    /**
     * Methode permettant de recuperer une liste d'item présents dans une liste de courses à partir de leur categorie
     * @param listId L'identifiant de la liste de courses
     * @param categoryId La categorie des items
     * @return Une liste d'items dans une categorie
     */
    public ArrayList<String> getPersonnalItemsFromShoppingList(final int listId, final int categoryId){
        ArrayList<String> shoppingListItems = new ArrayList<>();
        Cursor mCursor =
                mDb.query(TABLE_PERSONNAL_ITEMS_SHOPPING, null,  COL_PERSONNAL_SHOPPING_ITEMS_LIST + "='" + listId + "' AND " + COL_PERSONNAL_SHOPPING_ITEMS_CAT + "='" + categoryId + "'", null, null, null, null);
        if(mCursor != null && mCursor.moveToFirst()){
            for(int i=0; i<mCursor.getCount(); i++){
                shoppingListItems.add(getItemNameWithId(mCursor.getInt(mCursor.getColumnIndex(COL_PERSONNAL_SHOPPING_ITEMS_ITEM))));
                mCursor.moveToNext();
            }
            mCursor.close();
            return shoppingListItems;
        } else {
            return null;
        }
    }


    /**
     * Methode permettant de supprimer une categorie
     * @param id L'identifiant de la categorie
     * @throws SQLException Exception
     */
    public void deleteCategoryWithId(final int id) throws SQLException {
        mDb.delete(TABLE_CATEGORY, COL_CATEGORY_ID + " = '" + id + "'", null);
    }

    /**
     * Methode permettant de supprimer un item
     * @param id L'identifiant de l'item
     * @throws SQLException Exception
     */
    public void deleteItemWithId(final int id) throws SQLException {
        mDb.delete(TABLE_ITEMS, COL_ITEM_ID + " = '" + id + "'", null);
    }

    /**
     * Methode permettant de supprimer une liste de shopping en base de donnees. Deux etapes sont necessaires :
     *      -> On supprimer toutes les iterations de la liste presentes dans la table PERSONNAL SHOPPING ITEMS
     *      -> On supprime la liste dans la table PERSONNAL SHOPPING.
     * @param id L'identifiant de la liste de courses
     * @throws SQLException Exception
     */
    public void deleteShoppingListWithId(final int id) throws SQLException {
        mDb.delete(TABLE_PERSONNAL_ITEMS_SHOPPING, COL_PERSONNAL_SHOPPING_ITEMS_LIST + " = '" + id + "'", null);
        mDb.delete(TABLE_PERSONNAL_SHOPPING, COL_PERSONNAL_SHOPPING_ID + " = '" + id + "'", null);
    }

    /**
     * Methode permettant de supprimer un element dans une liste de courses
     * @param idList L'identifiant de la liste de courses
     * @param idItem L'identifiant de l'item
     * @throws SQLException Exception
     */
    public void deleteItemIntoShoppingList(final int idList, final int idItem) throws SQLException{
        mDb.delete(TABLE_PERSONNAL_ITEMS_SHOPPING, COL_PERSONNAL_SHOPPING_ITEMS_LIST + " = '" + idList + "' AND " + COL_PERSONNAL_SHOPPING_ITEMS_ITEM + " = '" + idItem + "'", null);
    }




    /**
     * Methode permettant de mettre a jour une categorie
     * @param category La categorie a mettre a jour
     * @throws SQLException Exception
     */
    public void updateCategory(Category category) throws SQLException{
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CATEGORY_ID, category.getIdCat());
        contentValues.put(COL_CATEGORY_NOM_EN, category.getNameEn());
        contentValues.put(COL_CATEGORY_NOM_FR, category.getNameFr());
        mDb.update(TABLE_CATEGORY, contentValues, COL_CATEGORY_ID + "='" + category.getIdCat() + "'", null);
    }

    /**
     * Methode permettant de mettre a jour un item
     * @param item L'item a mettre a jour
     * @throws SQLException Exception
     */
    public void updateItem(Item item) throws SQLException{
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ITEM_ID, item.getIdItem());
        contentValues.put(COL_ITEM_ID_CAT, item.getIdCat());
        contentValues.put(COL_ITEM_NOM_EN, item.getName_en());
        contentValues.put(COL_ITEM_NOM_FR, item.getName_fr());
        mDb.update(TABLE_ITEMS, contentValues, COL_ITEM_ID + "='" + item.getIdItem() + "'", null);
    }


}
