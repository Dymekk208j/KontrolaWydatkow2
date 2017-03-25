package pl.damiandziura.kontrolawydatkow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.INotificationSideChannel;
import android.widget.Toast;

/**
 * Created by Dymek on 23.03.2017.
 */

public class baza_danych extends SQLiteOpenHelper
{
    private Cursor c;
    private static final int DATABASE_VERSION = 6;
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
                + KEY_godzina + " TEXT, "
                + KEY_data + " TEXT )";

        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_dochody  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa + " TEXT, "
                + KEY_kwota + " DOUBLE, "
                + KEY_kategoria + " INTEGER, "
                + KEY_podkategoria + " INTEGER, "
                + KEY_godzina + " TEXT, "
                + KEY_data + " TEXT )";
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

    String getNazwa(int aID)
    {
        String aNazwa = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM "+TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aNazwa = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aNazwa;
    }

    double getKwota(int aID)
    {
        double kwota = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kwota FROM "+TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                kwota = c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return kwota;
    }

    int getKategoria(int aID)
    {
        int kategoria = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kategoria FROM "+TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                kategoria = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return kategoria;
    }

    int getPodKategoria(int aID)
    {
        int PodKategoria = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT podkategoria FROM "+TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                PodKategoria = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return PodKategoria;
    }

    String getGodzina(int aID)
    {
        String godzina = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT godzina FROM "+TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                godzina = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return godzina;
    }

    String getData(int aID)
    {
        String data2 = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT data FROM "+ TABLE_wydatki + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                data2 = c.getString(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return data2;
    }

    void DodajDochod(String Nazwa, Double kwota, int kategoria, int podkategoria, String Godzina, String Data)
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
        db.insert(TABLE_dochody, null, values);
        db.close(); // Closing database connection

    }



    int getWydatkiMaxId()
    {
        int maxID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_wydatki + " ORDER BY id DESC LIMIT 1", null);

        if(c.moveToFirst()){
            do{
                maxID = c.getInt(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return maxID;
    }

    void delete(int _ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_wydatki,"id=?",new String[]{Integer.toString(_ID)});
        db.close(); // Closing database connection
    }

    String[] getOstatnieWydatki()
    {
        String OstatnieWydatki[] = new String[10];
        double KwotaOstatnieWydatki[] = new double[10];
        String bufor;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa, kwota FROM " + TABLE_wydatki + " ORDER BY data DESC, godzina DESC LIMIT 10", null);
int a = 0;

        if(c.moveToFirst()){
            do{
                bufor = c.getString(0);
                KwotaOstatnieWydatki[a] = c.getDouble(1);

                OstatnieWydatki[a] = Integer.toString(a+1) + ". " + bufor + " " + Double.toString(KwotaOstatnieWydatki[a]) + "zł";
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();


        return OstatnieWydatki;

    }


    double PortfelPrzeliczSaldo()
    {
        double dochody =0, wydatki=0;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kwota FROM " + TABLE_wydatki, null);


        if(c.moveToFirst()){
            do{
                wydatki += c.getDouble(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kwota FROM " + TABLE_dochody, null);


        if(c.moveToFirst()){
            do{
                dochody += c.getDouble(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return dochody-wydatki;

    }

    public void PortfelUstawSaldo(Double kwota)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("kwota", kwota);

        db.replaceOrThrow(TABLE_portfel, null, values);

        db.close(); // Closing database connection
    }

    public double getPortfelSaldo()
    {
        double kwota = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kwota FROM "+TABLE_portfel + " WHERE id = " + Integer.toString(1), null);

        if(c.moveToFirst()){
            do{
                kwota = c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return kwota;
    }

}
