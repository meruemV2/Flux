package com.pinkmoon.flux.API;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "table_quiz")
public class Quiz
{
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @NonNull
    private String quizId;

    @SerializedName("name")
    private String quizName;

    @SerializedName("course_id")
    private String quizCourseId;

    //    This may be null
    @SerializedName("unlock_at")
    private String quizOpenDate;

    @SerializedName("due_at")
    private String quizDueDate;

    @ColumnInfo(defaultValue = "false")
    private boolean isComplete;

    public Quiz(@NonNull String quizId, String quizName, String quizCourseId,
                String quizOpenDate, String quizDueDate , Boolean isComplete) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizCourseId = quizCourseId;
        this.quizOpenDate = quizOpenDate;
        this.quizDueDate = quizDueDate;
        this.isComplete = isComplete;
    }

    @NonNull
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizCourseId() {
        return quizCourseId;
    }

    public void setQuizCourseId(String quizCourseId) {
        this.quizCourseId = quizCourseId;
    }

    public String getQuizOpenDate() {
        return quizOpenDate;
    }

    public void setQuizOpenDate(String quizOpenDate) {
        this.quizOpenDate = quizOpenDate;
    }

    public String getQuizDueDate() {
        return quizDueDate;
    }

    public void setQuizDueDate(String quizDueDate) {
        this.quizDueDate = quizDueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
