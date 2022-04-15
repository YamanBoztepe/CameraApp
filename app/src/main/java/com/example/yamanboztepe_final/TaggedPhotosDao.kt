package com.example.yamanboztepe_final

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaggedPhotosDao {

    @Insert
    fun insert(photo: TaggedPhotos)

    @Delete
    fun delete(photo: TaggedPhotos)

    @Query("SELECT * FROM taggedPhotos WHERE tag=:tag")
    fun getPhotos(tag: String): List<TaggedPhotos>
}