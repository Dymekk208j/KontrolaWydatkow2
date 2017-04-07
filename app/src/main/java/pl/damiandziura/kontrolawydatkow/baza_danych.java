package pl.damiandziura.kontrolawydatkow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.support.v4.app.INotificationSideChannel;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dymek on 23.03.2017.
 */

public class baza_danych extends SQLiteOpenHelper
{
    private Cursor c;
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "baza.db";

    // Nazwy tabeli
    private static final String TABLE_wydatki = "Wydatki";
    private static final String TABLE_dochody = "Dochody";
    private static final String TABLE_portfel = "Portfel";
    private static final String TABLE_kategoria = "Kategoria";
    private static final String TABLE_podkategoria = "Podkategoria";
    private static final String TABLE_Staly_wydatek = "StalyWydatek";
    private static final String TABLE_Staly_dochod = "StalyDochod";

    // Nazwy kolumn
    private static final String KEY_ID = "id";
    private static final String KEY_nazwa = "nazwa";
    private static final String KEY_kwota = "kwota";
    private static final String KEY_podkategoria = "podkategoria";
    private static final String KEY_kategoria = "kategoria";
    private static final String KEY_data = "data";
    private static final String KEY_godzina = "godzina";
    private static final String KEY_odkiedy = "od_kiedy";
    private static final String KEY_dokiedy = "do_kiedy";
    private static final String KEY_nastepnaData = "nastepna_data";
    private static final String KEY_czestotliwosc = "czestotliwosc";


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

    enum CZESTOTLIWOSC
    {
        DZIENNIE("DZIENNIE"),
        TYDZIEN("TYDZIEN"),
        MIESIAC("MIESIAC"),
        KWARTAL("KWARTAL"),
        ROK("ROK");


        private final String text;

        private CZESTOTLIWOSC(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
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
                + KEY_kategoria  + " INTEGER, "
                + KEY_nazwa + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Staly_wydatek + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa  + " TEXT, "
                + KEY_kwota  + " DOUBLE, "
                + KEY_kategoria  + " INTEGER, "
                + KEY_podkategoria  + " INTEGER, "
                + KEY_odkiedy  + " TEXT, "
                + KEY_dokiedy  + " TEXT, "
                + KEY_nastepnaData  + " TEXT, "
                + KEY_czestotliwosc + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Staly_dochod + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_nazwa  + " TEXT, "
                + KEY_kwota  + " DOUBLE, "
                + KEY_kategoria  + " INTEGER, "
                + KEY_podkategoria  + " INTEGER, "
                + KEY_odkiedy  + " TEXT, "
                + KEY_dokiedy  + " TEXT, "
                + KEY_nastepnaData  + " TEXT, "
                + KEY_czestotliwosc + " TEXT )";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Staly_wydatek);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Staly_dochod);

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
///TODO Pozmieniac nazwy tych kategori od pobierania danych z konkretnego wydatku zeby byly bardziej adekwatne do tego co zwracaja
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

    int getKategoriaMaxId()
    {
        int MaxID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM "+TABLE_kategoria + " ORDER BY id DESC LIMIT 1", null);

        if(c.moveToFirst()){
            do{
                MaxID = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return MaxID;
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

    //TODO dodac metode ktora bedzie uaktualniala kategorie
    ArrayList getKategorie()
    {
        ArrayList<String> ListaKategorii = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM " + TABLE_kategoria, null);

        int a = 0;
        ListaKategorii.add("0. (Domyślna kategoria)");
        if(c.moveToFirst()){
            do{
                ListaKategorii.add(Integer.toString(a+1)+". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListaKategorii;
    }

    ArrayList getINTKategorie()
    {
        ArrayList<Integer> idkategorii = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_kategoria, null);
        int a = 0;
        idkategorii.add(0);

        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                idkategorii.add(c.getInt(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  idkategorii;
    }

    void AddKategoria(String Nazwa)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nazwa,Nazwa);

        // Inserting Row
        db.insert(TABLE_kategoria, null, values);
        db.close(); // Closing database connection

    }

    void RemoveKategoria(int idKategorii)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_kategoria,"id=?",new String[]{Integer.toString(idKategorii)});
        db.close(); // Closing database connection
    }

    String getKategoriaName(int aID)
    {
        String aNazwa = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM "+TABLE_kategoria + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aNazwa = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aNazwa;
    }




    void AddPodKategoria(String Nazwa, int idKategorii)
    {
        /*
        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_podkategoria + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_kategoria  + " INTEGER, "
                + KEY_nazwa + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);
        */

        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_kategoria, idKategorii);
        values.put(KEY_nazwa,Nazwa);

        // Inserting Row
        db.insert(TABLE_podkategoria, null, values);
        db.close(); // Closing database connection

    }

    void EditPodKategoria(String Nazwa, int idKategorii, int idPodKategorii)
    {
        /*
        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_podkategoria + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_kategoria  + " INTEGER, "
                + KEY_nazwa + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);
        */

        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_kategoria, idKategorii);
        values.put(KEY_nazwa,Nazwa);

        // Inserting Row
        db.insert(TABLE_podkategoria, null, values);
        db.close(); // Closing database connection

    }

    void RemovePodKategoria(int idPodKategorii)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_podkategoria,"id=?",new String[]{Integer.toString(idPodKategorii)});
        db.close(); // Closing database connection
    }

    String getPodKategoriaName(int aID)
    {
        String aNazwa = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM "+TABLE_podkategoria + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aNazwa = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aNazwa;
    }

    ArrayList getINTpodKategorie(int idKategoria)
    {
        ArrayList<Integer> idPodkategorii = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_podkategoria + " WHERE " + KEY_kategoria + " = " + Integer.toString(idKategoria), null);

        int a = 0;
        idPodkategorii.add(0);
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                idPodkategorii.add(c.getInt(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  idPodkategorii;
    }

    ArrayList getpodKategorie(int idKategoria)
    {
        ArrayList<String> Listapodkategorii = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT " + KEY_nazwa + " FROM " + TABLE_podkategoria + " WHERE " + KEY_kategoria + " = " + Integer.toString(idKategoria), null);

        int a = 0;
        Listapodkategorii.add("0. (Domyślna podkategoria)");
        if(c.moveToFirst()){
            do{
                Listapodkategorii.add(Integer.toString(a+1)+". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  Listapodkategorii;
    }

    void updateNamePodkategoria(String name, int aID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nazwa",name); //These Fields should be your String values of actual column names


        db.update(TABLE_podkategoria, cv, "id="+aID, null);
        db.close(); // Closing database connection

       // UPDATE Podkategoria SET nazwa = "dupa" WHERE ID = 1;
    }


    //TODO dodac metode ktora bedzie uaktualniala cyklicznego wydatki
    //TODO dodac metode ktora bedzie umozliwiala usuniecie cyklicznego wydatku
    ArrayList getStaleWydatki()
    {
        ArrayList<String> ListaStalychWydatkow = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM " + TABLE_Staly_wydatek, null);

        int a = 0;
        if(c.moveToFirst()){
            do{
                ListaStalychWydatkow.add(Integer.toString(a)+ ". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListaStalychWydatkow;
    }

    ArrayList getINTstaleWydatki()
    {
        ArrayList<Integer> idStalegoWydatku = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_Staly_wydatek, null);
        int a = 0;
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                idStalegoWydatku.add(c.getInt(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  idStalegoWydatku;
    }

    public void addStalyWydatek(String aNazwa, double aKwota, int aKategoria, int aPodkategoria, String aOdKiedy,
                                String aDoKiedy, String aNastepnaData, CZESTOTLIWOSC czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nazwa,aNazwa);
        values.put(KEY_kwota,aKwota);
        values.put(KEY_kategoria,Integer.toString(aKategoria));
        values.put(KEY_podkategoria,Integer.toString(aPodkategoria));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_czestotliwosc,czest.toString());

        // Inserting Row
        db.insert(TABLE_Staly_wydatek, null, values);
        db.close(); // Closing database connection


    }

    String getStalyWydatekName(int aID)
    {
        String aNazwa = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM "+TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aNazwa = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aNazwa;
    }

    double getStalyWydatekKwota (int aID)
    {
        double kwota = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT kwota FROM "+ TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                kwota = c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return kwota;
    }

    String getStalyWydatekOD(int aID)
    {
        String od_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT od_kiedy FROM "+TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                od_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return od_kiedy;
    }

    String getStalyWydatekDO(int aID)
    {
        String do_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT do_kiedy FROM "+TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                do_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return do_kiedy;
    }

    String getStalyWydatekNastepnaData(int aID)
    {
        String nastepna_data = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nastepna_data FROM "+TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                nastepna_data = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return nastepna_data;
    }

    int getStalyWydatekKategoria (int aID)
    {
        int Kategoria = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_kategoria + " FROM "+ TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Kategoria = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Kategoria;
    }

    int getStalyWydatekPodkategoria (int aID)
    {
        int Podkategoria = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_podkategoria + " FROM "+ TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Podkategoria = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Podkategoria;
    }

    int getStalyWydatekCzestotliwosc (int aID)
    {
        String buffor = "";

        int Czestotliwosc = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_czestotliwosc + " FROM "+ TABLE_Staly_wydatek + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                buffor = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        if(buffor.equals("DZIENNIE"))
        {
            Czestotliwosc = 0;
        }else if (buffor.equals("TYDZIEN"))
        {
            Czestotliwosc = 1;
        }else if (buffor.equals("MIESIAC"))
        {
            Czestotliwosc = 2;
        }else if (buffor.equals("KWARTAL"))
        {
            Czestotliwosc = 3;
        }else if (buffor.equals("ROK"))
        {
            Czestotliwosc = 4;
        }else
        {
            Czestotliwosc = 0;
        }
        return Czestotliwosc;
    }





    ArrayList getStaleDochody()
    {
        ArrayList<String> ListaStalychDochodow = new ArrayList<String>();
        String bufor;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nazwa FROM " + TABLE_Staly_dochod, null);

        int a = 0;
        if(c.moveToFirst()){
            do{
                bufor = c.getString(0);
                ListaStalychDochodow.add(Integer.toString(a+1)+". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListaStalychDochodow;
    }

    ArrayList getINTstaleDochody()
    {
        ArrayList<Integer> idStalegoDochodu = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_Staly_dochod, null);
        int a = 0;
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                idStalegoDochodu.add(c.getInt(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  idStalegoDochodu;
    }
    public void addStalyDochod(String aNazwa, double aKwota, int aKategoria, int aPodkategoria, String aOdKiedy,
                                String aDoKiedy, String aNastepnaData, CZESTOTLIWOSC czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nazwa,aNazwa);
        values.put(KEY_kwota,aKwota);
        values.put(KEY_kategoria,Integer.toString(aKategoria));
        values.put(KEY_podkategoria,Integer.toString(aPodkategoria));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_czestotliwosc,czest.toString());

        // Inserting Row
        db.insert(TABLE_Staly_dochod, null, values);
        db.close(); // Closing database connection


    }


}
