package com.sims.staffportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import webservice.SqlliteController;

public class HomeScreenCategory extends AppCompatActivity {
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    //    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private TextView tvPageTitle;
    private int intCategoryId = 0;
    private long lngEmployeeId=0;
    private String strMenuId = "";
    SQLiteDatabase db;
    SqlliteController controllerdb = new SqlliteController(this);
    String[] MENULIST= new String[1];
    int[] ICONS =new int[1];
    private int intFlag = 0;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreencategory);
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        tvPageTitle = (TextView) findViewById(R.id.pageTitle);
        intFlag=getIntent().getIntExtra("Flag",1);
        Button btnBack=(Button) findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeScreenCategory.this, HomePageGridViewLayout.class);
                startActivity(intent);
            }
        });
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
        intCategoryId = loginsession.getInt("categoryid", 0);
//        intCategoryId = getIntent().getIntExtra("CategoryId", 0);
        if (intCategoryId == 2){//Daily Activity
            tvPageTitle.setText(getResources().getString(R.string.hDailyActivity));
        }
        getMenuValues();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent;
//        if (intFlag == 1){
//            intent = new Intent(HomeScreenCategory.this, HomeScreenCategory.class);
//            startActivity(intent);
//        }
//        if (intFlag == 2){
        intent = new Intent(HomeScreenCategory.this, HomePageGridViewLayout.class);
        startActivity(intent);
//        }
        this.finish();
    }

    private void getMenuValues() {
        db = controllerdb.getReadableDatabase();
        try {
            if (intCategoryId == 1){ // Profile
                strMenuId = "1";
            }else if (intCategoryId == 2){// Leave Management
                strMenuId = "2,3";
            }else if (intCategoryId == 3){//Logout
                strMenuId = "50";
            }
            Cursor cursor = db.rawQuery("SELECT * FROM userwisemenuaccessrights WHERE employeeid =" + lngEmployeeId + " AND menuid in ("+strMenuId +")  ORDER BY  menusortnumber", null );
            MENULIST= new String[cursor.getCount()];
            ICONS= new int[cursor.getCount()];
            cursor.moveToFirst();
            if (cursor.moveToFirst()){
                int i = 0;
                do {
                    @SuppressLint("Range") long lngMenuId=cursor.getLong(cursor.getColumnIndex("menuid"));
                    @SuppressLint("Range") String strMenuname = cursor.getString(cursor.getColumnIndex("menuname"))+ "##" +lngMenuId;

                    MENULIST[i] = strMenuname;
                    if (lngMenuId == 1){
                        ICONS[i] = R.drawable.icon_profile;
                    }else if (lngMenuId == 2){
                        ICONS[i] = R.drawable.icon_daily_activity_entry;
                    }else if (lngMenuId == 3){
                        ICONS[i] = R.drawable.icon_daily_activity_approval;
                    }else if (lngMenuId == 50){
                        ICONS[i] = R.drawable.icon_logout;
                    }
                    i++;
                }while (cursor.moveToNext());
            }
            cursor.close();

            if (MENULIST.length == 0) {
                Toast.makeText(HomeScreenCategory.this, getResources().getString(R.string.noFeature), Toast.LENGTH_LONG).show();
            } else {
                mRecyclerView = (RecyclerView) findViewById(R.id.rvMenuContentCategory); // Assigning the RecyclerView Object to the xml View
                mRecyclerView.setHasFixedSize(true);
                // Letting the system know that the list objects are of fixed size
                HomeScreenAdapter TVA = new HomeScreenAdapter(MENULIST, ICONS);
                mRecyclerView.setAdapter(TVA);
                mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
                mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}