package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinkmoon.flux.FluxDate;
import com.pinkmoon.flux.R;

import java.util.ArrayList;
import java.util.List;

public class CourseAssignmentJoinAdapter extends
        RecyclerView.Adapter<CourseAssignmentJoinAdapter.CourseAssignmentJoinHolder> {

    private List<CourseAssignmentJoin> assignmentsByDueDate = new ArrayList<>();

    private static final StrikethroughSpan STRIKETHROUGH_SPAN = new StrikethroughSpan();

    @NonNull
    @Override
    public CourseAssignmentJoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_course_assignment_join, parent, false
        );
        return new CourseAssignmentJoinHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAssignmentJoinHolder holder, int position) {
        CourseAssignmentJoin currentAssignment = assignmentsByDueDate.get(position);

        setTvCourseName(holder, currentAssignment.getCourseName());
        setTvAssignmentName(holder, currentAssignment.getAssignmentName());
        setTvDueDate(holder, currentAssignment.getAssignmentDueDate());
        if(currentAssignment.isComplete()){
            setStrikeThrough(holder, currentAssignment);
        }
    }

    @Override
    public int getItemCount() {
        return assignmentsByDueDate.size();
    }

    private void setTvCourseName(CourseAssignmentJoinHolder holder, String courseName){
        holder.tvCourseName.setText(courseName);
    }

    private void setTvAssignmentName(CourseAssignmentJoinHolder holder, String assignmentName) {
        holder.tvAssignmentName.setText(assignmentName);
    }

    private void setTvDueDate(CourseAssignmentJoinHolder holder, String dueDate){
        holder.tvDueDate.setText(FluxDate.formatDatePretty(dueDate));
    }

    private void setStrikeThrough(CourseAssignmentJoinHolder holder, CourseAssignmentJoin currentAssignment){
        holder.tvCourseName.setText(currentAssignment.getCourseName(), TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) holder.tvCourseName.getText();
        spannable.setSpan(STRIKETHROUGH_SPAN, 0, currentAssignment.getCourseName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvAssignmentName.setText(currentAssignment.getCourseName(), TextView.BufferType.SPANNABLE);
        spannable = (Spannable) holder.tvAssignmentName.getText();
        spannable.setSpan(STRIKETHROUGH_SPAN, 0, currentAssignment.getCourseName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvDueDate.setText(currentAssignment.getCourseName(), TextView.BufferType.SPANNABLE);
        spannable = (Spannable) holder.tvDueDate.getText();
        spannable.setSpan(STRIKETHROUGH_SPAN, 0, currentAssignment.getCourseName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void setAssignmentsByDueDate(List<CourseAssignmentJoin> assignmentsByDueDate) {
        this.assignmentsByDueDate = assignmentsByDueDate;
        notifyDataSetChanged();
    }

    public class CourseAssignmentJoinHolder extends RecyclerView.ViewHolder {

        private TextView tvCourseName;
        private TextView tvAssignmentName;
        private TextView tvDueDate;

        public CourseAssignmentJoinHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tv_item_course_assignment_join_course_name);
            tvAssignmentName = itemView.findViewById(R.id.tv_item_course_assignment_join_assignment_name);
            tvDueDate = itemView.findViewById(R.id.tv_item_course_assignment_join_assignment_due_date);
        }
    }
}
