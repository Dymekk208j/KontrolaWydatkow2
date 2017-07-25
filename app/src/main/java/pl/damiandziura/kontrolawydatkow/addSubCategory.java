package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class addSubCategory extends AppCompatActivity {
    private Database database;
    private int CategoryID =0;
    private int SubCategoryID =0;
    private EditText SubCategoryName;
    private String WindowName = "";
    private boolean editing = false;
    private  ArrayList<String> SubCategoryList;
    private String BufforList[];
    private String CategoryName;
    private int IncomeExpanse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subcategory);
        Bundle extras = getIntent().getExtras();
        database = new Database(this);
        SubCategoryName = (EditText) findViewById(R.id.txtSubCategoryName);
        Button btDelete = (Button) findViewById(R.id.btDelete);


        if(extras != null)
        {

            IncomeExpanse = extras.getInt("IncomeExpanse");
            CategoryID = extras.getInt("CategoryID");
            SubCategoryID = extras.getInt("SubcategoryID");
            WindowName = extras.getString("WindowName");
            editing = extras.getBoolean("editing");
            BufforList = extras.getStringArray("subCategoryList");
            CategoryName = extras.getString("CategoryName");

            if(BufforList != null) {
                SubCategoryList = new ArrayList<>(Arrays.asList(BufforList));
            }else{
                SubCategoryList = new ArrayList<>();
            }



        }
        if(SubCategoryID == 0)//dodajesz nowa podkategorie
        {
            btDelete.setVisibility(View.INVISIBLE);

        }
        else//edytujesz istniejaca pod Categories
        {
            if(editing)//edytujesz podkategorie istniejaca w bazie danych
            {
                SubCategoryName.setText(database.getSubcategoryName(SubCategoryID));
            }
            else//edytujesz podkategorie jeszcze nie istniejaca w bazie danych
            {
                SubCategoryName.setText(SubCategoryList.get(SubCategoryID -1));
            }
            btDelete.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(this, "NumerPodKategorii: " + SubCategoryID, Toast.LENGTH_SHORT).show();
        if(!WindowName.equals(""))
        {
            setTitle(WindowName);
        }


    }

    public void back(View view)
    {
        Intent intent = new Intent(this, addCategory.class);
        intent.putExtra("IncomeExpanse", IncomeExpanse);
        intent.putExtra("CategoryID", CategoryID);
        intent.putExtra("CategoryName", CategoryName);

        if(editing) //Dodawanie nowej podkategorii do istniejace kategorii
        {
            intent.putExtra("editing", true);
            intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_kat));
        }
        else
        {
            intent.putExtra("WindowName", getResources().getString(R.string.str_nowa_kategoria));
            intent.putExtra("editing", false);
            intent.putExtra("subCategoryList", BufforList);

        }

        startActivity(intent);

    }

    public void confirm(View view)
    {
        if(editing) //dzialam na istniejacej kategorii
        {
            Intent intent = new Intent(this, addCategory.class);
            intent.putExtra("IncomeExpanse", IncomeExpanse);
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_kat));
            intent.putExtra("editing", true);
            intent.putExtra("CategoryName", CategoryName);

            if(SubCategoryID == 0)//dodaje nowa podkategorie
            {
                if (!SubCategoryName.getText().toString().equals("")) {
                    database.AddSubcategory(SubCategoryName.getText().toString(), CategoryID);
                    Toast.makeText(this, "Dodano nową podkategorie " + SubCategoryName.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }
            }
            else//edytuje istniejaca podkategorie
            {
                if (!SubCategoryName.getText().toString().equals("")) {
                    database.updateSubcategoryName(SubCategoryName.getText().toString(), SubCategoryID);
                    Toast.makeText(this, "Zmienione nazwe podkategorii na " + SubCategoryName.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }

            }
            startActivity(intent);


        }
        else //dzialam na jeszcze nie istniejacej kategorii
        {

            Intent intent = new Intent(this, addCategory.class);
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("WindowName", getResources().getString(R.string.str_nowa_kategoria));
            intent.putExtra("editing", false);
            intent.putExtra("CategoryName", CategoryName);

            if(SubCategoryID == 0)//dodaje nowa podkategorie
            {
                if (!SubCategoryName.getText().toString().equals("")) {
                    SubCategoryList.add(SubCategoryName.getText().toString());
                    BufforList = SubCategoryList.toArray(new String[0]);
                    Toast.makeText(this, getResources().getString(R.string.str_dodana_nowa_podkategorie) + SubCategoryName.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();
                }
            }
            else//edytuje istniejaca podkategorie
            {
                if (!SubCategoryName.getText().toString().equals("")) {
                    SubCategoryList.set(SubCategoryID -1, SubCategoryName.getText().toString());//-1 jest potrzebne po to zebym mogl operowac na tablicy o numerze 0
                    BufforList = SubCategoryList.toArray(new String[0]);
                    Toast.makeText(this, getResources().getString(R.string.str_zmieniono_nazwe_podkat) + SubCategoryName.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();
                }

            }
            intent.putExtra("subCategoryList", BufforList);
            startActivity(intent);


        }
    }

    public void delete(View view)
    {
        if(editing) //dzialam na istniejacej kategorii
        {
            database.RemoveSubcategory(SubCategoryID);
            Toast.makeText(this, getResources().getString(R.string.str_pomyslnie_usunieto_podkat) + " " + database.getSubcategoryName(SubCategoryID), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, addCategory.class);
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_kat));
            intent.putExtra("editing", true);
            intent.putExtra("CategoryName", CategoryName);
            startActivity(intent);

        }
        else // dzialam na jeszcze nie istniejacej kategorii
        {
            Toast.makeText(this, getResources().getString(R.string.str_pomyslnie_usunieto_podkat) + " " + database.getSubcategoryName(SubCategoryID), Toast.LENGTH_SHORT).show();
            SubCategoryList.remove(SubCategoryID -1);
            BufforList = SubCategoryList.toArray(new String[0]);
            Intent intent = new Intent(this, addCategory.class);
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_kat));
            intent.putExtra("editing", false);
            intent.putExtra("CategoryName", CategoryName);
            intent.putExtra("subCategoryList", BufforList);
            startActivity(intent);

        }

    }


}
