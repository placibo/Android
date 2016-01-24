package com.abhi_shri.placebo.giveandtake;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    String URL="content://contacts/people";
    String displayName= "display_name";
    String action="com.android.giveandtake.action.display";
    static String name="Name";
    String value;
    Uri contactUri=Uri.parse(URL);
    String DEAFULT_VALUE="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI,null,null,null,displayName);

        Cursor cursor1 = managedQuery(GiveAndTakeStatusProvider.CONTENT_URI,null,null,null,null);
        HashMap<String,String> GTdatabase=new HashMap<String,String>();
        HashMap<String,String> ContactDatabase=new HashMap<String,String>();

        if(cursor1.moveToFirst()){
            do{
                GTdatabase.put(cursor1.getString(cursor1.getColumnIndex(GiveAndTakeStatusProvider.tableColumnOneName)),"1");
            }while(cursor1.moveToNext());
        }

        String[] contactDetailsArray=new String[cursor.getCount()];
        int counter=0;

        // putting Data to array
        if(cursor.moveToFirst()){
            do{
                if(ContactDatabase.containsKey(cursor.getString(cursor.getColumnIndex(displayName))))
                    continue;
                contactDetailsArray[counter++]=cursor.getString(cursor.getColumnIndex(displayName));
                ContactDatabase.put(cursor.getString(cursor.getColumnIndex(displayName)),"1");

                if(!GTdatabase.containsKey(cursor.getString(cursor.getColumnIndex(displayName)))){
                    System.out.println("INSERT : " + cursor.getString(cursor.getColumnIndex(displayName)));
                    ContentValues values=new ContentValues();

                    values.put(GiveAndTakeStatusProvider.tableColumnOneName,cursor.getString(cursor.getColumnIndex(displayName)));
                    values.put(GiveAndTakeStatusProvider.tableColumnTwoName,DEAFULT_VALUE);
                    values.put(GiveAndTakeStatusProvider.tableColumnThreeName,DEAFULT_VALUE);

                    getContentResolver().insert(GiveAndTakeStatusProvider.CONTENT_URI,values);
                }else{
                    System.out.println("ALREADY INSERT : "+cursor.getString(cursor.getColumnIndex(displayName)));
                    //Delete Operation Will Be Implemented
                }
            }while(cursor.moveToNext());
        }

        String[] temp=new String[counter];
        for(int i=0;i<counter;i++){
            temp[i]=contactDetailsArray[i];
        }
        contactDetailsArray=temp;

//        contactDetailsArray=ContactDatabase.keySet().toArray(contactDetailsArray);
        // A hack to fill data in case of version 9.0

        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,R.layout.contact_list_view,contactDetailsArray);

        ListView listView=(ListView)findViewById(R.id.contacts);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setAction(action);
                value=adapterView.getAdapter().getItem(i).toString();
                intent.putExtra(name,value);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
