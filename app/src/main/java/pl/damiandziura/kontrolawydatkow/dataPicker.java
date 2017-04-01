package pl.damiandziura.kontrolawydatkow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import static java.util.Calendar.*;

public class dataPicker extends AppCompatActivity {

    private String buforNazwa = "", buforKwota = "";

    private String Powrot;
    private String DATA, GODZINA;
    private Intent intent;
    private TimePicker czas;
    private DatePicker data;
    private Calendar AktualnaData, dataPobrana;

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
            }else if(Powrot.equals("nowyStalyWydatek1") || Powrot.equals("nowyStalyWydatek2") || Powrot.equals("nowyStalyWydatek3"))
            {
                intent = new Intent(this, dodajStalyWydatek.class);
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

        if(Powrot.equals("nowyPrzychod"))
        {
            sprawdzDateZogarniczeniem();
        }else if(Powrot.equals("nowyWydatek"))
        {
            sprawdzDateZogarniczeniem();
        }else if(Powrot.equals("nowyStalyWydatek1") || Powrot.equals("nowyStalyWydatek2") || Powrot.equals("nowyStalyWydatek3"))
        {
            sprawdzDateBezogarniczenia();
        }

    }

    public void btTeraz(View view)
    {
        AktualnaData = Calendar.getInstance();
        data.updateDate(AktualnaData.get(Calendar.YEAR), AktualnaData.get(Calendar.MONTH), AktualnaData.get(Calendar.DAY_OF_MONTH));
    }

    private void sprawdzDateZogarniczeniem()
    {
        AktualnaData = Calendar.getInstance();
        dataPobrana = Calendar.getInstance();

        dataPobrana.set(YEAR, data.getYear());
        dataPobrana.set(MONTH, data.getMonth());
        dataPobrana.set(DAY_OF_MONTH, data.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getHour());
            dataPobrana.set(MINUTE, czas.getMinute());
        }
        else
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getCurrentHour());
            dataPobrana.set(MINUTE, czas.getCurrentMinute());
        }



        if (AktualnaData.getTimeInMillis() >= dataPobrana.getTimeInMillis())
        {
            DATA = Integer.toString(data.getDayOfMonth()) + "-" + Integer.toString(data.getMonth()+1) + "-" + Integer.toString(data.getYear());
            String minuty, godziny;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
            {
                if(czas.getMinute() <= 9) {
                    minuty = "0" + Integer.toString(czas.getMinute());
                }else minuty = Integer.toString(czas.getMinute());
                if(czas.getHour() <= 9) {
                    godziny = "0" + Integer.toString(czas.getHour());
                }else godziny = Integer.toString(czas.getHour());
            }
            else
            {
                if(czas.getCurrentMinute() <= 9) {
                    minuty = "0" + Integer.toString(czas.getCurrentMinute());
                }else minuty = Integer.toString(czas.getCurrentMinute());
                if(czas.getCurrentHour() <= 9) {
                    godziny = "0" + Integer.toString(czas.getCurrentHour());
                }else godziny = Integer.toString(czas.getCurrentHour());
            }

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
    }
    private void sprawdzDateBezogarniczenia()
    {
        dataPobrana = Calendar.getInstance();
        dataPobrana.set(YEAR, data.getYear());
        dataPobrana.set(MONTH, data.getMonth());
        dataPobrana.set(DAY_OF_MONTH, data.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getHour());
            dataPobrana.set(MINUTE, czas.getMinute());
        }
        else
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getCurrentHour());
            dataPobrana.set(MINUTE, czas.getCurrentMinute());
        }




        DATA = Integer.toString(data.getDayOfMonth()) + "-" + Integer.toString(data.getMonth()+1) + "-" + Integer.toString(data.getYear());
        String minuty, godziny;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            if(czas.getMinute() <= 9) {
                minuty = "0" + Integer.toString(czas.getMinute());
            }else minuty = Integer.toString(czas.getMinute());
            if(czas.getHour() <= 9) {
                godziny = "0" + Integer.toString(czas.getHour());
            }else godziny = Integer.toString(czas.getHour());
        }
        else
        {
            if(czas.getCurrentMinute() <= 9) {
                minuty = "0" + Integer.toString(czas.getCurrentMinute());
            }else minuty = Integer.toString(czas.getCurrentMinute());
            if(czas.getCurrentHour() <= 9) {
                godziny = "0" + Integer.toString(czas.getCurrentHour());
            }else godziny = Integer.toString(czas.getCurrentHour());
        }
            GODZINA = godziny + ":" + minuty;
            intent.putExtra("Data", DATA);
            intent.putExtra("Godzina", GODZINA);
            intent.putExtra("Kwota", buforKwota);
            intent.putExtra("Nazwa", buforNazwa);

            startActivity(intent);
        }




}
