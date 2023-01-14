
package com.merty.manageyourbudget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.merty.manageyourbudget.ui.main.SectionsPagerAdapter;
import com.merty.manageyourbudget.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ListView expensesListView, expensesListView1;

///menu inflate icin
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_expense,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.add_expense){
            //intent
            Intent intent = new Intent(getApplicationContext() , Expenses.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        TabLayout tabs = binding.tabs;

        FloatingActionButton fab = binding.fab;

        expensesListView1 = findViewById(R.id.expensesListView1);

        ArrayList<String>  expenseNameList = new ArrayList<String>();
         ArrayList<String> amountList =  new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,expenseNameList);
        expensesListView1.setAdapter(arrayAdapter);


        String Url ="content://com.merty.manageyourbudget.ContentProvider";
        Uri expenseUri = Uri.parse(Url);

        ContentResolver  contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(expenseUri,null,null,null,"name");
        if (cursor != null){
            while (cursor.moveToNext()){

                expenseNameList.add(cursor.getString(cursor.getColumnIndex(ContentProvider.NAME)));
                amountList.add(ContentProvider.AMOUNT);


            }
        }






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumberDialog(); // Veri girdisi alacak

                ///1
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
        });



    }


    private void showNumberDialog(){
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter a number")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (input.getText().toString().length() > 0) {
                            int number = Integer.parseInt(input.getText().toString());
                            // do something with the number here


                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ContentProvider.NAME, input.getText().toString());
                            contentValues.put(ContentProvider.AMOUNT, number);
                            Uri newUri = getContentResolver().insert(ContentProvider.CONTENT_URI, contentValues);


                            //gerek yok buna galiba
                        //    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                           // startActivity(intent);






                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user cancelled the dialog
                    }
                });
        builder.create().show();
    }

}