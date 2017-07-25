package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CyclicalExpenses extends AppCompatActivity {

    private ArrayList<Integer> idListCyclicalExpenses;
    private int idCyclicalExpenses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database database;
        ListView listView;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclical_expenses);
        setTitle(getResources().getString(R.string.str_lista_cykl_wydatkow));
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        database = new Database(this);


        ArrayList<String> list = database.getNameListOfCyclilcalExpenses();
        idListCyclicalExpenses = database.getIdListOfCyclicalExpenses();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idCyclicalExpenses = idListCyclicalExpenses.get(position);
                editCyclicalExpenses();
            }

        });
    }

    private void editCyclicalExpenses()
    {
        Intent intent = new Intent(this, addCyclicalExpenses.class);
        intent.putExtra("idCyclicalExpenses", idCyclicalExpenses);
        intent.putExtra("editing", true);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, addExpense.class);
        startActivity(intent);
    }

    public void add(View view) {
        Intent intent = new Intent(this, addCyclicalExpenses.class);
        intent.putExtra("editing", false);
        startActivity(intent);
    }



}