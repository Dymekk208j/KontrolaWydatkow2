package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class addIncome extends AppCompatActivity {

    private Database database;

    private String sName = "", sDate = "", sHour = "";
    private double sAmount = 0;

    private Intent intent;
    TextView txtDate;
    EditText txtName, txtAmount;
    Spinner spnrListOfCategory, spnrListOfSubCategory;
    private ArrayList<Integer> CategoryIdList, SubCategoryIdList;

    private int posCategory = 0, posSubcategory = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        Bundle extras = getIntent().getExtras();

        database = new Database(this);

        txtDate = (TextView) findViewById(R.id.txtDate2);
        txtName = (EditText) findViewById(R.id.txtCyclicalIncomeName);
        txtName.setText(sName);
        txtAmount = (EditText) findViewById(R.id.txtEditAmount2);
        txtAmount.setText("");
        spnrListOfCategory = (Spinner) findViewById(R.id.SpinnerCategoryIncome);
        spnrListOfSubCategory = (Spinner) findViewById(R.id.SpinerSubCategoryIncome);
        setDataAndHour();

        // SubCategory();


        if(extras != null)
        {
            sDate = extras.getString("Date");
            sHour = extras.getString("Hour");
            String a = extras.getString("Amount");
            if(a != null && !a.equals(""))
            {
                sAmount = Double.parseDouble(a);
            }
            sName = extras.getString("Name");
            posCategory = extras.getInt("CategoryID");
            posSubcategory = extras.getInt("SubCategoryID");
        }

        category();

        txtAmount.setText("");
        if(sAmount > 0.0) txtAmount.setText(String.format(Locale.getDefault(), "%.2f", sAmount));
        txtName.setText(sName);

        txtDate.setText(sDate + " " + sHour);


        txtAmount.addTextChangedListener(new TextWatcher()
        {
            boolean ignoreChange = false;
            String beforeChange;

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                if (!ignoreChange)
                {
                    String string = charSequence.toString();
                    String[] parts = string.split("\\.");
                    if (parts.length > 1)
                    {
                        String digitsAfterPoint = parts[1];
                        if (digitsAfterPoint.length() > 2)
                        {
                            ignoreChange = true;
                            txtAmount.setText(string.substring(0, string.indexOf(".") + 3));
                            txtAmount.setSelection(txtAmount.getText().length());
                            ignoreChange = false;
                        }
                    }
                    beforeChange = txtAmount.getText().toString();
                }

            }
        });


    }

    private void category()    {
        ArrayList<String> list = database.getCategory(1);
        CategoryIdList = database.getIdListOfCategory(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrListOfCategory.setAdapter(adapter);
        spnrListOfCategory.setSelection(posCategory);

        spnrListOfCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcategory(CategoryIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    public void subcategory(int number)
    {

        ArrayList<String> list = database.getNameListOfSubcategory(number);
        SubCategoryIdList = database.getIdListOfSubcategory(number);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spnrListOfSubCategory.setAdapter(adapter);
        spnrListOfSubCategory.setSelection(posSubcategory);

    }


    public void back(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void CyclicalIncome(View view)
    {
        Intent intent = new Intent(this, CyclicalIncome.class);
        startActivity(intent);
    }

    public void add(View view) {
        sName = txtName.getText().toString();

        if(!sName.equals("") && sName != null)
        {
            if(!txtAmount.getText().toString().equals("") && txtAmount.getText().toString() != null)
            {
                sAmount = Double.parseDouble(txtAmount.getText().toString());
                if (sAmount > 0.0) {
                    int Selected_category_ID = CategoryIdList.get(spnrListOfCategory.getSelectedItemPosition());
                    int Selected_subcategory_id = SubCategoryIdList.get(spnrListOfSubCategory.getSelectedItemPosition());

                    database.AddIncome(sName, sAmount, Selected_category_ID, Selected_subcategory_id, sHour, sDate);

                    Toast.makeText(this, getResources().getString(R.string.str_nowy_dochod) + sName + " " + Double.toString(sAmount) + getResources().getString(R.string.str_skrot_waluty), Toast.LENGTH_SHORT).show();

                    database.WalletSetBalance(database.WalletRecalculate());

                    intent = new Intent(this, MainActivity.class);

                    startActivity(intent);
                } else Toast.makeText(this, getResources().getString(R.string.str_kwota_wieksza_od_0), Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_kwote), Toast.LENGTH_SHORT).show();

        }else{Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();}




        //  Toast.makeText(this, sName+" / "+Double.parseDouble(txtAmount.getText().toString())+" / "+"0"+" / "+"0"+" / "+sHour+" / "+sDate, Toast.LENGTH_SHORT).show();
    }

    public void clean(View view)
    {
        sName ="";
        setDataAndHour();
        sAmount = 0;
        txtDate.setText(sDate + " " + sHour);
        txtName.setText(sName);
        txtAmount.setText("");
        spnrListOfCategory.setSelection(0);
        spnrListOfSubCategory.setSelection(0);

    }

    public void selectDate(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Amount", txtAmount.getText().toString());
        intent.putExtra("Class", "newIncome");
        intent.putExtra("CategoryID", spnrListOfCategory.getSelectedItemPosition());
        intent.putExtra("SubCategoryID", spnrListOfSubCategory.getSelectedItemPosition());
        startActivity(intent);
    }

    private void setDataAndHour()
    {
        Calendar c = Calendar.getInstance();

        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(c.get(Calendar.MONTH)+1);
        if((c.get(Calendar.MONTH)+1) <= 9) month = "0" + Integer.toString(c.get(Calendar.MONTH)+1);


        sDate = day + "-" + month + "-" + Integer.toString(c.get(Calendar.YEAR));
        sHour = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE));
    }




}
