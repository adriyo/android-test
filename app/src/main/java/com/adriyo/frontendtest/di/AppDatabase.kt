package com.adriyo.frontendtest.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adriyo.frontendtest.data.db.dao.PhotoDao
import com.adriyo.frontendtest.data.db.dao.UserDao
import com.adriyo.frontendtest.data.db.entity.PhotoEntity
import com.adriyo.frontendtest.data.db.entity.UserEntity

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@Database(
    entities = [UserEntity::class, PhotoEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun photoDao(): PhotoDao
}