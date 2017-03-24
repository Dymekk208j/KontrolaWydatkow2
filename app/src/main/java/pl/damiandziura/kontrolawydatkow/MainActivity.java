package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void dodajWydatek(View view) {
        intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodajdochod(View view) {
        intent = new Intent(this, nowyPrzychod.class);
        startActivity(intent);
    }

    public void kategorie(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);
    }

    public void Informacje(View view) {
        intent = new Intent(this, Informacje.class);
        startActivity(intent);
    }


}
