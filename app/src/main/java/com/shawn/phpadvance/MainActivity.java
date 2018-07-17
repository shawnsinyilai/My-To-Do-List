package com.shawn.phpadvance;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private TextView no_memo;
    private ListView listView;
    private MyListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAdapter= new DbAdapter(MainActivity.this);
        no_memo=findViewById(R.id.no_memo);
        listView=findViewById(R.id.list_memos);
        if(dbAdapter.listMemos().getCount()==0)
        {
            listView.setVisibility(View.VISIBLE);
            no_memo.setVisibility(View.INVISIBLE);
        }
        else
        {
            listView.setVisibility(View.VISIBLE);
            no_memo.setVisibility(View.INVISIBLE);
        }
        displayList();
    }
    private void displayList(){
        Cursor cursor= dbAdapter.listMemos();
        myListAdapter=new MyListAdapter(this,cursor);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor current_cursor=(Cursor) listView.getItemAtPosition(position);
                int item_id=current_cursor.getInt(current_cursor.getColumnIndexOrThrow("_id"));
                startActivity(new Intent(MainActivity.this,EditMemoActivity.class)
                .putExtra("item_id",item_id)
                .putExtra("type","edit"));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                startActivity(new Intent(MainActivity.this,
                        EditMemoActivity.class)
                .putExtra("type","add"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
