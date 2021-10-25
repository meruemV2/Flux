package com.pinkmoon.flux.db.canvas_classes.assignment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pinkmoon.flux.API.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {

    @Insert
    void insertAssignment(Assignment assignment);

    @Update
    void updateAssignment(Assignment assignment);

    @Delete
    void deleteAssignment(Assignment assignment);

    @Query("SELECT * FROM table_assignment")
    LiveData<List<Assignment>> getAllAssignments();
}
