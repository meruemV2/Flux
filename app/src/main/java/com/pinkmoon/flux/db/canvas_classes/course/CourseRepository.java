package com.pinkmoon.flux.db.canvas_classes.course;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

/**
 * Handles all operations that will interact with the DB and API.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class CourseRepository {
    private Application application;
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        this.application = application;

        FluxDB fluxDB = FluxDB.getInstance(application);
        courseDao = fluxDB.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public void insertCourse(Course course){
        new InsertCourseAsync(courseDao).execute(course);
    }

    public void updateCourse(Course course){
        new UpdateCourseAsync(courseDao).execute(course);
    }

    public void deleteCourse(Course course){
        new DeleteCourseAsync(courseDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public class InsertCourseAsync extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        public InsertCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insertCourse(courses[0]);
            return null;
        }
    }

    public class UpdateCourseAsync extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        public UpdateCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.updateCourse(courses[0]);
            return null;
        }
    }

    public class DeleteCourseAsync extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        public DeleteCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.deleteCourse(courses[0]);
            return null;
        }
    }
}
