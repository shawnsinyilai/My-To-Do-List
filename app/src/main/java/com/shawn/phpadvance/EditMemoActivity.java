package com.shawn.phpadvance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EditMemoActivity extends AppCompatActivity {
    private TextView txtTitle;
    private EditText edit_memo;
    private Button btn_ok, btn_to_previous_page;
    private Spinner spinner_colours;
    private String myMemo,currentTime;
    private MyListener myListener;
    private DbAdapter dbAdapter;
    private DateFormat dateFormat;
    private ArrayList<ItemData> colour_list;
    private SpinnerAdapter spinnerAdapter;
    private String selectedColour;
    private Bundle bundle;
    private int index;
    private Boolean isDeleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        initialisation();
            }

    private void initialisation(){
        toSeeIfValuesSent();
        myListener=new MyListener();
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.TAIWAN);
        colour_list=new ArrayList<>();
        addingColour();
        spinnerAdapter= new SpinnerAdapter(this,colour_list);
        spinner_colours=(Spinner)findViewById(R.id.spinner_colours);
        btn_ok=(Button)findViewById(R.id.confirm);
        btn_to_previous_page=(Button)findViewById(R.id.previous);
        btn_to_previous_page.setOnClickListener(myListener);
        btn_ok.setOnClickListener(myListener);
        spinner_colours.setAdapter(spinnerAdapter);
        spinner_colours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageView img = (ImageView) view.findViewById(R.id.palette);
                ColorDrawable drawable=(ColorDrawable)img.getBackground();
                selectedColour=Integer.toHexString(drawable.getColor()).substring(2);
                Log.i("selected_colour",selectedColour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void toSeeIfValuesSent(){
        edit_memo=(EditText)findViewById(R.id.editMemo);
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        dbAdapter=new DbAdapter(getBaseContext());
        bundle=this.getIntent().getExtras();
        if(bundle.getString("type").equals("edit")){
            txtTitle.setText("Edit Memo");
            index=bundle.getInt("item_id");
            Cursor cursor=dbAdapter.queryById(index);
            edit_memo.setText(cursor.getString(2));
        }
    }

    public void addingColour(){
        colour_list.add(new ItemData("Red","#e4222d"));
        colour_list.add(new ItemData("Green","#00c7a4"));
        colour_list.add(new ItemData("Blue","#4b7bd8"));
        colour_list.add(new ItemData("Orange","#fc8200"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(bundle.getString("type").equals("edit")) menuInflater.inflate(R.menu.del_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.del){
            new AlertDialog.Builder(EditMemoActivity.this)
                    .setTitle("WARNING")
                    .setMessage("Are you sure to permanently delete selected data? ")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isDeleted=dbAdapter.deleteMemo(index);
                            if(isDeleted){
                                Toast.makeText(EditMemoActivity.this,"DELETED",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditMemoActivity.this,MainActivity.class));
                            }

                        }
                    })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.previous:
                    startActivity(new Intent(EditMemoActivity.this,MainActivity.class));
                    break;
                case R.id.confirm:
                    myMemo=edit_memo.getText().toString();
                    Log.i("memo",myMemo);
                    currentTime=dateFormat.format(new Date(System.currentTimeMillis()));
                    if(bundle.getString("type").equals("edit")){
                        try {
                            dbAdapter.updateMemo(index,currentTime,myMemo,null,selectedColour);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(EditMemoActivity.this,MainActivity.class));
                        }
                    }
                    else
                    {
                    currentTime=dateFormat.format(new Date(System.currentTimeMillis()));
                    try {
                        dbAdapter.createMemo(currentTime,myMemo,null,selectedColour);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(EditMemoActivity.this,MainActivity.class));
                    }}
                    break;
            }
        }
    }
}
