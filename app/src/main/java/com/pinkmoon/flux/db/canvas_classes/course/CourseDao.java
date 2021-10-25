package com.pinkmoon.flux.db.canvas_classes.course;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pinkmoon.flux.API.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM table_course")
    LiveData<List<Course>> getAllCourses();
}
