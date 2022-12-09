package com.sims.staffportal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sims.staffportal.properties.AnswerModel;
import com.sims.staffportal.properties.ItemClickListener;
import com.sims.staffportal.properties.QuestionModel;
import com.sims.staffportal.properties.SubAnswerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import webservice.WebService;

public class DailyActivityQuestioner extends AppCompatActivity {
    long lngEmployeeId = 0;
    String strEmployeeName = "";
    TextView tvPageTitle, txtNoData;
    String strHTML = "";
    Button btnSave;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private static String ResultString = "";
    private String strResultMessage = "";
    List<QuestionModel> qml = new ArrayList();
    ItemClickListener itemClickListener;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_activity);
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        tvPageTitle = (TextView) findViewById(R.id.pageTitle);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        btnSave = (Button) findViewById(R.id.btnSave);
        tvPageTitle.setText("Daily Activity ");
        Button btnBack = (Button) findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
        strEmployeeName = loginsession.getString("employeename", "");
        Intent intent = getIntent();
        String questionerfeedbackid = null;
        if(intent != null) {
            questionerfeedbackid = intent.getStringExtra("questionerfeedbackid");
        }
        if(questionerfeedbackid != null){
            WebService.strParameters =  new String[]{"Long", "questionerfeedbackid",questionerfeedbackid};
            WebService.METHOD_NAME = "getViewActivityQuestionerJson";
            if (!CheckNetwork.isInternetAvailable(DailyActivityQuestioner.this)) {
                Toast.makeText(DailyActivityQuestioner.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                return;
            } else {
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        }else {
            WebService.strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
            WebService.METHOD_NAME = "getDailyActivityQuestioner1Json";
            if (!CheckNetwork.isInternetAvailable(DailyActivityQuestioner.this)) {
                Toast.makeText(DailyActivityQuestioner.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                return;
            } else {

                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        }
        // Initialize listener
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int listPos, int ansPos, int selectedAnswerId, int selectedAnswerMode, int subAnsListPos, int selectedSubAnswerId, String inputAns) {
                QuestionModel qsm = qml.get(listPos);
                qsm.setSelectedOption(selectedAnswerId);
                qsm.setSelectedOptionMode(selectedAnswerMode);
                qsm.setSelectedOptionSub(selectedSubAnswerId);
                qsm.setInputAns(inputAns);
                qsm.setSelectedSubAnsPos(subAnsListPos);
                qml.set(listPos, qsm);
                //     Log.i("TEST1", selectedAnswerId + " : " + selectedAnswerMode + " : " + selectedSubAnswerId + " : " + inputAns + " : " + subAnsListPos);

            }
        };
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
        boolean sel = true;
        boolean selAnyText = true;
        for (int i = 0; i < qml.size(); i++) {

            if (qml.get(i).getSelectedOption() == 0) {
                sel = false;
            }
        }
        for (int i = 0; i < qml.size(); i++) {
            AnswerModel answerModel = null;
            if (qml.get(i).getSelectedOption() != 0) {
                List<AnswerModel> ansModList =  qml.get(i).getAnswerModelList();
                for(int j = 0;j<ansModList.size();j++){
                    if(qml.get(i).getSelectedOption() == ansModList.get(j).getAnswerMode()){
                        List<SubAnswerModel> subAnswerModel =  ansModList.get(j).getSubAnswerModelList();
                        for(int a = 0;a<subAnswerModel.size();a++) {
                            SubAnswerModel sub = subAnswerModel.get(a);
                            if(qml.get(i).getSelectedOptionSub() == subAnswerModel.get(a).getSubAnswerId()){
                                if(sub.getSubAnswerDesc().equalsIgnoreCase("Any Other")){
                                    if(qml.get(i).getInputAns().length() < 1){
                                        selAnyText = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sel) {
            String dataSelected = "";
            for (int i = 0; i < qml.size(); i++) {
                QuestionModel qm = qml.get(i);
                String inputAnsText =  qm.getInputAns();
                if(qm.getSelectedOptionMode() == 0){
                    inputAnsText = "";
                }
                String s = qm.getQuestionId() + "," + qm.getSelectedOption() + "," + qm.getSelectedOptionSub() + "," +inputAnsText;
                Log.i("TEST ANS:",s);
                dataSelected = dataSelected + s + "$$";
            }
            if (dataSelected.length() > 0) {
                dataSelected = dataSelected.substring(0, dataSelected.length() - 2);
                WebService.strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId), "String", "feedbackvalues", dataSelected};
                WebService.METHOD_NAME = "SaveDailyActivityQuestioner1Json";
                if (!CheckNetwork.isInternetAvailable(DailyActivityQuestioner.this)) {
                    Toast.makeText(DailyActivityQuestioner.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    AsyncCallWSSave task = new AsyncCallWSSave();
                    task.execute();
                }
            }
        } else {
            if(!selAnyText){
                Toast.makeText(this, "Please Enter Something for 'Any Other' Selection", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Please Answer for All Questions ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DailyActivityQuestioner.this);

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
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            qml.clear();
            try {
                JSONArray temp = new JSONArray(ResultString.toString());
                if (temp.length() > 0) {
                    boolean selectedAlready = false;
                    for (int i = 0; i <= temp.length() - 1; i++) {

                        JSONObject object = new JSONObject(temp.getJSONObject(i).toString());
                        String[] answeridStr = object.getString("answerid").split(",");
                        String[] answerdesc = object.getString("answerdesc").split("~");
                        int selectedAnsMode = 0;
                        List<AnswerModel> answerModelList = new ArrayList<>();
                        int selectedSubAns = 0;
                        if (object.has("selectedsubanswerid")) {
                            selectedSubAns = object.getInt("selectedsubanswerid");
                        }
                        String inputanswer = "";
                        if (object.has("inputanswer")) {
                            inputanswer = object.getString("inputanswer");
                        }
                        for (int a = 0; a < answeridStr.length; a++) {
                            String[] ans = answeridStr[a].split("~");

                            int ansId = Integer.parseInt(ans[0]);
                            int ansMode = Integer.parseInt(ans[1]);
                            if (object.has("selectedanswerid")) {
                                if(ansId == object.getInt("selectedanswerid")){
                                    selectedAnsMode = ansMode;
                                }
                            }
                            String ansDesc = answerdesc[a];
                            List<SubAnswerModel> subAnswerModelList = new ArrayList<>();
                            if (ansMode == 1 || ansMode == 2) {
                                String[] subAnsweridStr = object.getString("subanswerid").split(",");
                                String[] subAnswerDesc = object.getString("subanswerdesc").split("~");
                                subAnswerModelList.clear();

                                for (int b = 0; b < subAnsweridStr.length; b++) {
                                    String[] subAns = subAnsweridStr[b].split("##");
                                    int subAnsId = Integer.parseInt(subAns[0]);
                                    int subAnsMode = Integer.parseInt(subAns[1]);

                                    String subAnsDesc = subAnswerDesc[b];
                                    SubAnswerModel sam = new SubAnswerModel(subAnsId, subAnsMode, subAnsDesc, selectedSubAns, inputanswer);
                                    subAnswerModelList.add(sam);
                                }
                            }
                            AnswerModel ansModel = new AnswerModel(ansId, ansMode, ansDesc, subAnswerModelList, selectedSubAns);
                            answerModelList.add(ansModel);
                        }
                        int selectedOption = 0;
                        if (object.has("selectedanswerid")) {
                            selectedOption = object.getInt("selectedanswerid");
                            selectedAlready = true;
                        }
                        int selectedOptionSub = 0;
                        if (object.has("selectedsubanswerid")) {
                            selectedOptionSub = object.getInt("selectedsubanswerid");
                        }
                        String inputAns = "";
                        if (object.has("inputanswer")) {
                            inputAns = object.getString("inputanswer");
                        }
                        QuestionModel qm = new QuestionModel(i, object.getInt("questionid"), object.getString("questiondesc"), answeridStr.length, answerModelList, selectedOption, selectedAnsMode, selectedOptionSub, 0, inputAns);
                        qml.add(qm);
                    }
                    if (temp.length() > 0) {
                        mRecyclerView = (RecyclerView) findViewById(R.id.rvLeaveStatus); // Assigning the RecyclerView Object to the xml View
                        DailyActivityLVAdapter TVA = new DailyActivityLVAdapter(qml, R.layout.daily_activity_listitem,itemClickListener);
                        TVA.notifyDataSetChanged();
                        mRecyclerView.setAdapter(TVA);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());                 // Creating a layout Manager
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        if (selectedAlready) {
                            btnSave.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.GONE);
                        } else {
                            btnSave.setVisibility(View.VISIBLE);
                            txtNoData.setVisibility(View.GONE);
                        }
                    } else {
                        btnSave.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DailyActivityQuestioner.this, ResultString, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
    }

    private class AsyncCallWSSave extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DailyActivityQuestioner.this);

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
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                JSONObject js = new JSONObject(ResultString);
                if (js.getString("Status").equalsIgnoreCase("Success")) {
                    Toast.makeText(DailyActivityQuestioner.this, js.getString("Message"), Toast.LENGTH_SHORT).show();
                    WebService.strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
                    WebService.METHOD_NAME = "getDailyActivityQuestioner1Json";
                    if (!CheckNetwork.isInternetAvailable(DailyActivityQuestioner.this)) {
                        Toast.makeText(DailyActivityQuestioner.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }
                } else {
                    Toast.makeText(DailyActivityQuestioner.this, ResultString, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(DailyActivityQuestioner.this, ResultString, Toast.LENGTH_SHORT).show();

            }
        }
    }
}