package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Informacje extends AppCompatActivity {

    private Intent intent;
    PieChart pieChart, pieChart2;
    private baza_danych BazaDanych;
    private ArrayList<Integer> colors;
    Random r = new Random();
    int Low = 0;
    int High = 255;
    ArrayList<Integer> IdWydatku, IdIncomeu;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Szczegółowe informacje");
        setContentView(R.layout.activity_informacje);

        BazaDanych = new baza_danych(this);

        Description desc = new Description();
        desc.setText(" ");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);
        pieChart.animateXY(1500, 1500);



        colors = new ArrayList<>();
        for(int i = 0; i < BazaDanych.getCategoryMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }



        RysujWykresWydatku(0);
        Spinner SpinnerCzestotliwosc1 = (Spinner) findViewById(R.id.SpinnerCzestotliwosc1);

        ArrayAdapter<CharSequence> czestotliwoscAdapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        czestotliwoscAdapter.setDropDownViewResource(R.layout.spinner_layout);


        SpinnerCzestotliwosc1.setAdapter(czestotliwoscAdapter);
        SpinnerCzestotliwosc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RysujWykresWydatku(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
        
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SzczegolyKategoriiWydatku(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });


        pieChart2 = (PieChart) findViewById(R.id.idPieChart2);
        pieChart2.setRotationEnabled(false);
        pieChart2.setDrawEntryLabels(false);
        pieChart2.setUsePercentValues(true);
        pieChart2.setDrawHoleEnabled(false);
        pieChart2.setDescription(desc);
        pieChart2.animateXY(1500, 1500);

        RysujWykresIncomeu(0);

        pieChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SzczegolyKategoriiIncomeu(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Spinner SpinnerCzestotliwosc2 = (Spinner) findViewById(R.id.SpinnerCzestotliwosc2);

        ArrayAdapter<CharSequence> czestotliwosc2Adapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        czestotliwosc2Adapter.setDropDownViewResource(R.layout.spinner_layout);


        SpinnerCzestotliwosc2.setAdapter(czestotliwosc2Adapter);
        SpinnerCzestotliwosc2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RysujWykresIncomeu(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });



    }

    public void cofnij(View view) {
       intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }


    void SzczegolyKategoriiWydatku(Highlight he)
    {
        intent = new Intent(this, SzczegoloweInformacje.class);
        intent.putExtra("WydatekIncome", false); // wydatek
        int bufor =  IdWydatku.get((int)he.getX());
        intent.putExtra("IdKategorii", bufor);
        startActivity(intent);
    }

    void SzczegolyKategoriiIncomeu(Highlight he)
    {
        intent = new Intent(this, SzczegoloweInformacje.class);
        intent.putExtra("WydatekDochod", true); // wydatek
        intent.putExtra("IdKategorii", IdIncomeu.get((int)he.getX()));
        startActivity(intent);
    }

    private void RysujWykresWydatku(int wybor)
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
        IdWydatku = BazaDanych.getCategoryIdListForExpenses(DataOd, AktualnaData);


        for(int a = 0; a < IdWydatku.size(); a++)
        {
            float bufor = BazaDanych.getHowMuchSpendInCategory(IdWydatku.get(a), DataOd, AktualnaData);
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

                if(IdWydatku.get(a) == 0)
                {
                    NazwyKategorii.add("Domyślna kategoria");
                }
                else
                {
                    NazwyKategorii.add(BazaDanych.getCategoryName(IdWydatku.get(a)));
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
        for(int i = 0; i < BazaDanych.getCategoryMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }


    private void RysujWykresIncomeu(int wybor)
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
        IdIncomeu = BazaDanych.getCategoryIdListForIncome(DataOd, AktualnaData);


        for(int a = 0; a < IdIncomeu.size(); a++)
        {
            float bufor = BazaDanych.getHowMuchEarnInCategory(IdIncomeu.get(a), DataOd, AktualnaData);
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

                if(IdIncomeu.get(a) == 0)
                {
                    NazwyKategorii.add("Domyślna kategoria");
                }
                else
                {
                    NazwyKategorii.add(BazaDanych.getCategoryName(IdIncomeu.get(a)));
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
        for(int i = 0; i < BazaDanych.getCategoryMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }

        pieDataSet.setColors(colors);

        Legend legend = pieChart2.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart2.setData(pieData);
        pieChart2.invalidate();

    }



}
