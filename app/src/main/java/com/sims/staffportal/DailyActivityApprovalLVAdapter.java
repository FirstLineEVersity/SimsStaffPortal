package com.sims.staffportal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import webservice.CheckNetwork;

public class DailyActivityApprovalLVAdapter extends RecyclerView.Adapter<DailyActivityApprovalLVAdapter.ViewHolder> {
    private static ArrayList<String> pending_list=new ArrayList<String>(500);
    private final int itemLayout;

    public DailyActivityApprovalLVAdapter(ArrayList<String> pending_list, int itemLayout){
        DailyActivityApprovalLVAdapter.pending_list = pending_list;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String item = pending_list.get(position);
        String[] strColumns = item.split("##");

        holder.textEmployeeName.setText(strColumns[1]);
        holder.textDepartment.setText(strColumns[2]);
        holder.textDesignation.setText(strColumns[3]);
        holder.textFeedbackDate.setText(strColumns[4]);
    }

    @Override
    public int getItemCount() {
        return pending_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText textEmployeeName;
        private final TextInputEditText textDepartment;
        private final TextInputEditText textDesignation;
        private final TextInputEditText textFeedbackDate;
        private final Button btnApprove;
        private final Button btnReject;
        private final Button btnView;
        private long lngFeedbackId=0;

        public ViewHolder(View itemView){
            super(itemView);
            textEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            textDepartment = itemView.findViewById(R.id.txtDepartment);
            textDesignation = itemView.findViewById(R.id.txtDesignation);
            textFeedbackDate = itemView.findViewById(R.id.txtFeedbackDate);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnView = itemView.findViewById(R.id.btnView);

            //this.btnApprove.setOnClickListener();
            this.btnView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        if (!CheckNetwork.isInternetAvailable(v.getContext())) {
                            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            String item = pending_list.get(getPosition());
                            String[] strColumns = item.split("##");
                            lngFeedbackId = Long.parseLong(strColumns[0]);

                            Intent intent = new Intent(v.getContext(), DailyActivityQuestioner.class);
                            intent.putExtra("questionerfeedbackid",String.valueOf(lngFeedbackId));
                            v.getContext().startActivity(intent);

                           /* String item = pending_list.get(getPosition());
                            String[] strColumns = item.split("##");
                            removeAt(getPosition());
                            ((DailyActivityApproval) v.getContext()).callDailyActivityView(lngFeedbackId);

                            */
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            });
            this.btnApprove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        if (!CheckNetwork.isInternetAvailable(v.getContext())) {
                            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                            return;
                        }else {

                            String item = pending_list.get(getPosition());
                            String[] strColumns = item.split("##");
                            lngFeedbackId = Long.parseLong(strColumns[0]);
                            removeAt(getPosition());
                            ((DailyActivityApproval) v.getContext()).callDailyActivityApproveReject(lngFeedbackId, 1);
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            });
            this.btnReject.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        if (!CheckNetwork.isInternetAvailable(v.getContext())) {
                            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                            return;
                        }else {

                            String item = pending_list.get(getPosition());
                            String[] strColumns = item.split("##");
                            lngFeedbackId = Long.parseLong(strColumns[0]);
                            removeAt(getPosition());
                            ((DailyActivityApproval) v.getContext()).callDailyActivityApproveReject(lngFeedbackId, 9);
                        } } catch (Exception e) {
                        // ignore
                    }
                }
            });
        }
    }

    public void removeAt(int position) {
        pending_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pending_list.size());
    }
}