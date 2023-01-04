package com.sims.staffportal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StaffBiometricLVAdapter extends RecyclerView.Adapter<StaffBiometricLVAdapter.ViewHolder> {
    private static ArrayList<String> leavestatus_list=new ArrayList<String>();
    private int itemLayout;

    public StaffBiometricLVAdapter(ArrayList<String> leavestatus_list, int itemLayout) {
        this.leavestatus_list = leavestatus_list;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = leavestatus_list.get(position);
        String[] strColumns = item.split("##");
        if (position % 2 != 0){
            holder.tbLeaveAvailability.setBackgroundResource(R.color.cardColorg);
        }else{
            holder.tbLeaveAvailability.setBackgroundResource(R.color.colorWhite);
        }

            holder.txtDate.setText(strColumns[0]);
        holder.txtFistIn.setText(strColumns[1]);
            holder.txtLastOut.setText(strColumns[2]);
            holder.txtFistInLate.setText(strColumns[3]);
            holder.txtLastOutEarly.setText(strColumns[4]);


    }

    @Override
    public int getItemCount() {
        return leavestatus_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate,txtFistIn,txtLastOut,txtFistInLate,txtLastOutEarly;
        TableLayout tbLeaveAvailability;
        public ViewHolder(View itemView){
            super(itemView);
            tbLeaveAvailability = itemView.findViewById(R.id.tbLeaveAvailability);
            txtDate = (TextView) itemView.findViewById(R.id.txt1);
            txtFistIn = (TextView) itemView.findViewById(R.id.txt2);
            txtLastOut = (TextView) itemView.findViewById(R.id.txt3);
            txtFistInLate = (TextView) itemView.findViewById(R.id.txt4);
            txtLastOutEarly = (TextView) itemView.findViewById(R.id.txt5);


        }
    }
}
