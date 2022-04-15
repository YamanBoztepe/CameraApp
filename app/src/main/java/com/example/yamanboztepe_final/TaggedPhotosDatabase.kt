package com.example.yamanboztepe_final

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaggedPhotos::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaggedPhotosDatabase : RoomDatabase() {

    abstract fun photosDao(): TaggedPhotosDao

    companion object {
        private var instance: TaggedPhotosDatabase? = null

        fun getPhotosDatabase(context: Context): TaggedPhotosDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    TaggedPhotosDatabase::class.java,
                    "taggedPhotos.db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}