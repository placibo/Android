package com.abhi_shri.placebo.giveandtake;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayActivity extends ActionBarActivity {

    EditText give;
    EditText take;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Getting Calling Intent
        Intent intent=getIntent();

        String contactName=intent.getStringExtra(MainActivity.name);

        TextView textView=(TextView)findViewById(R.id.contactName);
        textView.setText(contactName);

        URL=GiveAndTakeStatusProvider.URL+"/"+contactName;

        Cursor cursor = managedQuery(Uri.parse(URL),null,null,null,null);

        give=(EditText)findViewById(R.id.give_edit_text);
        take=(EditText)findViewById(R.id.take_edit_text);

        if(cursor.moveToFirst()){
            give.setText(cursor.getString(cursor.getColumnIndex(GiveAndTakeStatusProvider.tableColumnTwoName)));
            take.setText(cursor.getString(cursor.getColumnIndex(GiveAndTakeStatusProvider.tableColumnThreeName)));
        }
    }


    public void saveData(View view){
        ContentValues values = new ContentValues();
        values.put(GiveAndTakeStatusProvider.tableColumnTwoName,give.getText().toString());
        values.put(GiveAndTakeStatusProvider.tableColumnThreeName,take.getText().toString());

        getContentResolver().update(Uri.parse(URL),values,null,null);
        Toast.makeText(getBaseContext(),"Data Saved",Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
