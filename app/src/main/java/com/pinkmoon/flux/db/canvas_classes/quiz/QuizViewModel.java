package com.pinkmoon.flux.db.canvas_classes.quiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinkmoon.flux.API.Quiz;
import com.pinkmoon.flux.API.Course;

import java.util.List;

import java.util.List;

public class QuizViewModel extends AndroidViewModel
{
    private QuizRepository quizRepository;

    private LiveData<List<Quiz>> allQuizzes;

    private MutableLiveData<List<Quiz>> listOfCanvasQuizzesByCourse;

    public QuizViewModel(@NonNull Application application) {
        super(application);

        quizRepository = new QuizRepository(application);

        allQuizzes = quizRepository.getAllQuizzes();
    }

    public void insertQuiz(Quiz... quiz){
        quizRepository.insertQuiz(quiz);
    }

    public void updateQuiz(Quiz quiz){
        quizRepository.updateQuiz(quiz);
    }

    public void deleteQuiz(Quiz quiz){
        quizRepository.deleteQuiz(quiz);
    }

    public LiveData<List<Quiz>> getAllQuizzes() {
        return allQuizzes;
    }

    public MutableLiveData<List<Quiz>> getListOfCanvasQuizzes(List<Course> courses) {
        listOfCanvasQuizzesByCourse = quizRepository.getListOfCanvasQuizzesByCourse(courses);
        return listOfCanvasQuizzesByCourse;
    }
}