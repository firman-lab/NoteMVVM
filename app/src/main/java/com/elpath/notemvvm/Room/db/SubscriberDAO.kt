package com.elpath.notemvvm.Room.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber) :Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber) : Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber) : Int

    @Query("Delete FROM subscriber_data_table")
    suspend fun deleteAll() : Int

    @Query("Select * From subscriber_data_table")
    fun getAllSubscriber():LiveData<List<Subscriber>>
}