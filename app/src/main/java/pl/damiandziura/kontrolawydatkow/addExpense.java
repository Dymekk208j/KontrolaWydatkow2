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

public class addExpense extends AppCompatActivity {

    private Database database;

    private String sName = "", sDate = "", sHour = "";
    private double amount = 0;

    private Intent intent;
    private TextView txtDate;
    private EditText txtName, txtAmount;
    private Spinner SpnrListOfCategory, SpnrListOfSubCategory;
    private ArrayList<Integer> CategoryIdList, SubCategoryIdList;

    private int posCategory = 0, posSubCategory = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        Bundle extras = getIntent().getExtras();

        database = new Database(this);

        txtDate = (TextView) findViewById(R.id.txtDate2);
        txtName = (EditText) findViewById(R.id.txtCyclicalIncomeName);
        txtName.setText(sName);
        txtAmount = (EditText) findViewById(R.id.txtEditAmount2);
        txtAmount.setText("");
        SpnrListOfCategory = (Spinner) findViewById(R.id.SpinnerCategoryExpense);
        SpnrListOfSubCategory = (Spinner) findViewById(R.id.SpinerSubCategoryExpense);
        setTitle(getResources().getString(R.string.str_dodaj_nowy_wydatek));
        setDateAndHour();

        if(extras != null)
        {
            sDate = extras.getString("Date");
            sHour = extras.getString("Hour");
            String a = extras.getString("Amount");
            if(a != null && !a.equals(""))
            {
                amount = Double.parseDouble(a);
            }
            sName = extras.getString("Name");
            posCategory = extras.getInt("CategoryID");
            posSubCategory = extras.getInt("SubCategoryID");
        }

        Category();

        txtAmount.setText("");
        if(amount > 0.0) txtAmount.setText(String.format(Locale.getDefault(), "%.2f", amount));
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

    private void Category()    {
        ArrayList<String> list = database.getCategory(0);
        CategoryIdList = database.getIdListOfCategory(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpnrListOfCategory.setAdapter(adapter);
        SpnrListOfCategory.setSelection(posCategory);

        SpnrListOfCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubCategory(CategoryIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    public void SubCategory(int number)
    {

        ArrayList<String> list = database.getNameListOfSubcategory(number);
        SubCategoryIdList = database.getIdListOfSubcategory(number);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        SpnrListOfSubCategory.setAdapter(adapter);
        SpnrListOfSubCategory.setSelection(posSubCategory);

    }


    public void back(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void CyclicalExpenses(View view) {
        intent = new Intent(this, CyclicalExpenses.class);
        startActivity(intent);
    }

    public void add(View view) {
        sName = txtName.getText().toString();

        if(!sName.equals("") && sName != null)
        {
           if(!txtAmount.getText().toString().equals(""))
           {
               amount = Double.parseDouble(txtAmount.getText().toString());
               if (amount > 0.0) {
                   int ID_SELECTED_CATEGORY = CategoryIdList.get(SpnrListOfCategory.getSelectedItemPosition());
                   int ID_SELECTED_SUBCATEGORY = SubCategoryIdList.get(SpnrListOfSubCategory.getSelectedItemPosition());

                   database.AddExpenses(sName, amount, ID_SELECTED_CATEGORY, ID_SELECTED_SUBCATEGORY, sHour, sDate);

                   Toast.makeText(this, getResources().getString(R.string.str_dodawno_nowy_wydatek) + sName + " " + Double.toString(amount) + getResources().getString(R.string.str_skrot_waluty), Toast.LENGTH_SHORT).show();

                   database.WalletSetBalance(database.WalletRecalculate());

                   intent = new Intent(this, MainActivity.class);

                   startActivity(intent);
               } else Toast.makeText(this, getResources().getString(R.string.str_kwota_wieksza_od_0), Toast.LENGTH_SHORT).show();
           } else Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_kwote), Toast.LENGTH_SHORT).show();

        }else{Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();}
   }

    public void clean(View view)
    {
        sName ="";
        setDateAndHour();
        amount = 0;
        txtDate.setText(sDate + " " + sHour);
        txtName.setText(sName);
        txtAmount.setText("");
        SpnrListOfCategory.setSelection(0);
        SpnrListOfSubCategory.setSelection(0);
    }

    public void SelectDateAndHour(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Amount", txtAmount.getText().toString());
        intent.putExtra("Class", "newExpense");
        intent.putExtra("CategoryID", SpnrListOfCategory.getSelectedItemPosition());
        intent.putExtra("SubCategoryID", SpnrListOfSubCategory.getSelectedItemPosition());
        startActivity(intent);
    }

    private void setDateAndHour()
    {
        Calendar c;
        c = Calendar.getInstance();

        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(c.get(Calendar.MONTH)+1);
        if((c.get(Calendar.MONTH)+1) <= 9) month = "0" + Integer.toString(c.get(Calendar.MONTH)+1);


        sDate = day + "-" + month + "-" + Integer.toString(c.get(Calendar.YEAR));
        sHour = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE));
    }




}
