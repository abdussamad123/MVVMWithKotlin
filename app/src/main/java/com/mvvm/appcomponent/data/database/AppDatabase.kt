package com.mvvm.appcomponent.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvvm.appcomponent.data.database.daos.UserDao
import com.mvvm.appcomponent.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

}