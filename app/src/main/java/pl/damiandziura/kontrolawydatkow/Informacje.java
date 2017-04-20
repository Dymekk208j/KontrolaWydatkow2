package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Informacje extends AppCompatActivity {

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacje);

    }

    public void cofnij(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
