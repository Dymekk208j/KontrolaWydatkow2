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
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    PieChart pieChart;

    private Database database;
    TextView LastExpense[] = new TextView[10];
    String sLastExpense[] = new String[10];

    TextView LastIncome[] = new TextView[10];
    String sLastIncome[] = new String[10];
    TextView money;
    Random r = new Random();
    int Low = 65;
    int High = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = new Database(this);
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.app_name));
        setContentView(R.layout.activity_main);



        database.WalletSetBalance(database.WalletRecalculate());
        money = (TextView) findViewById(R.id.textView);
        String kwotaa = String.format(Locale.getDefault(), "%.2f", database.getWalletBalance());
        money.setText(getResources().getString(R.string.str_saldo) + ": " + kwotaa + getResources().getString(R.string.str_skrot_waluty));

        LastExpense[0] = (TextView) findViewById(R.id.TxtLastExp0);
        LastExpense[1] = (TextView) findViewById(R.id.TxtLastExp1);
        LastExpense[2] = (TextView) findViewById(R.id.TxtLastExp2);
        LastExpense[3] = (TextView) findViewById(R.id.TxtLastExp3);
        LastExpense[4] = (TextView) findViewById(R.id.TxtLastExp4);
        LastExpense[5] = (TextView) findViewById(R.id.TxtLastExp5);
        LastExpense[6] = (TextView) findViewById(R.id.TxtLastExp6);
        LastExpense[7] = (TextView) findViewById(R.id.TxtLastExp7);
        LastExpense[8] = (TextView) findViewById(R.id.TxtLastExp8);
        LastExpense[9] = (TextView) findViewById(R.id.TxtLastExp9);

        LastIncome[0] = (TextView) findViewById(R.id.TxtLastDochod0);
        LastIncome[1] = (TextView) findViewById(R.id.TxtLastDochod1);
        LastIncome[2] = (TextView) findViewById(R.id.TxtLastDochod2);
        LastIncome[3] = (TextView) findViewById(R.id.TxtLastDochod3);
        LastIncome[4] = (TextView) findViewById(R.id.TxtLastDochod4);
        LastIncome[5] = (TextView) findViewById(R.id.TxtLastDochod5);
        LastIncome[6] = (TextView) findViewById(R.id.TxtLastDochod6);
        LastIncome[7] = (TextView) findViewById(R.id.TxtLastDochod7);
        LastIncome[8] = (TextView) findViewById(R.id.TxtLastDochod8);
        LastIncome[9] = (TextView) findViewById(R.id.TxtLastDochod9);

        sLastExpense = database.getLastExpenses();
        sLastIncome = database.getLastIncome();



        for(int a = 0; a < 10; a++)
        {
            LastExpense[a].setText(sLastExpense[a]);
            LastIncome[a].setText(sLastIncome[a]);
        }
        Description desc = new Description();
        desc.setText(getResources().getString(R.string.str_wyd_wzg_kat));

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);

        DrawPie(0);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Spinner SpnrFreq = (Spinner) findViewById(R.id.SpinnerMainFreq);

        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(
                this, R.array.czestotliwosc2, R.layout.spinner_layout);
        freqAdapter.setDropDownViewResource(R.layout.spinner_layout);


        SpnrFreq.setAdapter(freqAdapter);
        SpnrFreq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DrawPie(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

    }

    public void addExpense(View view) {
        intent = new Intent(this, addExpense.class);
        startActivity(intent);
    }

    public void addIncome(View view) {
        intent = new Intent(this, addIncome.class);
        startActivity(intent);
    }

    public void Categories(View view) {
        intent = new Intent(this, Categories.class);
        startActivity(intent);
    }

    public void Informations(View view) {
        intent = new Intent(this, Informations.class);
        startActivity(intent);

    }

    private void DrawPie(int x)
    {
        boolean var = false;
        Calendar dateNow = Calendar.getInstance();
        int DO_DAY = dateNow.get(Calendar.DAY_OF_MONTH);
        int DO_MONTH = dateNow.get(Calendar.MONTH) +1;
        int DO_YEAR = dateNow.get(Calendar.YEAR);



        Calendar DateStart = Calendar.getInstance();
        int OD_DAY = DateStart.get(Calendar.DAY_OF_MONTH);
        int OD_MONTH = DateStart.get(Calendar.MONTH) +1;
        int OD_YEAR = DateStart.get(Calendar.YEAR);





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

        String dateStart = day + "-" + month + "-" + Integer.toString(DO_YEAR);


        day = Integer.toString(OD_DAY);
        if(OD_DAY <= 9) day = "0" + Integer.toString(OD_DAY);

        month = Integer.toString(OD_MONTH);
        if((OD_MONTH) <= 9) month = "0" + Integer.toString(OD_MONTH);

        String DataOd = day + "-" + month + "-" + Integer.toString(OD_YEAR);

        if(var)
        {
            DataOd = "33-33-3333";
            dateStart = "33-33-3333";
        }
        ArrayList<String> CategoryNames = new ArrayList<>();
        ArrayList<Float> Costs = new ArrayList<>();
        ArrayList<Integer> CategoryInt = database.getCategoryIdListForExpenses(DataOd, dateStart);


        for(int a = 0; a < CategoryInt.size(); a++)
        {
            float bufor = database.getHowMuchSpendInCategory(CategoryInt.get(a), DataOd, dateStart);
            if(bufor > 0)
            {
                Costs.add(bufor);

                if(CategoryInt.get(a) == 0)
                {
                    CategoryNames.add(getResources().getString(R.string.str_domyslna_kategoria));
                }
                else
                {
                    CategoryNames.add(database.getCategoryName(CategoryInt.get(a)));
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
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();

    }


}
