package com.pinkmoon.flux.db.canvas_classes.assignment;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

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

    @Insert(onConflict = REPLACE)
    void insertAssignment(Assignment... assignment);

    @Update
    void updateAssignment(Assignment assignment);

    @Delete
    void deleteAssignment(Assignment assignment);

    @Query("SELECT * FROM table_assignment")
    LiveData<List<Assignment>> getAllAssignments();

    @Query("SELECT * FROM table_assignment WHERE assignmentDueDate LIKE :yearMonth || '%' ORDER BY assignmentDueDate ASC")
    LiveData<List<Assignment>> getAssignmentsByYearMonth(String yearMonth);

    @Query("UPDATE table_assignment SET isComplete = :reminderStatus WHERE assignmentId = :reminderId")
    void tagReminderAsComplete(int reminderStatus, int reminderId);
}
