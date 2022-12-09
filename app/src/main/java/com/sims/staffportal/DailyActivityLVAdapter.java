package com.sims.staffportal;

import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.sims.staffportal.properties.AnswerModel;
import com.sims.staffportal.properties.ItemClickListener;
import com.sims.staffportal.properties.QuestionModel;
import com.sims.staffportal.properties.SubAnswerModel;

import java.util.ArrayList;
import java.util.List;

public class DailyActivityLVAdapter extends RecyclerView.Adapter<DailyActivityLVAdapter.ViewHolder> {
    private static List<QuestionModel> qml = new ArrayList();
    private int itemLayout;
    ItemClickListener itemClickListener;

    public DailyActivityLVAdapter(List<QuestionModel> leavestatus_list, int itemLayout, ItemClickListener itemClickListener) {
        this.qml = leavestatus_list;
        this.itemLayout = itemLayout;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final QuestionModel qm = qml.get(position);
        String item = qm.getQuestion();
        int quesNo = position + 1;
        holder.textQuestion.setText(quesNo + "." + item);
        holder.radioGroup.removeAllViews();
        holder.textQuestion.setTag(position);
        int selectedSubAnsPos = -1;

        String[] myStringArray = null;
        for (int count = 0; count < qm.getNubOfOptions(); count++) {
            AnswerModel am = qm.getAnswerModelList().get(count);
            RadioButton btn = new RadioButton(holder.textQuestion.getContext());
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
            holder.radioGroup.addView(btn);

            if (am.getAnswerMode() == 1) {
                List<SubAnswerModel> saml = am.getSubAnswerModelList();
                myStringArray = new String[saml.size()];

                for (int a = 0; a < saml.size(); a++) {
                    if (qm.getSelectedOptionSub() > 0) {
                        if (saml.get(a).getSubAnswerId() == qm.getSelectedOptionSub()) {
                            selectedSubAnsPos = a;
                        }
                    }
                    myStringArray[a] = saml.get(a).getSubAnswerDesc();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.dropdown.getContext(),
                        android.R.layout.simple_list_item_1, myStringArray);

                holder.dropdown.setAdapter(adapter);
            } else if (am.getAnswerMode() == 3) {
                holder.dropdown.setVisibility(View.GONE);
            }

        }
        if (selectedSubAnsPos > -1) {
            holder.dropdown.setVisibility(View.VISIBLE);
            holder.dropdown.setSelection(selectedSubAnsPos);
            holder.dropdown.setEnabled(false);
        }

        if (qm.getInputAns().trim().length() > 0 && qm.getSelectedOptionMode() > 0) {
            holder.txtInputAns.setVisibility(View.GONE);

            if (qm.getSelectedOptionMode() == 1 && holder.dropdown.getSelectedItem().toString().equalsIgnoreCase("Any Other")) {
                holder.txtInputAnswer.setVisibility(View.VISIBLE);
                holder.txtInputAnswer.setText(qm.getInputAns().trim());
                holder.txtInputAnswer.setFocusable(false);
            } else if (qm.getSelectedOptionMode() == 3) {
                holder.txtInputAnswer.setVisibility(View.VISIBLE);
                holder.txtInputAnswer.setText(qm.getInputAns().trim());
                holder.txtInputAnswer.setFocusable(false);

            }
        } else {
            holder.txtInputAns.setVisibility(View.GONE);
            holder.txtInputAns.setFocusable(true);
        }
        if (selectedSubAnsPos == -1) {
            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int selectedPos = Integer.parseInt(holder.textQuestion.getTag().toString());
                    RadioButton rb = (RadioButton) holder.radioGroup.findViewById(checkedId);
                    if (rb != null && rb.getTag() != null) {
                        int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                        if (qm.getAnswerModelList() != null) {
                            AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                            if (am.getAnswerMode() == 0) {
                                holder.dropdown.setVisibility(View.GONE);
                                holder.txtInputAns.setVisibility(View.GONE);
                            } else if (am.getAnswerMode() == 1) {
                                holder.dropdown.setVisibility(View.VISIBLE);
                            } else if (am.getAnswerMode() == 3) {
                                holder.txtInputAns.setVisibility(View.VISIBLE);
                            }
                            itemClickListener.onClick(selectedPos, selectedAnsPos, checkedId, am.getAnswerMode(), 0, 0, "");
                        }
                    }
                }
            });

            holder.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    int selectedPos = Integer.parseInt(holder.textQuestion.getTag().toString());
                    RadioButton rb = (RadioButton) holder.radioGroup.findViewById(holder.radioGroup.getCheckedRadioButtonId());
                    int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                    if (qm.getAnswerModelList() != null) {

                        AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                        SubAnswerModel subAm = am.getSubAnswerModelList().get(position);
                        if (am.getAnswerMode() == 1) {
                            itemClickListener.onClick(selectedPos, selectedAnsPos, holder.radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, subAm.getSubAnswerId(), "");

                        } else if (am.getAnswerMode() == 3) {
                            itemClickListener.onClick(selectedPos, selectedAnsPos, holder.radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, 0, "");
                        }
                    }

                    if (parentView.getItemAtPosition(position).toString().equalsIgnoreCase("Any Other")) {
                        holder.txtInputAns.setText("");
                        holder.txtInputAns.setVisibility(View.VISIBLE);
                    } else {
                        holder.txtInputAns.setText("");
                        holder.txtInputAns.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            holder.txtInputAns.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //setDate
                    int selectedPos = Integer.parseInt(holder.textQuestion.getTag().toString());
                    RadioButton rb = (RadioButton) holder.radioGroup.findViewById(holder.radioGroup.getCheckedRadioButtonId());
                    int selectedAnsPos = Integer.parseInt(rb.getTag().toString());
                    int position = holder.dropdown.getSelectedItemPosition();
                    if (qm.getAnswerModelList() != null) {
                        AnswerModel am = qm.getAnswerModelList().get(selectedAnsPos);
                        if (am.getAnswerMode() == 1) {
                            SubAnswerModel subAm = am.getSubAnswerModelList().get(position);

                            itemClickListener.onClick(selectedPos, selectedAnsPos, holder.radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), position, subAm.getSubAnswerId(), s.toString());

                        } else if (am.getAnswerMode() == 3) {
                            itemClickListener.onClick(selectedPos, selectedAnsPos, holder.radioGroup.getCheckedRadioButtonId(), am.getAnswerMode(), 0, 0, s.toString());
                        }
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return qml.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textQuestion;
        private EditText txtInputAns;
        private RadioGroup radioGroup;
        private Spinner dropdown;
        private TextInputEditText txtInputAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            textQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            txtInputAns = (EditText) itemView.findViewById(R.id.txtInputAns);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            dropdown = itemView.findViewById(R.id.spinner1);
            txtInputAnswer = itemView.findViewById(R.id.txtInputAnswer);
        }
    }
}
