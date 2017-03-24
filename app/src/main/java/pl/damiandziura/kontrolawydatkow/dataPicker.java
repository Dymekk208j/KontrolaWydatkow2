package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class dataPicker extends AppCompatActivity {

    private String buforNazwa = "", buforKwota = "";

    private String Powrot;
    private String DATA, GODZINA;
    private Intent intent;
    private TimePicker czas;
    private DatePicker data;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            buforNazwa = extras.getString("Nazwa");
            buforKwota = extras.getString("Kwota");
            Powrot = extras.getString("Klasa");

            if(Powrot.equals("nowyPrzychod"))
            {
                intent = new Intent(this, nowyPrzychod.class);
            }else if(Powrot.equals("nowyWydatek"))
            {
                intent = new Intent(this, AddNewExpenses.class);
            }
        }


        data = (DatePicker) findViewById(R.id.datePicker2);
        czas = (TimePicker) findViewById(R.id.timePicker3);
        czas.setIs24HourView(true);
    }

    public void btCofnij(View view) {
        startActivity(intent);
    }

    public void btZatwierdz(View view)
    {
        c = Calendar.getInstance();

        if(data.getYear() <= c.get(Calendar.YEAR) &&
           data.getMonth() <= c.get(Calendar.MONTH)+1 &&
           data.getDayOfMonth() <= c.get(Calendar.DAY_OF_MONTH) &&
            czas.getCurrentHour() <= c.get(Calendar.HOUR)  &&
            czas.getCurrentMinute() <= c.get(Calendar.MINUTE))
        {
            DATA = Integer.toString(data.getDayOfMonth()) + "-" + Integer.toString(data.getMonth()+1) + "-" + Integer.toString(data.getYear());
            String minuty, godziny;

            if(czas.getCurrentMinute() <= 9) {
                minuty = "0" + Integer.toString(czas.getCurrentMinute());
            }else minuty = Integer.toString(czas.getCurrentMinute());
            if(czas.getCurrentHour() <= 9) {
                godziny = "0" + Integer.toString(czas.getCurrentHour());
            }else godziny = Integer.toString(czas.getCurrentHour());

            GODZINA = godziny + ":" + minuty;
            intent.putExtra("Data", DATA);
            intent.putExtra("Godzina", GODZINA);
            intent.putExtra("Kwota", buforKwota);
            intent.putExtra("Nazwa", buforNazwa);

            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Data nie może być z przyszłości!", Toast.LENGTH_SHORT).show();
        }
      //  data.setMaxDate(c.getTimeInMillis());

       // if(data.getMaxDate() < data.getDrawingTime())

       // Toast.makeText(this, DATA+"/"+GODZINA, Toast.LENGTH_SHORT).show();

    }

    public void btTeraz(View view)
    {
        c = Calendar.getInstance();
        data.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }
}
