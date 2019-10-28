package com.travel721;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventCardDao {

    @Query("SELECT * FROM eventcard")
    List<EventCard> getAll();


    @Insert
    void insertAll(EventCard... eventCards);

    @Delete
    void delete(EventCard eventCard);
}
