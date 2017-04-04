package pl.damiandziura.kontrolawydatkow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    TextView lblGodzina, lblData;
    String data_godzina1 = "", data_godzina2 = "", data_godzina3 = "";
    /*
    intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
     */
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

            data_godzina1 = extras.getString("data_godzina1");
            data_godzina2 = extras.getString("data_godzina2");
            data_godzina3 = extras.getString("data_godzina3");


        }

        lblGodzina = (TextView) findViewById(R.id.lblGodzina);
        lblData = (TextView) findViewById(R.id.lblData);
        data = (DatePicker) findViewById(R.id.datePicker2);
        czas = (TimePicker) findViewById(R.id.timePicker3);
        czas.setIs24HourView(true);

        if(Powrot.equals("nowyPrzychod"))
        {
            intent = new Intent(this, nowyPrzychod.class);
        }else if(Powrot.equals("nowyWydatek"))
        {
            intent = new Intent(this, AddNewExpenses.class);
        }else if(Powrot.equals("nowyStalyWydatek1") || Powrot.equals("nowyStalyWydatek2") || Powrot.equals("nowyStalyWydatek3"))
        {
            intent = new Intent(this, dodajStalyWydatek.class);
            czas.setVisibility(View.INVISIBLE);
            lblGodzina.setVisibility(View.INVISIBLE);
        }else if(Powrot.equals("nowyStalyDochod1") || Powrot.equals("nowyStalyDochod2") || Powrot.equals("nowyStalyDochod3"))
        {
            intent = new Intent(this, dodajStalydochod.class);
            czas.setVisibility(View.INVISIBLE);
            lblGodzina.setVisibility(View.INVISIBLE);
        }

    }

    public void btCofnij(View view) {
        onBackPressed();
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
        }else if(Powrot.equals("nowyStalyDochod1") || Powrot.equals("nowyStalyDochod2") || Powrot.equals("nowyStalyDochod3"))
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
            String dzien = Integer.toString(data.getDayOfMonth());
            if(data.getDayOfMonth() <= 9) dzien = "0" + Integer.toString(data.getDayOfMonth());
            String miesiac = Integer.toString(data.getDayOfMonth()+1);
            if((data.getMonth()+1) <= 9) miesiac = "0" + Integer.toString(data.getMonth()+1);

            DATA = dzien + "-" + miesiac + "-" + Integer.toString(data.getYear());
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

    private void sprawdzDateZogarniczeniem(int aYear, int aMonth, int aDay)
    {
       Calendar DataGraniczna = Calendar.getInstance();
        DataGraniczna.set(YEAR, aYear);
        DataGraniczna.set(MONTH, aMonth);
        DataGraniczna.set(DAY_OF_MONTH, aDay);

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



        if (DataGraniczna.getTimeInMillis() >= dataPobrana.getTimeInMillis())
        {
            String dzien = Integer.toString(data.getDayOfMonth());
            if(data.getDayOfMonth() <= 9) dzien = "0" + Integer.toString(data.getDayOfMonth());
            String miesiac = Integer.toString(data.getDayOfMonth()+1);
            if((data.getMonth()+1) <= 9) miesiac = "0" + Integer.toString(data.getMonth()+1);

            DATA = dzien + "-" + miesiac + "-" + Integer.toString(data.getYear());
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
            Toast.makeText(this, "Data nie może być większa od " + aDay + "-" + aMonth + "-" + aYear, Toast.LENGTH_SHORT).show();
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




        String dzien = Integer.toString(data.getDayOfMonth());
        if(data.getDayOfMonth() <= 9) dzien = "0" + Integer.toString(data.getDayOfMonth());
        String miesiac = Integer.toString(data.getDayOfMonth()+1);
        if((data.getMonth()+1) <= 9) miesiac = "0" + Integer.toString(data.getMonth()+1);

        DATA = dzien + "-" + miesiac + "-" + Integer.toString(data.getYear());
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
            intent.putExtra("Powrot", Powrot);
            intent.putExtra("data_godzina1", data_godzina1);
            intent.putExtra("data_godzina2", data_godzina2);
            intent.putExtra("data_godzina3", data_godzina3);

            startActivity(intent);
        }




}
