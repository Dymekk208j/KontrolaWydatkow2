package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class addCategory extends AppCompatActivity
{
    private Intent intent;
    private Database database;
    private EditText editName;

    private ArrayList<String> subcategoryList;
    private int CategoryID = 0;
    private ArrayList<Integer> subcategoryIdList;
    private String WindowName = "";
    private  Boolean editing = false;
    private ArrayList<String> SubCategoryList;
    private int IncomeExpanse = 0; //0 - wydatek; 1 dochod
    private RadioButton RadioExpanse;
    private RadioGroup radioIncomeExpanse;
    private String CategoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        database = new Database(this);


        Button btDelete;
        ListView listView ;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_category);
        Bundle extras = getIntent().getExtras();
        btDelete = (Button) findViewById(R.id.btDelete);
        RadioExpanse = (RadioButton) findViewById(R.id.radioExpanse);


        radioIncomeExpanse = (RadioGroup) findViewById(R.id.radioIncomeExpanse);

        radioIncomeExpanse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                test();
            }
        });


        if(extras != null)
        {
            CategoryID = extras.getInt("CategoryID");
            WindowName = extras.getString("WindowName");
            editing = extras.getBoolean("editing");
            CategoryName = extras.getString("CategoryName");
            String Buffor[] = extras.getStringArray("subCategoryList");

            if(Buffor == null)
            {
                SubCategoryList = new ArrayList<>();
                SubCategoryList.add(0, "(" + getResources().getString(R.string.str_domyślna_podkategoria)+ ")");
            }else
            {
                SubCategoryList = new ArrayList<>(Arrays.asList(Buffor));
                SubCategoryList.add(0, "(" + getResources().getString(R.string.str_domyślna_podkategoria)+ ")");
            }

        }
        editName = (EditText) findViewById(R.id.editText);

        if(CategoryName != null)
        {
            editName.setText(CategoryName);
        }




        if(!WindowName.equals("")) setTitle(WindowName);

        listView = (ListView) findViewById(R.id.SubcategoryList);

        if(editing)//editing kategorii
        {
            subcategoryList = database.getNameListOfSubcategory(CategoryID);
            subcategoryList.remove(0);
            subcategoryIdList = database.getIdListOfSubcategory(CategoryID);
            editName.setText(database.getCategoryName(CategoryID));
            btDelete.setVisibility(View.VISIBLE);
            IncomeExpanse = database.getTypeOfCategory(CategoryID);
            if(IncomeExpanse == 0)
            {
                radioIncomeExpanse.check(R.id.radioExpanse);


            }else
            {
                radioIncomeExpanse.check(R.id.radioIncome);
            }


        }else
        {
            subcategoryList = SubCategoryList;
            subcategoryList.remove(0);
            btDelete.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, subcategoryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                    editSubcategory(position);


            }

        });

    }

    public void back(View view) {
       intent = new Intent(this, Categories.class);
       startActivity(intent);
    }

    public void confirm(View view)
    {
        if(editing)
        {
            database.UpdateCategory(editName.getText().toString(), CategoryID, IncomeExpanse);
            Toast.makeText(this, getResources().getString(R.string.str_kategoria) + editName.getText().toString() + " " + getResources().getString(R.string.str_zostala_zaktualizowana) + ".", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Categories.class);
            startActivity(intent);
        }
        else
        {
            database.AddCategory(editName.getText().toString(), IncomeExpanse);
            for(int a = 0; a < subcategoryList.size(); a++)
            {
                database.AddSubcategory(subcategoryList.get(a), database.getCategoryMaxId());
            }
            Toast.makeText(this, getResources().getString(R.string.str_nowa_kategoria) + editName.getText().toString() + " " + getResources().getString(R.string.str_zostala_dodana) + ".", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Categories.class);
            startActivity(intent);
        }

    }

    public void delete(View view)
    {
        Toast.makeText(this, getResources().getString(R.string.str_pomyslnie_usunieto_kategorie) + database.getCategoryName(CategoryID), Toast.LENGTH_SHORT).show();
        subcategoryIdList = database.getIdListOfSubcategory(CategoryID);
        for(int a = 0; a < subcategoryIdList.size(); a++)
        {
            database.RemoveSubcategory(subcategoryIdList.get(a));
        }
        database.RemoveCategory(CategoryID);
        intent = new Intent(this, Categories.class);
        startActivity(intent);

    }

    public void addSubcategory(View view)
    {
        Intent intent = new Intent(this, addSubCategory.class);

        intent.putExtra("IncomeExpanse", IncomeExpanse);

        if(editing)
        {
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("SubcategoryID", 0);
            intent.putExtra("WindowName", getResources().getString(R.string.str_dodaj_podk));
            intent.putExtra("editing", true);
            intent.putExtra("CategoryName", editName.getText().toString());

        }
        else
        {
            intent.putExtra("CategoryID", 0);
            intent.putExtra("SubcategoryID", 0);
            String subcatlist[] = SubCategoryList.toArray(new String[0]);
            intent.putExtra("WindowName", "Edytuj podkategorie");
            intent.putExtra("CategoryName", editName.getText().toString());
            intent.putExtra("subCategoryList", subcatlist);
            intent.putExtra("editing", false);
        }
        startActivity(intent);


    }

    public void editSubcategory(int SubCategoryID)
    {
        if(editing)
        {
            Intent intent = new Intent(this, addSubCategory.class);
            intent.putExtra("IncomeExpanse", IncomeExpanse);
            intent.putExtra("CategoryID", CategoryID);
            intent.putExtra("SubcategoryID", subcategoryIdList.get(SubCategoryID+1));
            intent.putExtra("WindowName", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("CategoryName", editName.getText().toString());
            intent.putExtra("editing", true);
            startActivity(intent);
        }else
        {
            int buffor = SubCategoryID+1;
            Intent intent = new Intent(this, addSubCategory.class);
            intent.putExtra("IncomeExpanse", IncomeExpanse);
            intent.putExtra("CategoryID", 0);
            intent.putExtra("SubcategoryID", buffor);
            intent.putExtra("WindowName", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("CategoryName", editName.getText().toString());
            intent.putExtra("editing", false);
            String listapodkat[] = SubCategoryList.toArray(new String[0]);
            intent.putExtra("subCategoryList", listapodkat);

            startActivity(intent);
        }

    }

    private void test()
    {

        if(RadioExpanse.isChecked())
        {
            IncomeExpanse = 0;
        }else
        {
            IncomeExpanse = 1;
        }


    }


}
