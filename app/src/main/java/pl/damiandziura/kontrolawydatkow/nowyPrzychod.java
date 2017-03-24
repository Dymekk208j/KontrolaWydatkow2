package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class nowyPrzychod extends AppCompatActivity {
private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowy_przychod);
    }

    public void cofnij(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void dodajStalyDochod(View view) {
        intent = new Intent(this, dodajStalydochod.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
    }

    public void wyczysc(View view) {

    }
}
