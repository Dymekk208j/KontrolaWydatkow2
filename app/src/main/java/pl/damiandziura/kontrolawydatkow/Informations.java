package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class Informations extends AppCompatActivity {

    private Intent intent;
    PieChart pieChart, pieChart2;
    private Database database;
    Random r = new Random();
    int Low = 0;
    int High = 255;
    ArrayList<Integer> ExpensesID, IncomeID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.str_szczeg_info));
        setContentView(R.layout.activity_informations);

        database = new Database(this);

        Description desc = new Description();
        desc.setText(" ");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);
        pieChart.animateXY(1500, 1500);



        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < database.getCategoryMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }



        drawExpensesPie(0);
        Spinner spnrFreq = (Spinner) findViewById(R.id.SpinnerFreq);

        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        freqAdapter.setDropDownViewResource(R.layout.spinner_layout);


        spnrFreq.setAdapter(freqAdapter);
        spnrFreq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drawExpensesPie(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ExpensesDetails(h);
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

        DrawIncomesPie(0);

        pieChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                IncomesDetails(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Spinner SpnrFreq2 = (Spinner) findViewById(R.id.SpinnerFreq2);

        ArrayAdapter<CharSequence> Freq2Adapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        Freq2Adapter.setDropDownViewResource(R.layout.spinner_layout);


        SpnrFreq2.setAdapter(Freq2Adapter);
        SpnrFreq2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DrawIncomesPie(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });



    }

    public void back(View view) {
       intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }


    void ExpensesDetails(Highlight he)
    {
        intent = new Intent(this, Details.class);
        intent.putExtra("IncomeExpanse", false); // wydatek
        int buffor =  ExpensesID.get((int)he.getX());
        intent.putExtra("CategoryID", buffor);
        startActivity(intent);
    }

    void IncomesDetails(Highlight he)
    {
        intent = new Intent(this, Details.class);
        intent.putExtra("IncomeExpanse", true); // wydatek
        intent.putExtra("CategoryID", IncomeID.get((int)he.getX()));
        startActivity(intent);
    }

    private void drawExpensesPie(int x)
    {
        boolean var = false;

        Calendar dateNow = Calendar.getInstance();
        int DO_DAY = dateNow.get(Calendar.DAY_OF_MONTH);
        int DO_MONTH = dateNow.get(Calendar.MONTH) +1;
        int DO_YEAR = dateNow.get(Calendar.YEAR);



        Calendar DateTo = Calendar.getInstance();
        int OD_DAY = DateTo.get(Calendar.DAY_OF_MONTH);
        int OD_MONTH = DateTo.get(Calendar.MONTH) +1;
        int OD_YEAR = DateTo.get(Calendar.YEAR);





        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        switch(x)
        {
            case 0:
                var = true;
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

        String day = Integer.toString(DO_DAY);
        if(DO_DAY <= 9) day = "0" + Integer.toString(DO_DAY);

        String month = Integer.toString(DO_MONTH);
        if((DO_MONTH) <= 9) month = "0" + Integer.toString(DO_MONTH);

        String sDateNow = day + "-" + month + "-" + Integer.toString(DO_YEAR);


        day = Integer.toString(OD_DAY);
        if(OD_DAY <= 9) day = "0" + Integer.toString(OD_DAY);

        month = Integer.toString(OD_MONTH);
        if((OD_MONTH) <= 9) month = "0" + Integer.toString(OD_MONTH);

        String startDate = day + "-" + month + "-" + Integer.toString(OD_YEAR);

        if(var)
        {
            startDate = "33-33-3333";
            sDateNow = "33-33-3333";
        }

        ArrayList<String> CategoryNames = new ArrayList<>();
        ArrayList<Float> Costs = new ArrayList<>();
        ExpensesID = database.getCategoryIdListForExpenses(startDate, sDateNow);


        for(int a = 0; a < ExpensesID.size(); a++)
        {
            float buffor = database.getHowMuchSpendInCategory(ExpensesID.get(a), startDate, sDateNow);
            if(buffor > 0)
            {
                Costs.add(buffor);

                if(ExpensesID.get(a) == 0)
                {
                    CategoryNames.add(getResources().getString(R.string.str_domyslna_kategoria));
                }
                else
                {
                    CategoryNames.add(database.getCategoryName(ExpensesID.get(a)));
                }
            }

        }


        for(int i = 0; i < CategoryNames.size(); i++)
        {
            yEntrys.add(new PieEntry(Costs.get(i), CategoryNames.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < database.getCategoryMaxId(); i++)
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


    private void DrawIncomesPie(int x)
    {
        boolean var = false;

        Calendar dateNow = Calendar.getInstance();
        int DO_DAY = dateNow.get(Calendar.DAY_OF_MONTH);
        int DO_MONTH = dateNow.get(Calendar.MONTH) +1;
        int DO_YEAR = dateNow.get(Calendar.YEAR);

        Calendar dateStart = Calendar.getInstance();
        int OD_DAY = dateStart.get(Calendar.DAY_OF_MONTH);
        int OD_MONTH = dateStart.get(Calendar.MONTH) +1;
        int OD_YEAR = dateStart.get(Calendar.YEAR);


        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        switch(x)
        {
            case 0:
                var = true;
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

        String day = Integer.toString(DO_DAY);
        if(DO_DAY <= 9) day = "0" + Integer.toString(DO_DAY);

        String month = Integer.toString(DO_MONTH);
        if((DO_MONTH) <= 9) month = "0" + Integer.toString(DO_MONTH);

        String sNowDate = day + "-" + month + "-" + Integer.toString(DO_YEAR);


        day = Integer.toString(OD_DAY);
        if(OD_DAY <= 9) day = "0" + Integer.toString(OD_DAY);

        month = Integer.toString(OD_MONTH);
        if((OD_MONTH) <= 9) month = "0" + Integer.toString(OD_MONTH);

        String DateStart = day + "-" + month + "-" + Integer.toString(OD_YEAR);

        if(var)
        {
            DateStart = "33-33-3333";
            sNowDate = "33-33-3333";
        }

        ArrayList<String> CategoryNames = new ArrayList<>();
        ArrayList<Float> costs = new ArrayList<>();
        IncomeID = database.getCategoryIdListForIncome(DateStart, sNowDate);


        for(int a = 0; a < IncomeID.size(); a++)
        {
            float buffor = database.getHowMuchEarnInCategory(IncomeID.get(a), DateStart, sNowDate);
            if(buffor > 0)
            {
                costs.add(buffor);

                if(IncomeID.get(a) == 0)
                {
                    CategoryNames.add(getResources().getString(R.string.str_domyslna_kategoria));
                }
                else
                {
                    CategoryNames.add(database.getCategoryName(IncomeID.get(a)));
                }
            }

        }


        for(int i = 0; i < CategoryNames.size(); i++)
        {
            yEntrys.add(new PieEntry(costs.get(i), CategoryNames.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < database.getCategoryMaxId(); i++)
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
