package com.pinkmoon.flux.db.canvas_classes.quiz;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pinkmoon.flux.API.Quiz;

import java.util.List;

@Dao
public interface QuizDao
{
    @Insert(onConflict = REPLACE)
    void insertQuiz(Quiz... quiz);

    @Update
    void updateQuiz(Quiz quiz);

    @Delete
    void deleteQuiz(Quiz quiz);

    @Query("SELECT * FROM table_quiz")
    LiveData<List<Quiz>> getAllQuizzes();
}
