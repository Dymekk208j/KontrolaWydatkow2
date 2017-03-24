package pl.damiandziura.kontrolawydatkow;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dymek on 23.03.2017.
 */

public class baza_danych extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "baza.db";

    // Nazwy tabeli
    private static final String TABLE_wydatki = "Wydatki";
    private static final String TABLE_dochody = "Dochody";
    private static final String TABLE_portfel = "Portfel";
    private static final String TABLE_kategoria = "Kategoria";
    private static final String TABLE_podkategoria = "Podkategoria";

    // Nazwy kolumn
    private static final String KEY_ID = "id";
    private static final String KEY_nazwa = "nazwa";
    private static final String KEY_kwota = "kwota";
    private static final String KEY_podkategoria = "podkategoria";
    private static final String KEY_kategoria = "kategoria";
    private static final String KEY_data = "data";
    private static final String KEY_godzina = "godzina";

    // property help us to keep data
    private int wydatki_id;
    private String nazwa;
    private double kwota;
    private int kategoria;
    private int podkategoria;
    private int data;

    public baza_danych(Context context )
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_wydatki  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa + " TEXT, "
                + KEY_kwota + " DOUBLE, "
                + KEY_kategoria + " INTEGER, "
                + KEY_podkategoria + " INTEGER, "
                + KEY_godzina + " TIME, "
                + KEY_data + " DATE )";

        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_dochody  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa + " TEXT, "
                + KEY_kwota + " DOUBLE, "
                + KEY_kategoria + " INTEGER, "
                + KEY_podkategoria + " INTEGER, "
                + KEY_godzina + " TIME, "
                + KEY_data + " DATE )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_portfel  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_kwota + " DOUBLE )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_kategoria  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_podkategoria + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "Kategoria"  + " INTEGER, "
                + KEY_nazwa + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_wydatki);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_portfel);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_dochody);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_kategoria);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_podkategoria);

        // Create tables again
        onCreate(db);

    }

    void DodajWydatek(String Nazwa, Double kwota, int kategoria, int podkategoria, String Godzina, String Data)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nazwa,Nazwa);
        values.put(KEY_kwota,Double.toString(kwota));
        values.put(KEY_kategoria,Integer.toString(kategoria));
        values.put(KEY_podkategoria,Integer.toString(podkategoria));
        values.put(KEY_godzina,Godzina);
        values.put(KEY_data,Data);

        // Inserting Row
        db.insert(TABLE_wydatki, null, values);
        db.close(); // Closing database connection

    }

    public void update(String Nazwa, Double kwota, int kategoria, int podkategoria, String Data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nazwa,Nazwa);
        values.put(KEY_kwota,Double.toString(kwota));
        values.put(KEY_kategoria,Integer.toString(kategoria));
        values.put(KEY_podkategoria,Integer.toString(podkategoria));
        values.put(KEY_data,Data);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(TABLE_wydatki, values, KEY_ID + "= ?", new String[] { String.valueOf(wydatki_id) });
        db.close(); // Closing database connection
    }

}
