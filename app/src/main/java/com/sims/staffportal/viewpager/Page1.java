package com.sims.staffportal.viewpager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sims.staffportal.R;
import com.sims.staffportal.properties.AnswerModel;
import com.sims.staffportal.properties.QuestionModel;
import com.sims.staffportal.properties.SubAnswerModel;

import java.util.List;

public class Page1 extends Fragment {
    QuestionModel qm;
    public Page1(QuestionModel questionModel) {
        // required empty public constructor.
        this.qm = questionModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private TextView textQuestion;
    private EditText txtInputAns;
    private RadioGroup radioGroup;
    private Spinner dropdown;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_page1, container, false);
        textQuestion = v.findViewById(R.id.txtQuestion);
        textQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        txtInputAns = (EditText) v.findViewById(R.id.txtInputAns);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        dropdown = v.findViewById(R.id.spinner1);

        textQuestion.setText(qm.getQuestionId()+"."+qm.getQuestion());
        textQuestion.setTag(qm.getQuestionId());

        radioGroup.removeAllViews();
        int selectedSubAnsPos = -1;

        String[] myStringArray = null;
        for (int count = 0; count < qm.getNubOfOptions(); count++) {
            AnswerModel am = qm.getAnswerModelList().get(count);
            RadioButton btn = new RadioButton(textQuestion.getContext());
            btn.setText(am.getAnswerDesc());
            int selectedOP = am.getAnswerId();
            btn.setId(selectedOP);
            btn.setTag(count + "");
            if (qm.getSelectedOption() > 0) {
                btn.setClickable(false);
            }
            if (selectedOP == qm.getSelectedOption()) {
                btn.setChecked(true);
            } else {
                btn.setEnabled(false);
            }
            if (qm.getSelectedOption() == 0) {
                btn.setEnabled(true);
            }
            radioGroup.addView(btn);

            if (am.getAnswerMode() == 1) {
                List<SubAnswerModel> saml = am.getSubAnswerModelList();
                myStringArray = new String[saml.size()];

                for (int a = 0; a < saml.size(); a++) {
                    if(qm.getSelectedOptionSub()>0){
                        if(saml.get(a).getSubAnswerId() == qm.getSelectedOptionSub()){
                            selectedSubAnsPos = a;
                        }
                    }
                    myStringArray[a] = saml.get(a).getSubAnswerDesc();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(dropdown.getContext(),
                        android.R.layout.simple_list_item_1, myStringArray);

                dropdown.setAdapter(adapter);
            } else if (am.getAnswerMode() == 3) {
                dropdown.setVisibility(View.GONE);
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedPos = Integer.parseInt(textQuestion.getTag().toString());
                RadioButton rb = (RadioButton) radioGroup.findViewById(checkedId);
                if (rb != null && rb.getTag() != null) {
                    int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                    if (qm.getAnswerModelList() != null) {
                        AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                        if (am.getAnswerMode() == 0) {
                            dropdown.setVisibility(View.GONE);
                            txtInputAns.setVisibility(View.GONE);
                        } else if (am.getAnswerMode() == 1) {
                            dropdown.setVisibility(View.VISIBLE);
                        } else if (am.getAnswerMode() == 3) {
                            txtInputAns.setVisibility(View.VISIBLE);
                        }
                        // itemClickListener.onClick(selectedPos, selectedAnsPos, checkedId, am.getAnswerMode(), 0, 0, "");
                    }
                }
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                int selectedPos = Integer.parseInt(textQuestion.getTag().toString());
                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                if (qm.getAnswerModelList() != null) {

                    AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                    SubAnswerModel subAm = am.getSubAnswerModelList().get(position);
                    if (am.getAnswerMode() == 1) {
                        //  itemClickListener.onClick(selectedPos, selectedAnsPos, radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, subAm.getSubAnswerId(), "");

                    } else if (am.getAnswerMode() == 3) {
                        //  itemClickListener.onClick(selectedPos, selectedAnsPos, radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, 0, "");
                    }
                }

                if (parentView.getItemAtPosition(position).toString().equalsIgnoreCase("Any Other")) {
                    txtInputAns.setVisibility(View.VISIBLE);
                } else {
                    txtInputAns.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        txtInputAns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("TEST TEST ", s.toString());
                //setDate
                int selectedPos = Integer.parseInt(textQuestion.getTag().toString());
                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                int position = dropdown.getSelectedItemPosition();
                if (qm.getAnswerModelList() != null) {
                    AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                    if (am.getAnswerMode() == 1) {
                        SubAnswerModel subAm = am.getSubAnswerModelList().get(position);

                        //   itemClickListener.onClick(selectedPos, selectedAnsPos, radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, subAm.getSubAnswerId(), s.toString());

                    } else if (am.getAnswerMode() == 3) {
                        //   itemClickListener.onClick(selectedPos, selectedAnsPos, radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), 0, 0, s.toString());
                    }
                }
            }
        });
        return v;
    }
}
