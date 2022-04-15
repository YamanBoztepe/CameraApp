package com.example.yamanboztepe_final

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taggedPhotos")
data class TaggedPhotos(
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "photos") val photos: Bitmap,
) {
    @PrimaryKey(autoGenerate = true) var photoID: Int = 0
}