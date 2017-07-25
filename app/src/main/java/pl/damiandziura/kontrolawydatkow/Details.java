package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import java.util.Random;

public class Details extends AppCompatActivity {

    private Intent intent;
    private int Category_ID;
    private PieChart pieChart;
    private Database database;
    private ArrayList<Integer> colors;
    private Random r = new Random();
    boolean IncomeExpanse = false; //0 wydatek, 1 dochod

    private ArrayList<Integer> ExpenseIDs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int Low = 0;
        int High = 255;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        database = new Database(this);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            IncomeExpanse = extras.getBoolean("IncomeExpanse");
            Category_ID = extras.getInt("CategoryID");
        }

        String sIdCategory;
        if(Category_ID != 0) {
            sIdCategory = database.getCategoryName(Category_ID);
        }else{ sIdCategory = getResources().getString(R.string.str_domyslna_kategoria);}

        setTitle(sIdCategory);



        Description desc = new Description();
        desc.setText("");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);




        colors = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }


            addSubcategoryData();

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    SubcategoryDetails(h);
                }

                @Override
                public void onNothingSelected() {

                }
            });


    }

    private void addSubcategoryData()
    {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> SubcategoriesNames = new ArrayList<>();
        ArrayList<Float> Costs = new ArrayList<>();
        ArrayList<Integer> SubcategoryInt = database.getIdListOfSubcategory(Category_ID);
        ExpenseIDs = new ArrayList<>();
        float buffor;

        for(int a = 0; a < SubcategoryInt.size(); a++)
        {
            if(!IncomeExpanse)
            {
                buffor = database.getHowMuchSpendInSubcategory(SubcategoryInt.get(a));
            }else
            {
                buffor = database.getHowMuchEarnInSubcategory(SubcategoryInt.get(a));
            }

            if(buffor > 0)
            {
                Costs.add(buffor);

                if(SubcategoryInt.get(a) == 0)
                {
                    SubcategoriesNames.add(getResources().getString(R.string.str_domyślna_podkategoria));
                    ExpenseIDs.add(0);
                }
                else
                {
                    SubcategoriesNames.add(database.getSubcategoryName(SubcategoryInt.get(a)));
                    ExpenseIDs.add(SubcategoryInt.get(a));
                }
            }

        }


        for(int i = 0; i < SubcategoriesNames.size(); i++)
        {
            yEntrys.add(new PieEntry(Costs.get(i), SubcategoriesNames.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);



        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public void back(View view) {
        intent = new Intent(this, Informations.class);
        startActivity(intent);
    }

    public void test(View view)
    {
        intent = new Intent(this, ListaWydatkow.class);
        startActivity(intent);
    }

    void SubcategoryDetails(Highlight he)
    {
        intent = new Intent(this, ListaWydatkow.class);
        intent.putExtra("CategoryID", Category_ID);
        int buffor =  ExpenseIDs.get((int)he.getX());
        intent.putExtra("SubCategoryID", buffor);
        intent.putExtra("IncomeExpanse", IncomeExpanse);

        startActivity(intent);
    }




}
