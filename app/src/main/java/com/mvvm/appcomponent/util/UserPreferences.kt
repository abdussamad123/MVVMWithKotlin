package com.mvvm.appcomponent.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "mvvm_data_store")

class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
     val applicationContext: Context = context.applicationContext
     val coroutineScope = CoroutineScope(Dispatchers.IO)
     val gson = Gson()


    suspend fun saveStringDataToDataStore(key: String, data: String) {
        log("DATA-->", "saveStringDataToDataStore")
        val saveStringKey = stringPreferencesKey(key)
        applicationContext.dataStore.edit {
            it[saveStringKey] = data
        }
    }

    fun getStringDataFromDataStore(key: String): String {
        log("DATA-->", "getStringDataFromDataStore")
        val getStringKey = stringPreferencesKey(key)
        var value = ""
        runBlocking {
            value = applicationContext.dataStore.data.map {
                it[getStringKey] ?: ""
            }.first()
        }
        return value
    }

    suspend fun saveIntDataToDataStore(key: String, data: Int) {
        log("DATA-->","saveIntDataToDataStore")
        val saveIntKey = intPreferencesKey(key)
        applicationContext.dataStore.edit {
            it[saveIntKey] = data
        }
    }

    fun getIntDataFromDataStore(key: String): Int {
        log("DATA-->","getIntDataFromDataStore")
        val getIntKey = intPreferencesKey(key)
        var value = 0
        runBlocking {
            value = applicationContext.dataStore.data.map {
                it[getIntKey] ?: 0
            }.first()
        }
        return value

    }


    suspend fun saveLongDataToDataStore(key: String, data: Long) {
        log("DATA-->","saveLongDataToDataStore")
        val saveLongKey = longPreferencesKey(key)
        applicationContext.dataStore.edit {
            it[saveLongKey] = data
        }
    }

    fun getLongDataFromDataStore(key: String): Long {
        log("DATA-->","getIntDataFromDataStore")
        val getLongKey = longPreferencesKey(key)
        var value = 0L
        runBlocking {
            value = applicationContext.dataStore.data.map {
                it[getLongKey] ?: 0
            }.first()
        }
        return value

    }


    suspend fun saveBoolDataToDataStore(key: String, data: Boolean) {
        log("DATA-->","saveBoolDataToDataStore")
        val saveBoolKey = booleanPreferencesKey(key)
        applicationContext.dataStore.edit {
            it[saveBoolKey] = data
        }
    }

    fun getBooleanDataFromDataStore(key: String): Boolean {
        log("DATA-->","getBooleanDataFromDataStore")
        val getBoolKey = booleanPreferencesKey(key)
        var value = false
        runBlocking {
            value = applicationContext.dataStore.data.map {
                it[getBoolKey] ?: false
            }.first()
        }
        return value
    }

    suspend fun <T> saveObjectDataToDataStore(key: String, data: T) {
        log("DATA-->","saveObjectDataToDataStore")
        val dataAsString = gson.toJson(data)
        val saveObjectKey = stringPreferencesKey(key)
        applicationContext.dataStore.edit {
            it[saveObjectKey] = dataAsString
        }

    }

    inline fun <reified T> getObjectDataFromStore(key: String): T {
        log("DATA-->","getObjectDataFromStore")
        val getObjectKey = stringPreferencesKey(key)
        var  value:T
        runBlocking {
            value=  applicationContext.dataStore.data.map {
                gson.fromJson(it[getObjectKey], T::class.java)
            }.first()
        }
        return value
    }

    suspend fun clear() {
        log("DATA-->","UserPreferenceClear")
        applicationContext.dataStore.edit {
            it.clear()
        }
    }


}