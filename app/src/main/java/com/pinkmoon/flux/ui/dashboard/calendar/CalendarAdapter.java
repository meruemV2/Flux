package com.pinkmoon.flux.ui.dashboard.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.FluxDate;
import com.pinkmoon.flux.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private String month;
    private OnItemClickListener listener;

    private List<Assignment> allAssignments = new ArrayList<>();

    public CalendarAdapter(ArrayList<String> daysOfMonth, String month) {
        this.daysOfMonth = daysOfMonth;
        this.month = month;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_cell, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666); // get each cell to be exactly 1/6th of the view

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.tvDayOfMonth.setText(daysOfMonth.get(position));

        if(!allAssignments.isEmpty()) setDueDateIndicator(holder, position);
    }

    private void setDueDateIndicator(CalendarViewHolder holder, int position) {
        for (Assignment a: allAssignments) {
            if(a.getAssignmentDueDate() != null){
                if(FluxDate.extractDayFromDate(
                        a.getAssignmentDueDate()).equals(daysOfMonth.get(position)) &&
                        FluxDate.extractMonthFromDate(a.getAssignmentDueDate()).equals(month)){
                    holder.tvDueDateIndicator.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void setAllAssignments(List<Assignment> allAssignments){
        this.allAssignments = allAssignments;
        notifyDataSetChanged();
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDayOfMonth;
        private TextView tvDueDateIndicator;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDayOfMonth = itemView.findViewById(R.id.cellDayText);
            tvDueDateIndicator = itemView.findViewById(R.id.indicator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition(), tvDayOfMonth.getText().toString());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String dayText);
    }
}
