package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static java.util.Calendar.*;

public class dataPicker extends AppCompatActivity {

    private String bufforName = "", bufforAmount = "";

    private String onBack;
    private String txtDate, txtHour;
    private Intent intent;
    private TimePicker time;
    private DatePicker date;
    private Calendar nowDate, selectedDate;

    private TextView lblHour, lblDate;
    private String date1 = "", date2 = "", date3 = "";
    private int posCategory = 0;
    private int posSubCategory = 0;
    private int posFreq = 0;
    private boolean editing = false;
    private int IdCyclicalExpenses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            bufforName = extras.getString("Name");
            bufforAmount = extras.getString("Amount");
            onBack = extras.getString("Class");

            date1 = extras.getString("date1");
            date2 = extras.getString("date2");
            date3 = extras.getString("date3");

            posCategory = extras.getInt("CategoryID");
            posSubCategory = extras.getInt("SubCategoryID");
            posFreq = extras.getInt("FREQUENCYID");
            IdCyclicalExpenses = extras.getInt("IdCyclicalExpenses");
            editing = extras.getBoolean("editing");

        }

        lblHour = (TextView) findViewById(R.id.lblGodzina);
        lblDate = (TextView) findViewById(R.id.lblData);
        date = (DatePicker) findViewById(R.id.datePicker2);
        time = (TimePicker) findViewById(R.id.timePicker3);
        time.setIs24HourView(true);

        switch (onBack) {
            case "addIncome":
                intent = new Intent(this, addIncome.class);
                setTitle(getResources().getString(R.string.str_wybierz_date_i_godzine));
                break;
            case "addExpense":
                intent = new Intent(this, addExpense.class);
                setTitle(getResources().getString(R.string.str_wybierz_date_i_godzine));
                break;
            case "NewCyclicalExpenses1":
            case "NewCyclicalExpenses2":
            case "NewCyclicalExpenses3":
                intent = new Intent(this, addCyclicalExpenses.class);
                time.setVisibility(View.INVISIBLE);
                lblHour.setVisibility(View.INVISIBLE);
                setTitle(getResources().getString(R.string.str_wybierz_date));
                break;
            case "NewCyclicalIncome1":
            case "NewCyclicalIncome2":
            case "NewCyclicalIncome3":
                intent = new Intent(this, addCyclicalIncome.class);
                time.setVisibility(View.INVISIBLE);
                lblHour.setVisibility(View.INVISIBLE);
                setTitle(getResources().getString(R.string.str_wybierz_date));
                break;
        }

    }

    public void btBack(View view) {
        onBackPressed();
    }
    
    public void btConfirm(View view)
    {

        switch (onBack) {
            case "addIncome":
                validateDate();
                break;
            case "newExpense":
            case "newIncome":
                validateDate();
                break;
            case "NewCyclicalExpenses1":
            case "NewCyclicalExpenses2":
            case "NewCyclicalExpenses3":
                validateDate2();
                break;
            case "NewCyclicalIncome1":
            case "NewCyclicalIncome2":
            case "NewCyclicalIncome3":
                validateDate2();
                break;
        }

    }

    public void btNow(View view)
    {
        nowDate = Calendar.getInstance();
        date.updateDate(nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DAY_OF_MONTH));
    }

    private void validateDate()
    {
        nowDate = Calendar.getInstance();
        selectedDate = Calendar.getInstance();

        selectedDate.set(YEAR, date.getYear());
        selectedDate.set(MONTH, date.getMonth());
        selectedDate.set(DAY_OF_MONTH, date.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            selectedDate.set(HOUR_OF_DAY, time.getHour());
            selectedDate.set(MINUTE, time.getMinute());
        }
        else
        {
            selectedDate.set(HOUR_OF_DAY, time.getCurrentHour());
            selectedDate.set(MINUTE, time.getCurrentMinute());
        }



        if (nowDate.getTimeInMillis() >= selectedDate.getTimeInMillis())
        {
            String day = Integer.toString(date.getDayOfMonth());
            if(date.getDayOfMonth() <= 9) day = "0" + Integer.toString(date.getDayOfMonth());
            String month = Integer.toString(date.getDayOfMonth()+1);
            if((date.getMonth()+1) <= 9) month = "0" + Integer.toString(date.getMonth()+1);

            txtDate = day + "-" + month + "-" + Integer.toString(date.getYear());
            String minutes, hours;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
            {
                if(time.getMinute() <= 9) {
                    minutes = "0" + Integer.toString(time.getMinute());
                }else minutes = Integer.toString(time.getMinute());
                if(time.getHour() <= 9) {
                    hours = "0" + Integer.toString(time.getHour());
                }else hours = Integer.toString(time.getHour());
            }
            else
            {
                if(time.getCurrentMinute() <= 9) {
                    minutes = "0" + Integer.toString(time.getCurrentMinute());
                }else minutes = Integer.toString(time.getCurrentMinute());
                if(time.getCurrentHour() <= 9) {
                    hours = "0" + Integer.toString(time.getCurrentHour());
                }else hours = Integer.toString(time.getCurrentHour());
            }

            txtHour = hours + ":" + minutes;
            intent.putExtra("Date", txtDate);
            intent.putExtra("Hour", txtHour);
            intent.putExtra("Amount", bufforAmount);
            intent.putExtra("Name", bufforName);

            intent.putExtra("CategoryID", posCategory);
            intent.putExtra("SubCategoryID", posSubCategory);

            intent.putExtra("IdCyclicalExpenses", IdCyclicalExpenses);



            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.str_data_z_przyszlosci), Toast.LENGTH_SHORT).show();
        }
    }

    private void validateDate2()
    {
        selectedDate = Calendar.getInstance();
        selectedDate.set(YEAR, date.getYear());
        selectedDate.set(MONTH, date.getMonth());
        selectedDate.set(DAY_OF_MONTH, date.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            selectedDate.set(HOUR_OF_DAY, time.getHour());
            selectedDate.set(MINUTE, time.getMinute());
        }
        else
        {
            selectedDate.set(HOUR_OF_DAY, time.getCurrentHour());
            selectedDate.set(MINUTE, time.getCurrentMinute());
        }




        String day = Integer.toString(date.getDayOfMonth());
        if(date.getDayOfMonth() <= 9) day = "0" + Integer.toString(date.getDayOfMonth());
        String month = Integer.toString(date.getDayOfMonth()+1);
        if((date.getMonth()+1) <= 9) month = "0" + Integer.toString(date.getMonth()+1);

        txtDate = day + "-" + month + "-" + Integer.toString(date.getYear());
        String minutes, hours;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            if(time.getMinute() <= 9) {
                minutes = "0" + Integer.toString(time.getMinute());
            }else minutes = Integer.toString(time.getMinute());
            if(time.getHour() <= 9) {
                hours = "0" + Integer.toString(time.getHour());
            }else hours = Integer.toString(time.getHour());
        }
        else
        {
            if(time.getCurrentMinute() <= 9) {
                minutes = "0" + Integer.toString(time.getCurrentMinute());
            }else minutes = Integer.toString(time.getCurrentMinute());
            if(time.getCurrentHour() <= 9) {
                hours = "0" + Integer.toString(time.getCurrentHour());
            }else hours = Integer.toString(time.getCurrentHour());
        }
            txtHour = hours + ":" + minutes;
            intent.putExtra("Date", txtDate);
            intent.putExtra("Hour", txtHour);
            intent.putExtra("Amount", bufforAmount);
            intent.putExtra("Name", bufforName);
            intent.putExtra("onBack", onBack);
            intent.putExtra("date1", date1);
            intent.putExtra("date2", date2);
            intent.putExtra("date3", date3);
            intent.putExtra("CategoryID", posCategory);
            intent.putExtra("SubCategoryID", posSubCategory);
            intent.putExtra("FREQUENCYID", posFreq);
            intent.putExtra("IdCyclicalExpenses", IdCyclicalExpenses);
            intent.putExtra("editing", editing);

            startActivity(intent);
        }




}
