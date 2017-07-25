package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;



import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    private Database database;
    private ListView listView;
    private ArrayList<Integer> idListCategory;
    private int idCategory = 0;
    private int incomeOrExpense = 0; // 0-wydatek; 1-dochod
    private RadioButton RadioExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle(getResources().getString(R.string.str_lista_kat));
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        RadioExpense = (RadioButton) findViewById(R.id.RadioExpense);

        database = new Database(this);


        RadioGroup radioIncomeExpense = (RadioGroup) findViewById(R.id.RadioGrup);


        radioIncomeExpense.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                test();
            }
        });

        ArrayList<String> list = database.getCategory(incomeOrExpense);
        list.remove(0);

        idListCategory = database.getIdListOfCategory(incomeOrExpense);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idCategory = idListCategory.get(position+1);
                EditCategory();
            }

        });
    }

    private void EditCategory()
    {
        Intent intent = new Intent(this, addCategory.class);
        intent.putExtra("CategoryID", idCategory);
        intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_kat));
        intent.putExtra("editing", true);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addCategory(View view) {
        Intent intent = new Intent(this, addCategory.class);
        intent.putExtra("editing", false);
        intent.putExtra("WindowName", getResources().getString(R.string.str_nowa_kategoria));
        startActivity(intent);
    }

    private void test()
    {

        if(RadioExpense.isChecked())
        {
            incomeOrExpense = 0;
        }else
        {
            incomeOrExpense = 1;
        }

        ArrayList<String> list= database.getCategory(incomeOrExpense);
        list.remove(0);

        idListCategory = database.getIdListOfCategory(incomeOrExpense);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idCategory = idListCategory.get(position+1);
                EditCategory();
            }

        });


    }


}