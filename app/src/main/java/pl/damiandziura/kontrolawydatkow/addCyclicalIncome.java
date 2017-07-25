package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class addCyclicalIncome extends AppCompatActivity {

    private Intent intent;
    private Database database;
    private String bufforName = "";
    private String bufforAmount = "";
    private String onBack = "";
    private EditText txtName, txtAmount;
    private TextView txt1, txt2, txt3;
    private String date1 = "", date2 = "", date3 = "";
    private String buforDate = "";
    private Spinner sprFreq, sprCat, sprSubCat;
    private ArrayList<Integer> CategoryIdList, SubCategoryIdList;
    private boolean editing = false;
    private int idCyclicalIncome = 0;

    private int posCategory = 0, posSubCategory = 0, positionFREQUENCY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cyclical_income);
        setTitle(getResources().getString(R.string.str_dodaj_cykliczny_dochod));
        database = new Database(this);

        txtName = (EditText) findViewById(R.id.txtCyclicalIncomeName);
        txtAmount = (EditText) findViewById(R.id.txtCyclicalIncomeAmount);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

        sprFreq = (Spinner) findViewById(R.id.SpinnerMainFreq);
        sprCat = (Spinner) findViewById(R.id.SpinnerCyclicalIncomeCategory);
        sprSubCat = (Spinner) findViewById(R.id.SpinnerCyclicalIncomeSubCateogry);
        Button btDelete = (Button) findViewById(R.id.btCyclicalIncomeDelete);

        btDelete.setVisibility(View.INVISIBLE);

        Calendar c = Calendar.getInstance();
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(c.get(Calendar.MONTH)+1);
        if(c.get(Calendar.MONTH)+1 <= 9) month = "0" + Integer.toString(c.get(Calendar.MONTH)+1);

        date1 = day + "-" + month + "-" + Integer.toString(c.get(Calendar.YEAR));
        date2 = date1;
        date3 = date1;

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            bufforName = extras.getString("Name");
            bufforAmount = extras.getString("Amount");
            onBack = extras.getString("onBack");
            buforDate = extras.getString("Date");
            editing = extras.getBoolean("editing");
            idCyclicalIncome = extras.getInt("idCyclicalIncome");

            if(extras.getString("date1") != null) date1 = extras.getString("date1");
            if(extras.getString("date2") != null) date2 = extras.getString("date2");
            if(extras.getString("date3") != null) date3 = extras.getString("date3");

            posCategory = extras.getInt("CategoryID");
            posSubCategory = extras.getInt("SubCategoryID");
            positionFREQUENCY = extras.getInt("FREQUENCYID");
        }
        if(editing)
        {
            btDelete.setVisibility(View.VISIBLE);
            ArrayList<Integer> CategoryIdList, SubCategoryIdList;
            CategoryIdList = database.getIdListOfCategory(0);

            posCategory = CategoryIdList.indexOf(database.getCyclicalIncomeCategory(idCyclicalIncome));

            SubCategoryIdList = database.getIdListOfSubcategory(CategoryIdList.get(posCategory));



            posSubCategory = SubCategoryIdList.indexOf(database.getCyclicalIncomeSubcategory(idCyclicalIncome));
            positionFREQUENCY = database.getCyclicalIncomeFrequency(idCyclicalIncome);

            bufforName = database.getCyclicalIncomeName(idCyclicalIncome);
            bufforAmount = Double.toString(database.getCyclicalIncomeAmount(idCyclicalIncome));
            setTitle(getResources().getString(R.string.str_edycja_cyklicznego_wydatku));
            date1 = database.getCyclicalIncomeOD(idCyclicalIncome);
            date2 = database.getCyclicalIncomeDO(idCyclicalIncome);
            date3 = database.getCyclicalIncomeNastepnaData(idCyclicalIncome);

        }

        if(bufforName != null) txtName.setText(bufforName);
        if(bufforAmount != null) txtAmount.setText(bufforAmount);

        if(date1 != null) txt1.setText(date1);
        if(date2 != null)txt2.setText(date2);
        if(date3 != null)txt3.setText(date3);

        sprFreq.setSelection(positionFREQUENCY);

        if(onBack != null)
        {
            switch(onBack)
            {
                case "NewCyclicalIncome1":
                    date1 = buforDate;
                    txt1.setText(buforDate);
                    break;

                case "NewCyclicalIncome2":
                    date2 = buforDate;
                    txt2.setText(buforDate);
                    break;

                case "NewCyclicalIncome3":
                    date3 = buforDate;
                    txt3.setText(buforDate);
                    break;
            }
        }
        category();

        sprCat.setSelection(posCategory);


        checkDate();

    }

    public void back(View view) {
        intent = new Intent(this, CyclicalIncome.class);
        startActivity(intent);
        // onBackPressed();
    }

    public void add(View view) {
        checkDate();
        boolean correct = true;

        Calendar cTxt1, cTxt2, cTxt3;
        cTxt1 = Calendar.getInstance();
        cTxt1.set(YEAR, Integer.parseInt(date1.substring(6, 10)));
        cTxt1.set(MONTH, Integer.parseInt(date1.substring(3, 5)));
        cTxt1.set(DAY_OF_MONTH, Integer.parseInt(date1.substring(0, 2)));

        cTxt2 = Calendar.getInstance();
        cTxt2.set(YEAR, Integer.parseInt(date2.substring(6, 10)));
        cTxt2.set(MONTH, Integer.parseInt(date2.substring(3, 5)));
        cTxt2.set(DAY_OF_MONTH, Integer.parseInt(date2.substring(0, 2)));

        cTxt3 = Calendar.getInstance();
        cTxt3.set(YEAR, Integer.parseInt(date3.substring(6, 10)));
        cTxt3.set(MONTH, Integer.parseInt(date3.substring(3, 5)));
        cTxt3.set(DAY_OF_MONTH, Integer.parseInt(date3.substring(0, 2)));

        if(cTxt1.getTimeInMillis() >= cTxt2.getTimeInMillis())
        {
            txt2.setTextColor(ContextCompat.getColor(this, R.color.Red2));
            Toast.makeText(this, getResources().getString(R.string.str_blad_daty_3) + date1, Toast.LENGTH_LONG).show();
            correct = false;
        }else
        {
            txt2.setTextColor(ContextCompat.getColor(this, R.color.normal));
        }


        if(cTxt3.getTimeInMillis() <= cTxt2.getTimeInMillis() &&
                cTxt3.getTimeInMillis() >= cTxt1.getTimeInMillis())
        {
            txt3.setTextColor(ContextCompat.getColor(this, R.color.normal));
        }else
        {
            txt3.setTextColor(ContextCompat.getColor(this, R.color.Red2));
            correct = false;

            if(cTxt1.getTimeInMillis() == cTxt2.getTimeInMillis())
            {
                Toast.makeText(this, getResources().getString(R.string.str_blad_daty_1), Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, getResources().getString(R.string.str_blad_daty_2) + date1 + " " + getResources().getString(R.string.str_do2) + " " + date2, Toast.LENGTH_LONG).show();
        }

        if(txtName.getText().toString().equals(""))
        {
            correct = false;
            Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();

        }

        if(txtAmount.getText().toString().equals("") || Double.parseDouble(txtAmount.getText().toString()) <= 0.0)
        {
            correct = false;
            Toast.makeText(this, getResources().getString(R.string.str_kwota_wieksza_od_0), Toast.LENGTH_SHORT).show();

        }

        if(correct)
        {
            int CATEGORY_SELECTED_ID = CategoryIdList.get(sprCat.getSelectedItemPosition());
            int SUBCATEGORY_SELECTED_ID = SubCategoryIdList.get(sprSubCat.getSelectedItemPosition());
            if(!editing)
            {
                switch (sprFreq.getSelectedItemPosition()) {
                    case 0:
                        database.addCyclicalIncome(txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.DAILY);
                        Toast.makeText(this, getResources().getString(R.string.str_dodano_nowy_cykliczny_dochod) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        database.addCyclicalIncome(txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.WEEKLY);
                        Toast.makeText(this, getResources().getString(R.string.str_dodano_nowy_cykliczny_dochod) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        database.addCyclicalIncome(txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.MONTHLY);
                        Toast.makeText(this, getResources().getString(R.string.str_dodano_nowy_cykliczny_dochod) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        database.addCyclicalIncome(txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.QUARTERLY);
                        Toast.makeText(this, getResources().getString(R.string.str_dodano_nowy_cykliczny_dochod) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        database.addCyclicalIncome(txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.YEARLY);
                        Toast.makeText(this, getResources().getString(R.string.str_dodano_nowy_cykliczny_dochod) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, getResources().getString(R.string.str_blad_dodawania_cykl_doch), Toast.LENGTH_LONG);
                        break;
                }
            }
            else
            {
                switch (sprFreq.getSelectedItemPosition()) {
                    case 0:
                        database.UpdateCyclicalIncome(idCyclicalIncome, txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.DAILY);
                        Toast.makeText(this, getResources().getString(R.string.str_uaktualniono_cyk_doch) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        database.UpdateCyclicalIncome(idCyclicalIncome, txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.WEEKLY);
                        Toast.makeText(this, getResources().getString(R.string.str_uaktualniono_cyk_doch) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        database.UpdateCyclicalIncome(idCyclicalIncome, txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.MONTHLY);
                        Toast.makeText(this, getResources().getString(R.string.str_uaktualniono_cyk_doch) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        database.UpdateCyclicalIncome(idCyclicalIncome, txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.QUARTERLY);
                        Toast.makeText(this, getResources().getString(R.string.str_uaktualniono_cyk_doch) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        database.UpdateCyclicalIncome(idCyclicalIncome, txtName.getText().toString(), Double.parseDouble(txtAmount.getText().toString()), CATEGORY_SELECTED_ID, SUBCATEGORY_SELECTED_ID, date1, date2, date3, Database.FREQUENCY.YEARLY);
                        Toast.makeText(this, getResources().getString(R.string.str_uaktualniono_cyk_doch) + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, getResources().getString(R.string.str_blad_dodawania_cykl_doch), Toast.LENGTH_LONG).show();
                        break;
                }
            }
            intent = new Intent(this, CyclicalIncome.class);
            startActivity(intent);
        }
    }

    public void clean(View view)
    {
        txtName.setText("");
        txtAmount.setText("");
        Calendar c = Calendar.getInstance();
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(c.get(Calendar.MONTH)+1);
        if(c.get(Calendar.MONTH)+1 <= 9) month = "0" + Integer.toString(c.get(Calendar.MONTH)+1);

        date1 = day + "-" + month + "-" + Integer.toString(c.get(Calendar.YEAR));
        date2 = date1;
        date3 = date1;
        txt1.setText(date1);
        txt2.setText(date2);
        txt3.setText(date3);
        posCategory = 0;
        posSubCategory = 0;
        positionFREQUENCY = 0;
        sprFreq.setSelection(positionFREQUENCY);
        sprCat.setSelection(posCategory);
        sprSubCat.setSelection(posSubCategory);

    }

    private void category()    {
        ArrayList<String> list = database.getCategory(0);
        CategoryIdList = database.getIdListOfCategory(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprCat.setAdapter(adapter);
        sprCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }



    private void subCategory()    {

        int category = CategoryIdList.get(sprCat.getSelectedItemPosition());
        ArrayList<String> list = database.getNameListOfSubcategory(category);
        SubCategoryIdList = database.getIdListOfSubcategory(category);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprSubCat.setAdapter(adapter);
        sprSubCat.setSelection(posSubCategory);

    }

    public void SelectDate1(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Amount", txtAmount.getText().toString());
        intent.putExtra("Class", "NewCyclicalIncome1");
        //date3
        intent.putExtra("date1", date1);
        intent.putExtra("date2", date2);
        intent.putExtra("date3", date3);
        intent.putExtra("CategoryID", sprCat.getSelectedItemPosition());
        intent.putExtra("SubCategoryID", sprSubCat.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", sprFreq.getSelectedItemPosition());
        intent.putExtra("IdCyclicalIncome", idCyclicalIncome);
        intent.putExtra("editing", editing);
        startActivity(intent);


    }
    public void SelectDate2(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Amount", txtAmount.getText().toString());
        intent.putExtra("Class", "NewCyclicalIncome2");
        //date3
        intent.putExtra("date1", date1);
        intent.putExtra("date2", date2);
        intent.putExtra("date3", date3);
        intent.putExtra("CategoryID", sprCat.getSelectedItemPosition());
        intent.putExtra("SubCategoryID", sprSubCat.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", sprFreq.getSelectedItemPosition());
        intent.putExtra("IdCyclicalIncome", idCyclicalIncome);
        intent.putExtra("editing", editing);
        startActivity(intent);


    }
    public void SelectDate3(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Amount", txtAmount.getText().toString());
        intent.putExtra("Class", "NewCyclicalIncome3");
        //date3
        intent.putExtra("date1", date1);
        intent.putExtra("date2", date2);
        intent.putExtra("date3", date3);
        intent.putExtra("CategoryID", sprCat.getSelectedItemPosition());
        intent.putExtra("SubCategoryID", sprSubCat.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", sprFreq.getSelectedItemPosition());
        intent.putExtra("IdCyclicalIncome", idCyclicalIncome);
        intent.putExtra("editing", editing);
        startActivity(intent);


    }


    private void checkDate()
    {//DD-MM-YYYY
        Calendar cTxt1, cTxt2, cTxt3;
        cTxt1 = Calendar.getInstance();
        cTxt1.set(YEAR, Integer.parseInt(date1.substring(6, 10)));
        cTxt1.set(MONTH, Integer.parseInt(date1.substring(3, 5)));
        cTxt1.set(DAY_OF_MONTH, Integer.parseInt(date1.substring(0, 2)));

        cTxt2 = Calendar.getInstance();
        cTxt2.set(YEAR, Integer.parseInt(date2.substring(6, 10)));
        cTxt2.set(MONTH, Integer.parseInt(date2.substring(3, 5)));
        cTxt2.set(DAY_OF_MONTH, Integer.parseInt(date2.substring(0, 2)));

        cTxt3 = Calendar.getInstance();
        cTxt3.set(YEAR, Integer.parseInt(date3.substring(6, 10)));
        cTxt3.set(MONTH, Integer.parseInt(date3.substring(3, 5)));
        cTxt3.set(DAY_OF_MONTH, Integer.parseInt(date3.substring(0, 2)));

        if(cTxt1.getTimeInMillis() >= cTxt2.getTimeInMillis())
        {
            txt2.setTextColor(ContextCompat.getColor(this, R.color.Red2));
        }else
        {
            txt2.setTextColor(ContextCompat.getColor(this, R.color.normal));
        }


        if(cTxt3.getTimeInMillis() <= cTxt2.getTimeInMillis() &&
                cTxt3.getTimeInMillis() >= cTxt1.getTimeInMillis())
        {
            txt3.setTextColor(ContextCompat.getColor(this, R.color.normal));
        }else
        {
            txt3.setTextColor(ContextCompat.getColor(this, R.color.Red2));
        }


        //Toast.makeText(this, date1.substring(0, 2) + "-" + date1.substring(3, 5) + "-" + date1.substring(6, 10), Toast.LENGTH_SHORT).show();

    }

    public void delete(View view)
    {
        database.RemoveCyclicalIncome(idCyclicalIncome);
        intent = new Intent(this, CyclicalIncome.class);
        startActivity(intent);
    }
}
