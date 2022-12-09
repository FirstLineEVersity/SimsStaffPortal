package com.sims.staffportal.viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sims.staffportal.CheckNetwork;
import com.sims.staffportal.R;
import com.sims.staffportal.properties.AnswerModel;
import com.sims.staffportal.properties.QuestionModel;
import com.sims.staffportal.properties.SubAnswerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import webservice.WebService;

public class DailyQuestionerViewPager extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private static String ResultString = "";
    private String strResultMessage = "";
    List<QuestionModel> qml = new ArrayList();

    TextView tvPageTitle;
    long lngEmployeeId = 0;
    String strEmployeeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_questioner_view_pager);
        tvPageTitle = (TextView) findViewById(R.id.pageTitle);
        tvPageTitle.setText("Daily Activity Questioner");
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
        WebService.strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
        WebService.METHOD_NAME = "getDailyActivityQuestioner1Json";
        if (!CheckNetwork.isInternetAvailable(DailyQuestionerViewPager.this)) {
            Toast.makeText(DailyQuestionerViewPager.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
            return;
        } else {

            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }
        viewPager = findViewById(R.id.vpQuestion);

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // add the fragments

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DailyQuestionerViewPager.this);

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
                if(temp.length() > 0) {
                    boolean selectedAlready = false;
                    for (int i = 0; i <= temp.length() - 1; i++) {
                        JSONObject object = new JSONObject(temp.getJSONObject(i).toString());
                        String[] answeridStr = object.getString("answerid").split(",");
                        String[] answerdesc = object.getString("answerdesc").split("~");
                        List<AnswerModel> answerModelList = new ArrayList<>();
                        int selectedSubAns = 0;
                        if (object.has("selectedsubanswerid")) {
                            selectedSubAns = object.getInt("selectedsubanswerid");
                        }
                        String inputanswer = "";
                        if (object.has("inputanswer")) {
                            inputanswer = object.getString("inputanswer");
                        }

                        for(int a = 0; a<answeridStr.length;a++){
                            String[] ans = answeridStr[a].split("~");

                            int ansId = Integer.parseInt(ans[0]);
                            int ansMode = Integer.parseInt(ans[1]);
                            String ansDesc = answerdesc[a];

                            List<SubAnswerModel> subAnswerModelList = new ArrayList<>();

                            if (ansMode == 1 || ansMode == 2 ) {
                                String[] subAnsweridStr = object.getString("subanswerid").split(",");
                                String[] subAnswerDesc = object.getString("subanswerdesc").split("~");
                                subAnswerModelList.clear();

                                for(int b = 0; b<subAnsweridStr.length;b++){
                                    String[] subAns = subAnsweridStr[b].split("##");
                                    int subAnsId = Integer.parseInt(subAns[0]);
                                    int subAnsMode = Integer.parseInt(subAns[1]);

                                    String subAnsDesc = subAnswerDesc[b];
                                    SubAnswerModel sam = new SubAnswerModel(subAnsId,subAnsMode,subAnsDesc,selectedSubAns,inputanswer);
                                    subAnswerModelList.add(sam);
                                }
                            }
                            AnswerModel ansModel = new AnswerModel(ansId,ansMode,ansDesc,subAnswerModelList,selectedSubAns);
                            answerModelList.add(ansModel);
                        }

                        int selectedOption = 0;
                        if (object.has("selectedanswerid")) {
                            selectedOption = object.getInt("selectedanswerid");
                            selectedAlready = true;
                        }
                        int selectedOptionMode = 0;
                        int selectedOptionSub = 0;
                        if (object.has("selectedsubanswerid")) {
                            selectedOptionSub = object.getInt("selectedsubanswerid");

                        }
                        String inputAns ="";
                        if (object.has("inputanswer")) {
                            inputAns = object.getString("inputanswer");

                        }
                        QuestionModel qm = new QuestionModel(i ,object.getInt("questionid"), object.getString("questiondesc"),answeridStr.length,answerModelList, selectedOption,selectedOptionMode,selectedOptionSub,0,inputAns);
                        qml.add(qm);
                    }
                    if(qml.size() > 0){
                        //Insert Data in sqllite
                        for(int s = 0;s <qml.size();s++){
                            viewPagerAdapter.add(new Page1(qml.get(s)), "Page 1");
                        }
                    }
                    viewPagerAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(DailyQuestionerViewPager.this, ResultString, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}