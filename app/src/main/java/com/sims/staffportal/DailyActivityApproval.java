package com.sims.staffportal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import webservice.WebService;

public class DailyActivityApproval extends AppCompatActivity {
    private TextView tvPageTitle, tvLastUpdated;
    private long lngEmployeeId=0;
    private static String[] strParameters;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private static String ResultString = "";
    private final ArrayList<String> pending_list = new ArrayList<String>(200);
    SQLiteDatabase db;
    private String strResultMessage="";
    private int intFlag = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyactivityapproval);
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        tvPageTitle = findViewById(R.id.pageTitle);
        tvPageTitle.setText("Daily Activity Approval");
        tvLastUpdated = findViewById(R.id.txtLastUpdated);
        tvLastUpdated.setVisibility(View.GONE);
        Button btnBack= findViewById(R.id.button_back);
        Button btnRefresh= findViewById(R.id.button_refresh);
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
        intFlag=getIntent().getIntExtra("Flag",1);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
                WebService.strParameters = strParameters;
                WebService.METHOD_NAME = "getPendingActivityQuestionerJson";
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });
        strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
        WebService.strParameters = strParameters;
        WebService.METHOD_NAME = "getPendingActivityQuestionerJson";
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    public void callDailyActivityApproveReject(long lngFeedbackId,int intActiveStatus){
        strParameters = new String[]{"Long", "questionerfeedbackid", String.valueOf(lngFeedbackId),
                "int", "approveorrejectstatus", String.valueOf(intActiveStatus),
                "Long", "approvalemployeeid", String.valueOf(lngEmployeeId)};
        WebService.strParameters = strParameters;
        WebService.METHOD_NAME = "ApproveorRejectDailyActivityJson";
        AsyncCallApproveDailyActivityWS task = new AsyncCallApproveDailyActivityWS();
        task.execute();
    }

    public void callDailyActivityView(long lngFeedbackId){
        Intent intent = new Intent(this, DailyActivityQuestioner.class);
        intent.putExtra("questionerfeedbackid",String.valueOf(lngFeedbackId));
        startActivity(intent);
/*
        strParameters = new String[]{"Long", "questionerfeedbackid", String.valueOf(lngFeedbackId)};
        WebService.strParameters = strParameters;
        WebService.METHOD_NAME = "getViewActivityQuestionerJson";
        AsyncCallApproveDailyActivityWS task = new AsyncCallApproveDailyActivityWS();
        task.execute();

 */
    }

    private class AsyncCallApproveDailyActivityWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DailyActivityApproval.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //Log.i(TAG, "onPostExecute");
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            try {
                JSONObject js = new JSONObject(ResultString);
                if(js.has("Status") && js.getString("Status").equalsIgnoreCase("Success")){
                    Toast.makeText(DailyActivityApproval.this, js.getString("Message"), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DailyActivityApproval.this, js.getString("Message"), Toast.LENGTH_LONG).show();
                }
                //clearForm((ViewGroup) findViewById(R.id.leaveentrylayout));
            } catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(DailyActivityApproval.this, ResultString, Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DailyActivityApproval.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try{
                JSONObject jsonObject = new JSONObject(ResultString);
                if (jsonObject.getString("Status").equals("Error"))
                    strResultMessage = jsonObject.getString("Message");
            }
            catch (Exception e){}
            try {
                JSONArray temp = new JSONArray(ResultString);
                pending_list.clear();
                for (int i = 0; i <= temp.length() - 1; i++){
                    JSONObject object = new JSONObject(temp.getJSONObject(i).toString());
                    pending_list.add(object.getString("questionerfeedbackid") + "##" +object.getString("employeename")
                            + "##" + object.getString("department")+ "##" + object.getString("designation") + "##"
                            + object.getString("feedbackdate"));
                }
                if (pending_list.size() == 0){
                    Toast.makeText(DailyActivityApproval.this, "Response: No Data Found", Toast.LENGTH_LONG).show();
                } else {
                    mRecyclerView = findViewById(R.id.rvLeaveStatus); // Assigning the RecyclerView Object to the xml View
                    mRecyclerView.setHasFixedSize(true);
                    // Letting the system know that the list objects are of fixed size
                    DailyActivityApprovalLVAdapter TVA = new DailyActivityApprovalLVAdapter(pending_list, R.layout.dailyactivitylistitems);
                    mRecyclerView.setAdapter(TVA);
                    mLayoutManager = new LinearLayoutManager(DailyActivityApproval.this);                 // Creating a layout Manager
                    mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(DailyActivityApproval.this, "Response: "+strResultMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}