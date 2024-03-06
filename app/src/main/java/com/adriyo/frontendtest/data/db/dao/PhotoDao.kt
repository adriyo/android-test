package com.adriyo.frontendtest.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.adriyo.frontendtest.data.db.entity.PhotoEntity

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Dao
abstract class PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(photo: PhotoEntity)

    @Transaction
    open suspend fun save(list: List<PhotoEntity>) {
        list.onEach { save(it) }
    }

    @Query("SELECT * FROM photos ORDER BY albumId ASC")
    abstract fun getPhotosPaged(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photos")
    abstract suspend fun clearAll()

}