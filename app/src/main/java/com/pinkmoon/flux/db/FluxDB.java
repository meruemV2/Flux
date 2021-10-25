package com.pinkmoon.flux.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentDao;
import com.pinkmoon.flux.db.canvas_classes.course.CourseDao;
import com.pinkmoon.flux.db.category.Category;
import com.pinkmoon.flux.db.category.CategoryDao;
import com.pinkmoon.flux.db.task.Task;
import com.pinkmoon.flux.db.task.TaskDao;

@Database(entities = {
        Assignment.class,
        Category.class,
        Course.class,
        Task.class,
        }, version = 1)
public abstract class FluxDB extends RoomDatabase {

    private static FluxDB instance;

    // Dao Declarations
    public abstract AssignmentDao       assignmentDao();
    public abstract CategoryDao         categoryDao();
    public abstract CourseDao           courseDao();
    public abstract TaskDao             taskDao();

    /**
     * Forms a single instance of the database object that will
     * survive as long as the application does.
     * @param context the current application context.
     * @return a single instance of the database object.
     */
    public static synchronized FluxDB getInstance(Context context) {
        // we check if it is null, because if it is, it means it has
        // yet to be created and we create it if that's the case. If it
        // is not null, we just return an instance of the already
        // existing object.
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FluxDB.class, "flux_db")
                    .build();
        }
        return instance;
    }

    // Migrations can be added below this line if they are needed later
}
