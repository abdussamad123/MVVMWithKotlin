package com.mvvm.appcomponent.data.database.daos

import androidx.room.*
import com.mvvm.appcomponent.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserEntity)

    @Query("SELECT * FROM user where id = (:userId) ")
    suspend fun getUserData(userId: Int): UserEntity

    @Query("SELECT * FROM user")
    fun getUserDataFlow(): Flow<UserEntity>
}