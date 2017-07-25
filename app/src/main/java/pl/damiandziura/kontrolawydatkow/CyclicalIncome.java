package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CyclicalIncome extends AppCompatActivity {


    private ArrayList<Integer> idListCyclicalIncome;
    private int CyclicalIncomeID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database database;
        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclical_income);
        setTitle(getResources().getString(R.string.str_lista_cykl_dochodow));
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        database = new Database(this);


        ArrayList<String> list = database.getNameListOfCyclicalIncome();
        idListCyclicalIncome = database.getIdListOfCyclicalIncome();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CyclicalIncomeID = idListCyclicalIncome.get(position);
                editCyclicalIncome();
            }

        });
    }

    private void editCyclicalIncome()
    {
        Intent intent = new Intent(this, addCyclicalIncome.class);
        intent.putExtra("CyclicalIncomeID", CyclicalIncomeID);
        intent.putExtra("editing", true);
        intent.putExtra("WindowName", getResources().getString(R.string.str_edycja_cyklicznego_dochodu));
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, addIncome.class);
        startActivity(intent);
    }

    public void add(View view) {
        Intent intent = new Intent(this, addCyclicalIncome.class);
        intent.putExtra("WindowName", getResources().getString(R.string.str_dodaj_cykliczny_dochod));
        intent.putExtra("editing", false);
        startActivity(intent);
    }


}
