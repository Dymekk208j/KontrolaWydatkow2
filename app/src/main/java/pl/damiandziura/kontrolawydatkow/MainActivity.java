package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    PieChart pieChart;

    private baza_danych BazaDanych;
    TextView OstatniWydatek[] = new TextView[10];
    String sOstatniWydatek[] = new String[10];

    TextView OstatniDochod[] = new TextView[10];
    String sOstatniDochod[] = new String[10];
    TextView PosiadaneSrodki;
    Random r = new Random();
    int Low = 65;
    int High = 255;
    private Spinner SpinnerCzestotliwosc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BazaDanych = new baza_danych(this);
        super.onCreate(savedInstanceState);
        setTitle("Aplikacja do kontroli wydatków");
        setContentView(R.layout.activity_main);



        BazaDanych.PortfelUstawSaldo(BazaDanych.PortfelPrzeliczSaldo());
        PosiadaneSrodki = (TextView) findViewById(R.id.textView);
        String kwotaa = String.format("%.2f",BazaDanych.getPortfelSaldo());
        PosiadaneSrodki.setText("Saldo konta: " + kwotaa + "zł");

        OstatniWydatek[0] = (TextView) findViewById(R.id.TxtLastExp0);
        OstatniWydatek[1] = (TextView) findViewById(R.id.TxtLastExp1);
        OstatniWydatek[2] = (TextView) findViewById(R.id.TxtLastExp2);
        OstatniWydatek[3] = (TextView) findViewById(R.id.TxtLastExp3);
        OstatniWydatek[4] = (TextView) findViewById(R.id.TxtLastExp4);
        OstatniWydatek[5] = (TextView) findViewById(R.id.TxtLastExp5);
        OstatniWydatek[6] = (TextView) findViewById(R.id.TxtLastExp6);
        OstatniWydatek[7] = (TextView) findViewById(R.id.TxtLastExp7);
        OstatniWydatek[8] = (TextView) findViewById(R.id.TxtLastExp8);
        OstatniWydatek[9] = (TextView) findViewById(R.id.TxtLastExp9);

        OstatniDochod[0] = (TextView) findViewById(R.id.TxtOstatniDochod0);
        OstatniDochod[1] = (TextView) findViewById(R.id.TxtOstatniDochod1);
        OstatniDochod[2] = (TextView) findViewById(R.id.TxtOstatniDochod2);
        OstatniDochod[3] = (TextView) findViewById(R.id.TxtOstatniDochod3);
        OstatniDochod[4] = (TextView) findViewById(R.id.TxtOstatniDochod4);
        OstatniDochod[5] = (TextView) findViewById(R.id.TxtOstatniDochod5);
        OstatniDochod[6] = (TextView) findViewById(R.id.TxtOstatniDochod6);
        OstatniDochod[7] = (TextView) findViewById(R.id.TxtOstatniDochod7);
        OstatniDochod[8] = (TextView) findViewById(R.id.TxtOstatniDochod8);
        OstatniDochod[9] = (TextView) findViewById(R.id.TxtOstatniDochod9);

        sOstatniWydatek = BazaDanych.getOstatnieWydatki();
        sOstatniDochod = BazaDanych.getOstatnieDochody();



        for(int a = 0; a < 10; a++)
        {
            OstatniWydatek[a].setText(sOstatniWydatek[a]);
            OstatniDochod[a].setText(sOstatniDochod[a]);
        }
        Description desc = new Description();
        desc.setText("Wydatki względem kategorii");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);

        RysujWykres(0);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Spinner SpinnerCzestotliwosc = (Spinner) findViewById(R.id.SpinnerCzestotliwosc);

        ArrayAdapter<CharSequence> czestotliwoscAdapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        czestotliwoscAdapter.setDropDownViewResource(R.layout.spinner_layout);


        SpinnerCzestotliwosc.setAdapter(czestotliwoscAdapter);
        SpinnerCzestotliwosc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RysujWykres(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

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

    private void RysujWykres(int wybor)
    {
        boolean wszystko = false;
        Calendar DataDzisiejsza = Calendar.getInstance();
        int DO_DAY = DataDzisiejsza.get(Calendar.DAY_OF_MONTH);
        int DO_MONTH = DataDzisiejsza.get(Calendar.MONTH) +1;
        int DO_YEAR = DataDzisiejsza.get(Calendar.YEAR);



        Calendar DataOdKiedy = Calendar.getInstance();
        int OD_DAY = DataOdKiedy.get(Calendar.DAY_OF_MONTH);
        int OD_MONTH = DataOdKiedy.get(Calendar.MONTH) +1;
        int OD_YEAR = DataOdKiedy.get(Calendar.YEAR);





        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        switch(wybor)
        {
            case 0:
                wszystko = true;
                break;
            case 1://dziennie
                break;
            case 2:
                OD_DAY -= 7;
                break;
            case 3:
                OD_MONTH -=1;
                break;
            case 4:
                OD_MONTH -=3;
                break;
            case 5:
                OD_YEAR -=1;
                break;

        }

        String dzien = Integer.toString(DO_DAY);
        if(DO_DAY <= 9) dzien = "0" + Integer.toString(DO_DAY);

        String miesiac = Integer.toString(DO_MONTH);
        if((DO_MONTH) <= 9) miesiac = "0" + Integer.toString(DO_MONTH);

        String AktualnaData = dzien + "-" + miesiac + "-" + Integer.toString(DO_YEAR);


        dzien = Integer.toString(OD_DAY);
        if(OD_DAY <= 9) dzien = "0" + Integer.toString(OD_DAY);

        miesiac = Integer.toString(OD_MONTH);
        if((OD_MONTH) <= 9) miesiac = "0" + Integer.toString(OD_MONTH);

        String DataOd = dzien + "-" + miesiac + "-" + Integer.toString(OD_YEAR);

        if(wszystko == true)
        {
            DataOd = "33-33-3333";
            AktualnaData = "33-33-3333";
        }
        ArrayList<String> NazwyKategorii = new ArrayList<>();
        ArrayList<Float> WydanePieniadze = new ArrayList<>();
        ArrayList<Integer> intKategorie = BazaDanych.getListaIDKategorii(DataOd, AktualnaData);


        for(int a = 0; a < intKategorie.size(); a++)
        {
            float bufor = BazaDanych.getIleWydanoWkategorii(intKategorie.get(a), DataOd, AktualnaData);
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

                if(intKategorie.get(a) == 0)
                {
                    NazwyKategorii.add("Domyślna kategoria");
                }
                else
                {
                    NazwyKategorii.add(BazaDanych.getKategoriaName(intKategorie.get(a)));
                }
            }

        }


        for(int i = 0; i < NazwyKategorii.size(); i++)
        {
            yEntrys.add(new PieEntry(WydanePieniadze.get(i), NazwyKategorii.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < BazaDanych.getKategoriaMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();

    }


}
