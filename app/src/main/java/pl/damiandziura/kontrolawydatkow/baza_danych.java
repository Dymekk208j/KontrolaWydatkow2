package pl.damiandziura.kontrolawydatkow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.Calendar;


 class baza_danych extends SQLiteOpenHelper
{
    private Cursor c;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "baza2.db";

    // Name tabeli
    private static final String TABLE_Expenses = "Expenses";
    private static final String TABLE_Income = "Income";
    private static final String TABLE_Wallet = "Wallet";
    private static final String TABLE_Category = "Category";
    private static final String TABLE_Subcategory = "Subcategory";
    private static final String TABLE_Cyclical_Expenses = "CyclicalExpenses";
    private static final String TABLE_Cyclical_Income = "CyclicalIncome";

    // Name kolumn
    private static final String KEY_ID = "id";
    private static final String KEY_Name = "Name";
    private static final String KEY_Amount = "Amount";
    private static final String KEY_Subcategory = "Subcategory";
    private static final String KEY_Category = "Category";
    private static final String KEY_date = "date";
    private static final String KEY_hour = "hour";
    private static final String KEY_odkiedy = "od_kiedy";
    private static final String KEY_dokiedy = "do_kiedy";
    private static final String KEY_nastepnaData = "nastepna_data";
    private static final String KEY_frequency = "frequency";
    private static final String KEY_Type = "Type"; // 0- Expenses; 1- Income// Do czego przypisana jest Category.

     baza_danych(Context context )
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    enum FREQUENCY
    {
        DAILY("DAILY"),
        WEEKLY("WEEKLY"),
        MONTHLY("MONTHLY"),
        QUARTERLY("QUARTERLY"),
        YEARLY("YEARLY");


        private final String text;

        FREQUENCY(final String text) {
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
        String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Expenses  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Name + " TEXT, "
                + KEY_Amount + " DOUBLE, "
                + KEY_Category + " INTEGER, "
                + KEY_Subcategory + " INTEGER, "
                + KEY_hour + " TEXT, "
                + KEY_date + " TEXT )";

        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Income  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Name + " TEXT, "
                + KEY_Amount + " DOUBLE, "
                + KEY_Category + " INTEGER, "
                + KEY_Subcategory + " INTEGER, "
                + KEY_hour + " TEXT, "
                + KEY_date + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Wallet  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Amount + " DOUBLE )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Category  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Name + " TEXT, "
                + KEY_Type + " INTEGER )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Subcategory + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Category  + " INTEGER, "
                + KEY_Name + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Cyclical_Expenses + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Name  + " TEXT, "
                + KEY_Amount  + " DOUBLE, "
                + KEY_Category  + " INTEGER, "
                + KEY_Subcategory  + " INTEGER, "
                + KEY_odkiedy  + " TEXT, "
                + KEY_dokiedy  + " TEXT, "
                + KEY_nastepnaData  + " TEXT, "
                + KEY_frequency + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_Cyclical_Income + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_Name  + " TEXT, "
                + KEY_Amount  + " DOUBLE, "
                + KEY_Category  + " INTEGER, "
                + KEY_Subcategory  + " INTEGER, "
                + KEY_odkiedy  + " TEXT, "
                + KEY_dokiedy  + " TEXT, "
                + KEY_nastepnaData  + " TEXT, "
                + KEY_frequency + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Expenses);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Wallet);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Income);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Category);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Subcategory);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Cyclical_Expenses);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Cyclical_Income);

        // Create tables again
        onCreate(db);

    }

    void AddExpenses(String Name, Double Amount, int Category, int Subcategory, String hour, String Date)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,Name);
        values.put(KEY_Amount,Double.toString(Amount));
        values.put(KEY_Category,Integer.toString(Category));
        values.put(KEY_Subcategory,Integer.toString(Subcategory));
        values.put(KEY_hour,hour);
        values.put(KEY_date,Date);

        // Inserting Row
        db.insert(TABLE_Expenses, null, values);
        db.close(); // Closing database connection

    }

    int getCategoryMaxId()
    {
        int MaxID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM "+TABLE_Category + " ORDER BY id DESC LIMIT 1", null);

        if(c.moveToFirst()){
            do{
                MaxID = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return MaxID;
    }

    private void setDefaultCategoryAndSubcategory(int aID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Category,0);
        values.put(KEY_Subcategory,0);

        // Inserting Row
        db.update(TABLE_Expenses, values, "id="+aID, null);
        db.close(); // Closing database connection



    }

     ArrayList<String> getExpensesNames(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<String> ListOfExpensesNames = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM Expenses WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfExpensesNames.add(c.getString(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfExpensesNames;
    }

     ArrayList<Double> getExpensesAmount(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<Double> ListOfExpensesAmounts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Expenses + " WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfExpensesAmounts.add(c.getDouble(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfExpensesAmounts;
    }

     ArrayList<String> getExpensesDates(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<String> ListOfExpensesDates = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT date FROM " + TABLE_Expenses + " WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfExpensesDates.add(c.getString(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfExpensesDates;
    }


    void AddIncome(String Name, Double Amount, int Category, int Subcategory, String hour, String Date)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,Name);
        values.put(KEY_Amount,Double.toString(Amount));
        values.put(KEY_Category,Integer.toString(Category));
        values.put(KEY_Subcategory,Integer.toString(Subcategory));
        values.put(KEY_hour,hour);
        values.put(KEY_date,Date);

        // Inserting Row
        db.insert(TABLE_Income, null, values);
        db.close(); // Closing database connection

    }
     ArrayList<String> getIncomeNames(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<String> ListOfIncomeNames = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM Income WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfIncomeNames.add(c.getString(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfIncomeNames;
    }

     ArrayList<Double> getIncomeAmounts(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<Double> ListOfIncomeAmounts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Income + " WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfIncomeAmounts.add(c.getDouble(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfIncomeAmounts;
    }

     ArrayList<String> getIncomeDates(int _IdCategory, int _IdSubcategory)
    {
        ArrayList<String> ListOfIncomeDates = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT date FROM " + TABLE_Income + " WHERE Subcategory = " + Integer.toString(_IdSubcategory) + " AND Category = " + Integer.toString(_IdCategory), null);

        if(c.moveToFirst()){
            do{
                ListOfIncomeDates.add(c.getString(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfIncomeDates;
    }

    String[] getLastExpenses()
    {
        String LastExpenses[] = new String[10];
        double AmountLastExpenses[] = new double[10];
        String bufor;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name, Amount FROM " + TABLE_Expenses + " ORDER BY date DESC, hour DESC LIMIT 10", null);
        int a = 0;

        if(c.moveToFirst()){
            do{
                bufor = c.getString(0);
                AmountLastExpenses[a] = c.getDouble(1);

                LastExpenses[a] = Integer.toString(a+1) + ". " + bufor + " " + Double.toString(AmountLastExpenses[a]) + "zł";
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();


        return LastExpenses;

    }

     String[] getLastIncome()
    {
        String LastIncome[] = new String[10];
        double AmountLastIncome[] = new double[10];
        String bufor;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name, Amount FROM " + TABLE_Income + " ORDER BY date DESC, hour DESC LIMIT 10", null);
        int a = 0;

        if(c.moveToFirst()){
            do{
                bufor = c.getString(0);
                AmountLastIncome[a] = c.getDouble(1);

                LastIncome[a] = Integer.toString(a+1) + ". " + bufor + " " + Double.toString(AmountLastIncome[a]) + "zł";
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();


        return LastIncome;

    }


    double WalletRecalculate()
    {
        double Income =0, Expenses=0;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Expenses, null);


        if(c.moveToFirst()){
            do{
                Expenses += c.getDouble(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Income, null);


        if(c.moveToFirst()){
            do{
                Income += c.getDouble(0);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Income-Expenses;

    }

     void WalletSetBalance(Double Amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("Amount", Amount);

        db.replaceOrThrow(TABLE_Wallet, null, values);

        db.close(); // Closing database connection
    }

     double getWalletBalance()
    {
        double Amount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM "+TABLE_Wallet + " WHERE id = " + Integer.toString(1), null);

        if(c.moveToFirst()){
            do{
                Amount = c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Amount;
    }


     ArrayList<String> getCategory(int RodzKat)
    {
        ArrayList<String> ListOfCategoryName = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name, Type FROM " + TABLE_Category, null);

        int a = 1;
        ListOfCategoryName.add("0. (Domyślna Category)");
        if(c.moveToFirst()){
            do{
                if(RodzKat == c.getInt(1))
                {
                    ListOfCategoryName.add(Integer.toString(a)+". " + c.getString(0));
                    a++;
                }
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  ListOfCategoryName;
    }

     ArrayList<Integer> getIdListOfCategory(int Type)
    {
        ArrayList<Integer> idCategory = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id, Type FROM " + TABLE_Category, null);
        idCategory.add(0);

        if(c.moveToFirst()){
            do{
                if(Type == c.getInt(1))
                {
                    idCategory.add(c.getInt(0));
                                }
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  idCategory;
    }

     void AddCategory(String Name, int RodzKat)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,Name);
        values.put(KEY_Type, RodzKat);
        // Inserting Row
        db.insert(TABLE_Category, null, values);

        db.close(); // Closing database connection

    }

     void RemoveCategory(int idCategory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Category,"id=?",new String[]{Integer.toString(idCategory)});
        db.close(); // Closing database connection

        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM "+ TABLE_Expenses + " WHERE " + KEY_Category + " = " + Integer.toString(idCategory), null);

        if(c.moveToFirst()){
            do{
                setDefaultCategoryAndSubcategory(c.getInt(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

    }

     String getCategoryName(int aID)
    {
        String aName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM " + TABLE_Category + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aName = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aName;
    }

     int getTypeOfCategory(int aID)
    {
        int KatExpensesIncome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT " + KEY_Type + " FROM " + TABLE_Category + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                KatExpensesIncome = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return KatExpensesIncome;
    }


     void UpdateCategory(String Name, int idCategory, int Type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name, Name);
        values.put(KEY_Type, Type);

        db.update(TABLE_Category, values, "id="+idCategory, null);
        db.close(); // Closing database connection
    }

     void AddSubcategory(String Name, int idCategory)
    {
        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_Category, idCategory);
        values.put(KEY_Name,Name);

        // Inserting Row
        db.insert(TABLE_Subcategory, null, values);
        db.close(); // Closing database connection

    }

     void RemoveSubcategory(int idSubcategory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Subcategory,"id=?",new String[]{Integer.toString(idSubcategory)});
        db.close(); // Closing database connection
    }

     String getSubcategoryName(int aID)
    {
        String aName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM "+TABLE_Subcategory + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aName = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aName;
    }

     ArrayList<Integer> getIdListOfSubcategory(int idCategory)
    {
        ArrayList<Integer> IdListOfSubcategory = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_Subcategory + " WHERE " + KEY_Category + " = " + Integer.toString(idCategory), null);


        IdListOfSubcategory.add(0);
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                IdListOfSubcategory.add(c.getInt(0));

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  IdListOfSubcategory;
    }

     ArrayList<String> getNameListOfSubcategory(int idCategory)
    {
        ArrayList<String> NameListOfSubcategory = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT " + KEY_Name + " FROM " + TABLE_Subcategory + " WHERE " + KEY_Category + " = " + Integer.toString(idCategory), null);

        int a = 0;
        NameListOfSubcategory.add("0. (Domyślna Subcategory)");
        if(c.moveToFirst()){
            do{
                NameListOfSubcategory.add(Integer.toString(a+1)+". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  NameListOfSubcategory;
    }

     void updateSubcategoryName(String name, int aID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Name",name); //These Fields should be your String values of actual column names


        db.update(TABLE_Subcategory, cv, "id="+aID, null);
        db.close(); // Closing database connection

       // UPDATE Subcategory SET Name = "dupa" WHERE ID = 1;
    }

     ArrayList<String> getNameListOfCyclilcalExpenses()
    {
        ArrayList<String> NameListOfCyclilcalExpenses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM " + TABLE_Cyclical_Expenses, null);

        int a = 0;
        if(c.moveToFirst()){
            do{
                NameListOfCyclilcalExpenses.add(Integer.toString(a)+ ". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  NameListOfCyclilcalExpenses;
    }

     ArrayList<Integer> getIdListOfCyclicalExpenses()
    {
        ArrayList<Integer> IdListOfCyclicalExpenses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_Cyclical_Expenses, null);
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                IdListOfCyclicalExpenses.add(c.getInt(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  IdListOfCyclicalExpenses;
    }

     void addCyclicalExpenses(String aName, double aAmount, int aCategory, int aSubcategory, String aOdKiedy,
                                String aDoKiedy, String aNastepnaData, FREQUENCY czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,aName);
        values.put(KEY_Amount,aAmount);
        values.put(KEY_Category,Integer.toString(aCategory));
        values.put(KEY_Subcategory,Integer.toString(aSubcategory));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_frequency,czest.toString());

        // Inserting Row
        db.insert(TABLE_Cyclical_Expenses, null, values);
        db.close(); // Closing database connection


    }

     String getCyclicalExpensesName(int aID)
    {
        String aName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM "+TABLE_Cyclical_Expenses + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aName = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aName;
    }

     String getCyclicalExpensesOD(int aID)
    {
        String od_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT od_kiedy FROM "+TABLE_Cyclical_Expenses + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                od_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return od_kiedy;
    }

    String getCyclicalExpensesDO(int aID)
    {
        String do_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT do_kiedy FROM "+TABLE_Cyclical_Expenses + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                do_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return do_kiedy;
    }

     String getCyclicalExpensesNastepnaData(int aID)
    {
        String nastepna_data = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nastepna_data FROM "+TABLE_Cyclical_Expenses + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                nastepna_data = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return nastepna_data;
    }

     int getCyclicalExpensesCategory (int aID)
    {
        int Category = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_Category + " FROM "+ TABLE_Cyclical_Expenses + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Category = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Category;
    }

     void UpdateCyclicalExpenses(int aID, String aName, double aAmount, int aCategory, int aSubcategory, String aOdKiedy,
                          String aDoKiedy, String aNastepnaData, FREQUENCY czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,aName);
        values.put(KEY_Amount,aAmount);
        values.put(KEY_Category,Integer.toString(aCategory));
        values.put(KEY_Subcategory,Integer.toString(aSubcategory));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_frequency,czest.toString());

        // Inserting Row
        db.update(TABLE_Cyclical_Expenses, values, "id="+aID, null);
        db.close(); // Closing database connection
    }

     void RemoveCyclicalExpenses(int aID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Cyclical_Expenses,"id=?",new String[]{Integer.toString(aID)});
        db.close(); // Closing database connection
    }





     ArrayList<String> getNameListOfCyclicalIncome()
    {
        ArrayList<String> NameListOfCyclicalIncome = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM " + TABLE_Cyclical_Income, null);

        int a = 0;
        if(c.moveToFirst()){
            do{
                NameListOfCyclicalIncome.add(Integer.toString(a)+ ". " + c.getString(0));
                a++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  NameListOfCyclicalIncome;
    }

     ArrayList<Integer> getIdListOfCyclicalIncome()
    {
        ArrayList<Integer> IdListOfCyclicalIncome = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT id FROM " + TABLE_Cyclical_Income, null);
        if(c.moveToFirst()){
            do{
                //  bufor = c.getInt(0);
                IdListOfCyclicalIncome.add(c.getInt(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return  IdListOfCyclicalIncome;
    }

     void addCyclicalIncome(String aName, double aAmount, int aCategory, int aSubcategory, String aOdKiedy,
                                String aDoKiedy, String aNastepnaData, FREQUENCY czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,aName);
        values.put(KEY_Amount,aAmount);
        values.put(KEY_Category,Integer.toString(aCategory));
        values.put(KEY_Subcategory,Integer.toString(aSubcategory));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_frequency,czest.toString());

        // Inserting Row
        db.insert(TABLE_Cyclical_Income, null, values);
        db.close(); // Closing database connection


    }

     String getCyclicalIncomeName(int aID)
    {
        String aName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Name FROM "+TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                aName = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return aName;
    }

     double getCyclicalIncomeAmount (int aID)
    {
        double Amount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM "+ TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Amount = c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Amount;
    }

     String getCyclicalIncomeOD(int aID)
    {
        String od_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT od_kiedy FROM "+TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                od_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return od_kiedy;
    }

     String getCyclicalIncomeDO(int aID)
    {
        String do_kiedy = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT do_kiedy FROM "+TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                do_kiedy = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return do_kiedy;
    }

     String getCyclicalIncomeNastepnaData(int aID)
    {
        String nastepna_data = "";
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT nastepna_data FROM "+TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                nastepna_data = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return nastepna_data;
    }

     int getCyclicalIncomeCategory (int aID)
    {
        int Category = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_Category + " FROM "+ TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Category = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Category;
    }

     int getCyclicalIncomeSubcategory (int aID)
    {
        int Subcategory = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_Subcategory + " FROM "+ TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                Subcategory = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Subcategory;
    }

     int getCyclicalIncomeFrequency (int aID)
    {
        String buffor = "";

        int frequency;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT "+ KEY_frequency + " FROM "+ TABLE_Cyclical_Income + " WHERE id = " + Integer.toString(aID), null);

        if(c.moveToFirst()){
            do{
                buffor = c.getString(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        switch(buffor)
        {
            case "DAILY":
                frequency = 0;
                break;

            case "WEEKLY":
                frequency = 1;
                break;

            case "MONTHLY":
                frequency = 2;
                break;

            case "QUARTERLY":
                frequency = 3;
                break;

            case "YEARLY":
                frequency = 4;
                break;

            default:
                frequency = 0;
                break;

        }

        return frequency;
    }

     void UpdateCyclicalIncome(int aID, String aName, double aAmount, int aCategory, int aSubcategory, String aOdKiedy,
                          String aDoKiedy, String aNastepnaData, FREQUENCY czest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Name,aName);
        values.put(KEY_Amount,aAmount);
        values.put(KEY_Category,Integer.toString(aCategory));
        values.put(KEY_Subcategory,Integer.toString(aSubcategory));
        values.put(KEY_odkiedy,aOdKiedy);
        values.put(KEY_dokiedy,aDoKiedy);
        values.put(KEY_nastepnaData,aNastepnaData);
        values.put(KEY_frequency,czest.toString());

        // Inserting Row
        db.update(TABLE_Cyclical_Income, values, "id="+aID, null);
        db.close(); // Closing database connection
    }

     void RemoveCyclicalIncome(int aID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Cyclical_Income,"id=?",new String[]{Integer.toString(aID)});
        db.close(); // Closing database connection
    }

     void CheckCyclicalExpenses()
    {
        Calendar AktualnaData = Calendar.getInstance();
        Calendar dataDoKiedy = Calendar.getInstance();
        //aktualna data
        int YEAR_N = AktualnaData.get(Calendar.YEAR);
        int DAY_N = AktualnaData.get(Calendar.DAY_OF_MONTH);
        int MONTH_N = AktualnaData.get(Calendar.MONTH) +1;

        // data pobierana z bazy
        int YEAR, DAY, MONTH;

        int ID;

        double  Amount;

        int     Category,
                Subcategory;


        String  aName,
                aOdKiedy,
                aDoKiedy,
                aNastepnaData,
                frequency;




        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT " + KEY_ID + "," + KEY_Name + "," + KEY_Amount + "," + KEY_Category + "," + KEY_Subcategory
                + "," + KEY_odkiedy + "," + KEY_dokiedy + "," + KEY_nastepnaData + "," + KEY_frequency +
                " FROM " + TABLE_Cyclical_Expenses, null);

        if(c.moveToFirst()){
            do {
                ID = c.getInt(0);
                aName = c.getString(1);
                Amount = c.getDouble(2);
                Category = c.getInt(3);
                Subcategory = c.getInt(4);
                aOdKiedy = c.getString(5);
                aDoKiedy = c.getString(6);
                aNastepnaData = c.getString(7);
                frequency = c.getString(8);


                DAY = Integer.parseInt(aNastepnaData.substring(0, 2));
                MONTH = Integer.parseInt(aNastepnaData.substring(3, 5));
                YEAR = Integer.parseInt(aNastepnaData.substring(7, 10));

                switch (frequency)
                {
                    case "DAILY":
                        if (DAY_N >= DAY) {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY + 1) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            } else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    case "WEEKLY":
                        if (DAY_N >= DAY + 7) {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY + 7) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.WEEKLY);
                            } else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    case "MONTHLY":
                        if (MONTH_N >= MONTH) {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH + 1) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            } else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    case "QUARTERLY":
                        if (MONTH_N >= MONTH + 3) {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH + 3) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            } else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    case "YEARLY":
                        if (YEAR_N >= YEAR) {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR + 1);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            } else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }while(c.moveToNext());
        }
        c.close();
        db.close();

    }

     void CheckCyclicalIncome()
    {
        Calendar AktualnaData = Calendar.getInstance();
        Calendar dataDoKiedy = Calendar.getInstance();
        //aktualna data
        int YEAR_N = AktualnaData.get(Calendar.YEAR);
        int DAY_N = AktualnaData.get(Calendar.DAY_OF_MONTH);
        int MONTH_N = AktualnaData.get(Calendar.MONTH) +1;

        // data pobierana z bazy
        int YEAR, DAY, MONTH;

        int ID;

        double  Amount;

        int     Category,
                Subcategory;


        String  aName,
                aOdKiedy,
                aDoKiedy,
                aNastepnaData,
                frequency;




        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT " + KEY_ID + "," + KEY_Name + "," + KEY_Amount + "," + KEY_Category + "," + KEY_Subcategory
                + "," + KEY_odkiedy + "," + KEY_dokiedy + "," + KEY_nastepnaData + "," + KEY_frequency +
                " FROM " + TABLE_Income, null);

        if(c.moveToFirst()){
            do{
                ID = c.getInt(0);
                aName = c.getString(1);
                Amount = c.getDouble(2);
                Category = c.getInt(3);
                Subcategory = c.getInt(4);
                aOdKiedy = c.getString(5);
                aDoKiedy = c.getString(6);
                aNastepnaData = c.getString(7);
                frequency = c.getString(8);


                DAY = Integer.parseInt(aNastepnaData.substring(0, 2));
                MONTH = Integer.parseInt(aNastepnaData.substring(3, 5));
                YEAR = Integer.parseInt(aNastepnaData.substring(7, 10));

                switch(frequency)
                {
                    case "DAILY":
                        if(DAY_N >= DAY)
                        {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY+1) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            }else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                        break;

                    case "WEEKLY":
                        if(DAY_N >= DAY+7)
                        {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY+7) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.WEEKLY);
                            }else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                    break;

                    case "MONTHLY":
                        if(MONTH_N >= MONTH)
                        {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH+1) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            }else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                    break;

                    case "QUARTERLY":
                        if(MONTH_N >= MONTH+3)
                        {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH+3) + "-" + Integer.toString(YEAR);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            }else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                    break;

                    case "YEARLY":
                        if(YEAR_N >= YEAR)
                        {
                            String N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR);
                            AddExpenses(aName, Amount, Category, Subcategory, "00:01", N_data);

                            dataDoKiedy.set(Calendar.YEAR, Integer.parseInt(aDoKiedy.substring(7, 10)));
                            dataDoKiedy.set(Calendar.MONTH, Integer.parseInt(aDoKiedy.substring(3, 5)));
                            dataDoKiedy.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aDoKiedy.substring(0, 2)));

                            if (AktualnaData.getTimeInMillis() >= dataDoKiedy.getTimeInMillis())//Aktualna data jest mniejsza badz rowna dacie koncowej to aktualizuje nastepna date
                            {
                                N_data = Integer.toString(DAY) + "-" + Integer.toString(MONTH) + "-" + Integer.toString(YEAR+1);
                                UpdateCyclicalIncome(ID, aName, Amount, Category, Subcategory, aOdKiedy, aDoKiedy, N_data, FREQUENCY.DAILY);
                            }else // data aktualna jest wieksza od daty koncowej, usuwam Cyclical Expenses
                            {
                                RemoveCyclicalExpenses(ID);
                            }
                        }
                    break;
                }


            }while(c.moveToNext());
        }
        c.close();
        db.close();

    }

     float getHowMuchSpendInSubcategory(int _Subcategory)
    {
        float Amount = 0.0f;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Expenses + " WHERE " + KEY_Subcategory + " = " + Integer.toString(_Subcategory), null);

        if(c.moveToFirst()){
            do{
                Amount += c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Amount;
    }

     float getHowMuchEarnInSubcategory(int _Subcategory)
    {
        float Amount = 0.0f;

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount FROM " + TABLE_Income + " WHERE " + KEY_Subcategory + " = " + Integer.toString(_Subcategory), null);

        if(c.moveToFirst()){
            do{
                Amount += c.getDouble(0);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return Amount;
    }

     float getHowMuchSpendInCategory(int _Category, String _OD, String _DO)
    {
        boolean wszystko = false;
        if(_OD.equals("33-33-3333") && _DO.equals("33-33-3333")) wszystko = true;

        Calendar DataOD = Calendar.getInstance();
        Calendar DataDO = Calendar.getInstance();
        Calendar DataPobrana = Calendar.getInstance();

        DataOD.set(Calendar.YEAR, Integer.parseInt(_OD.substring(7, 10)));
        DataOD.set(Calendar.MONTH, Integer.parseInt(_OD.substring(3, 5)));
        DataOD.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_OD.substring(0, 2)));

        DataDO.set(Calendar.YEAR, Integer.parseInt(_DO.substring(7, 10)));
        DataDO.set(Calendar.MONTH, Integer.parseInt(_DO.substring(3, 5)));
        DataDO.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_DO.substring(0, 2)));



        float Amount = 0.0f;



        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount, date FROM " + TABLE_Expenses + " WHERE " + KEY_Category + " = " + Integer.toString(_Category), null);

        if (!wszystko)
        {
            if (c.moveToFirst()) {
                do {
                    DataPobrana.set(Calendar.YEAR, Integer.parseInt(c.getString(1).substring(7, 10)));
                    DataPobrana.set(Calendar.MONTH, Integer.parseInt(c.getString(1).substring(3, 5)));
                    DataPobrana.set(Calendar.DAY_OF_MONTH, Integer.parseInt(c.getString(1).substring(0, 2)));

                    if (DataPobrana.getTimeInMillis() >= DataOD.getTimeInMillis() && DataPobrana.getTimeInMillis() <= DataDO.getTimeInMillis()) {
                        Amount += c.getDouble(0);
                    }
                } while (c.moveToNext());
            }
        } else {
            if (c.moveToFirst())
            {
                do {
                    Amount += c.getDouble(0);
                } while (c.moveToNext());
            }
        }

        c.close();
        db.close();

        return Amount;
    }



     ArrayList<Integer> getCategoryIdListForExpenses(String _OD, String _DO)//Name Category w ktorych byl utworzony wyadtek w zadanej dacie
    {
        ArrayList<Integer> ListaIDCategory = new ArrayList<>();

        boolean wszystko = false;
        if(_OD.equals("33-33-3333") && _DO.equals("33-33-3333")) wszystko = true;


        Calendar DataOD = Calendar.getInstance();
        Calendar DataDO = Calendar.getInstance();
        Calendar DataPobrana = Calendar.getInstance();

        DataOD.set(Calendar.YEAR, Integer.parseInt(_OD.substring(7, 10)));
        DataOD.set(Calendar.MONTH, Integer.parseInt(_OD.substring(3, 5)));
        DataOD.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_OD.substring(0, 2)));

        DataDO.set(Calendar.YEAR, Integer.parseInt(_DO.substring(7, 10)));
        DataDO.set(Calendar.MONTH, Integer.parseInt(_DO.substring(3, 5)));
        DataDO.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_DO.substring(0, 2)));
        int buffor;


        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Category, date FROM " + TABLE_Expenses, null);


        if(!wszystko)
        {
            if (c.moveToFirst()) {
                do {
                    DataPobrana.set(Calendar.YEAR, Integer.parseInt(c.getString(1).substring(7, 10)));
                    DataPobrana.set(Calendar.MONTH, Integer.parseInt(c.getString(1).substring(3, 5)));
                    DataPobrana.set(Calendar.DAY_OF_MONTH, Integer.parseInt(c.getString(1).substring(0, 2)));

                    if (DataPobrana.getTimeInMillis() >= DataOD.getTimeInMillis() && DataPobrana.getTimeInMillis() <= DataDO.getTimeInMillis()) {
                        buffor = c.getInt(0);

                        if (!ListaIDCategory.contains(buffor)) {
                            ListaIDCategory.add(buffor);
                        }
                    }
                } while (c.moveToNext());
            }
        }else
        {
            if (c.moveToFirst()) {
                do {
                        buffor = c.getInt(0);
                        if (!ListaIDCategory.contains(buffor))
                        {
                            ListaIDCategory.add(buffor);
                        }

                } while (c.moveToNext());
            }
        }

        c.close();
        db.close();

        return  ListaIDCategory;
    }


     float getHowMuchEarnInCategory(int _Category, String _OD, String _DO) {
        boolean wszystko = false;
        if (_OD.equals("33-33-3333") && _DO.equals("33-33-3333")) wszystko = true;

        Calendar DataOD = Calendar.getInstance();
        Calendar DataDO = Calendar.getInstance();
        Calendar DataPobrana = Calendar.getInstance();

        DataOD.set(Calendar.YEAR, Integer.parseInt(_OD.substring(7, 10)));
        DataOD.set(Calendar.MONTH, Integer.parseInt(_OD.substring(3, 5)));
        DataOD.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_OD.substring(0, 2)));

        DataDO.set(Calendar.YEAR, Integer.parseInt(_DO.substring(7, 10)));
        DataDO.set(Calendar.MONTH, Integer.parseInt(_DO.substring(3, 5)));
        DataDO.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_DO.substring(0, 2)));




        float Amount = 0.0f;


        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Amount, date FROM " + TABLE_Income + " WHERE " + KEY_Category + " = " + Integer.toString(_Category), null);

        if (!wszystko)
        {
            if (c.moveToFirst()) {
                do {
                    DataPobrana.set(Calendar.YEAR, Integer.parseInt(c.getString(1).substring(7, 10)));
                    DataPobrana.set(Calendar.MONTH, Integer.parseInt(c.getString(1).substring(3, 5)));
                    DataPobrana.set(Calendar.DAY_OF_MONTH, Integer.parseInt(c.getString(1).substring(0, 2)));

                    if (DataPobrana.getTimeInMillis() >= DataOD.getTimeInMillis() && DataPobrana.getTimeInMillis() <= DataDO.getTimeInMillis()) {
                        Amount += c.getDouble(0);
                    }
                } while (c.moveToNext());
            }
        } else {
            if (c.moveToFirst())
            {
                do {
                    Amount += c.getDouble(0);
                } while (c.moveToNext());
            }
        }

        c.close();
        db.close();

        return Amount;
    }



     ArrayList<Integer> getCategoryIdListForIncome(String _OD, String _DO)//Name Category w ktorych byl utworzony wyadtek w zadanej dacie
    {
        boolean wszystko = false;
        if (_OD.equals("33-33-3333") && _DO.equals("33-33-3333")) wszystko = true;

        ArrayList<Integer> ListaIDCategory = new ArrayList<>();

        Calendar DataOD = Calendar.getInstance();
        Calendar DataDO = Calendar.getInstance();
        Calendar DataPobrana = Calendar.getInstance();

        DataOD.set(Calendar.YEAR, Integer.parseInt(_OD.substring(7, 10)));
        DataOD.set(Calendar.MONTH, Integer.parseInt(_OD.substring(3, 5)));
        DataOD.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_OD.substring(0, 2)));

        DataDO.set(Calendar.YEAR, Integer.parseInt(_DO.substring(7, 10)));
        DataDO.set(Calendar.MONTH, Integer.parseInt(_DO.substring(3, 5)));
        DataDO.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_DO.substring(0, 2)));
        int buffor;


        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT Category, date FROM " + TABLE_Income, null);

        if(!wszystko)
        {
            if (c.moveToFirst()) {
                do {
                    DataPobrana.set(Calendar.YEAR, Integer.parseInt(c.getString(1).substring(7, 10)));
                    DataPobrana.set(Calendar.MONTH, Integer.parseInt(c.getString(1).substring(3, 5)));
                    DataPobrana.set(Calendar.DAY_OF_MONTH, Integer.parseInt(c.getString(1).substring(0, 2)));

                    if (DataPobrana.getTimeInMillis() >= DataOD.getTimeInMillis() && DataPobrana.getTimeInMillis() <= DataDO.getTimeInMillis()) {
                        buffor = c.getInt(0);

                        if (!ListaIDCategory.contains(buffor)) {
                            ListaIDCategory.add(buffor);
                        }
                    }
                } while (c.moveToNext());
            }
        }else
        {
            if (c.moveToFirst()) {
                do {
                    buffor = c.getInt(0);
                    if (!ListaIDCategory.contains(buffor))
                    {
                        ListaIDCategory.add(buffor);
                    }

                } while (c.moveToNext());
            }
        }


        c.close();
        db.close();

        return  ListaIDCategory;
    }

}
