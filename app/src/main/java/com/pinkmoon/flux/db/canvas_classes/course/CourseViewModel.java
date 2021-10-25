package com.pinkmoon.flux.db.canvas_classes.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinkmoon.flux.API.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private CourseRepository courseRepository;

    private LiveData<List<Course>> allCourses;

    //private List<Course> allCoursesFromAPI;
    private MutableLiveData<List<Course>> listOfCanvasCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);

        courseRepository = new CourseRepository(application);

        allCourses = courseRepository.getAllCourses();
        listOfCanvasCourses = courseRepository.getListOfCanvasCourses();
    }

    public void insertCourse(Course course){
        courseRepository.insertCourse(course);
    }

    public void updateCourse(Course course){
        courseRepository.updateCourse(course);
    }

    public void deleteCourse(Course course){
        courseRepository.deleteCourse(course);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public MutableLiveData<List<Course>> getListOfCanvasCourses() {
        return listOfCanvasCourses;
    }
}
