package com.sims.staffportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import webservice.SqlliteController;

public class HomePageGridViewLayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String strEmployee = "";
    SQLiteDatabase db;
    SqlliteController controllerdb = new SqlliteController(this);
    private long lngEmployeeId = 0;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout_cardview_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
        //Status Bar Color
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        //final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridViewmenu);
        setSingleEvent(gridLayout);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imageView=(ImageView) headerView.findViewById(R.id.imgStudentPhoto);

        Button btn = findViewById(R.id.btnTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DailyActivityQuestioner.class);
                startActivity(intent);
            }
        });
        btn.setVisibility(View.GONE);

        TextView tvEmployee = (TextView) headerView.findViewById(R.id.tvEmployeeName);
        tvEmployee.setText(loginsession.getString("employeename",""));
        TextView tvDepartment = (TextView) headerView.findViewById(R.id.tvDepartment);
        tvDepartment.setText(loginsession.getString("department",""));
        TextView tvDesignation = (TextView) headerView.findViewById(R.id.tvDesignation);
        tvDesignation.setText(loginsession.getString("designation",""));
        String base64 = loginsession.getString("profileImage","");
        if(!base64.equalsIgnoreCase("")){
            byte[] byteArrPhoto= Base64.decode(base64, Base64.DEFAULT);
            setCirularImage(byteArrPhoto);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomePageGridViewLayout.this);
        navigationView.setItemIconTintList(null);
        getMenuValues();
    }
    public void setCirularImage(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCornerRadius(10);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    // we are setting onClickListener for each element
    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
//            if (!CheckNetwork.isInternetAvailable(getApplicationContext())) {
//                Toast.makeText(getApplicationContext(),"You dont have Internet connection", Toast.LENGTH_LONG).show();
//                return;
//            }
            cardView.setOnClickListener(new View.OnClickListener() {
                SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
                SharedPreferences.Editor ed = loginsession.edit();
                @Override
                public void onClick(View view){
                    if (finalI == 0){ //Profile
                        Intent intent = new Intent(getApplicationContext(), PersonalDetails.class);
                        // Intent intent = new Intent(getApplicationContext(), UploadFiles.class);
                        startActivity(intent);
                        finish();
                    } else if (finalI == 2){ //Logout
                        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        SqlliteController sc = new SqlliteController(getApplicationContext());
                        sc.deleteLoginStaffDetails();
                        Intent intent = new Intent(HomePageGridViewLayout.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else{
                        Intent intent = new Intent(HomePageGridViewLayout.this, HomeScreenCategory.class);
                        intent.putExtra("Flag",1);
                        ed.putInt("categoryid", finalI+1);
                        ed.commit();
//                        intent.putExtra("CategoryId",finalI+1);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void getMenuValues() {
        db = controllerdb.getReadableDatabase();
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navView.getMenu();

        Cursor cursor = db.rawQuery("SELECT * FROM userwisemenuaccessrights WHERE employeeid =" + lngEmployeeId + " ORDER BY  menusortnumber", null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                long lngMenuId = cursor.getLong(cursor.getColumnIndex("menuid"));
                if (lngMenuId == 2){
                    nav_Menu.findItem(R.id.nav_daily_activity).setVisible(true);
                }
                if (lngMenuId == 3){
                    nav_Menu.findItem(R.id.nav_daily_activity_approval).setVisible(true);
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        navView.invalidate();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();
        Toolbar toolbar = findViewById(R.id.toolbar);
//        if (CheckNetwork.isInternetAvailable(HomePageGridViewLayout.this)) {
//            Toast.makeText(HomePageGridViewLayout.this,"You dont have Internet connection", Toast.LENGTH_LONG).show();
//        }
        if (id == R.id.nav_personaldetails){
            toolbar.setTitle(getResources().getString(R.string.hProfile));
            Intent intent = new Intent(HomePageGridViewLayout.this,PersonalDetails.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Flag",2);
            startActivity(intent);
            //fragmentClass = ProfileFragment.class;
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new ProfileFragment()).commit();
        }else if (id == R.id.nav_daily_activity){
            toolbar.setTitle(getResources().getString(R.string.hDailyActivity));
            Intent intent = new Intent(HomePageGridViewLayout.this,DailyActivityQuestioner.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Flag",2);
            startActivity(intent);
        }else if (id == R.id.nav_daily_activity_approval){
            toolbar.setTitle(getResources().getString(R.string.hDailyActivityApproval));
            Intent intent = new Intent(HomePageGridViewLayout.this,DailyActivityApproval.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Flag",2);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.clear();
            editor.commit();
            SqlliteController sc = new SqlliteController(getApplicationContext());
            sc.deleteLoginStaffDetails();
            Intent intent = new Intent(HomePageGridViewLayout.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.nav_personaldetails) {
                item.setIcon(getDrawable(R.drawable.icon_profile));
            }
        }
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        finishAffinity();
    }
}