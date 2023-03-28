package com.mvvm.appcomponent.di

import android.content.Context
import androidx.room.Room
import com.mvvm.BuildConfig
import com.mvvm.appcomponent.data.database.AppDatabase
import com.mvvm.appcomponent.data.database.daos.UserDao
import com.mvvm.appcomponent.data.remote.AppLevelApi
import com.mvvm.appcomponent.util.CustomInterceptor
import com.mvvm.appcomponent.util.TokenAuthenticator
import com.mvvm.appcomponent.util.TokenAuthenticatorPreLogin
import com.mvvm.appcomponent.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherAppLevelApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthAppLevelApi



    @Provides
    @OtherInterceptorOkHttpClient
    fun providesRetrofitForPreLogin(tokenAuthenticator: TokenAuthenticatorPreLogin,
                                    userPreferences: UserPreferences,
                                    @ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(getRetrofitClient(tokenAuthenticator, userPreferences = userPreferences,
                isPostLogin = false, context = context))
            .addConverterFactory(
                MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @AuthInterceptorOkHttpClient
    fun providesRetrofitForPostLogin(tokenAuthenticator: TokenAuthenticator,
                                     userPreferences: UserPreferences,
                                     @ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(getRetrofitClient(tokenAuthenticator, userPreferences = userPreferences, context = context))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }



    private fun getRetrofitClient(authenticator: Authenticator? = null,
                                  userPreferences: UserPreferences,
                                  isPostLogin: Boolean = true,
                                  isAuthenticationRequired:Boolean=true,
                                  context: Context
    ) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            CustomInterceptor(isPostLogin,userPreferences,isAuthenticationRequired,context)
        ).also { client ->
            authenticator?.let { client.authenticator(it) }
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(
                    HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build()
    }

    @Provides
    @OtherAppLevelApi
    fun providesOtherAppLevelApi(@AppModule.OtherInterceptorOkHttpClient retrofit: Retrofit)
            : AppLevelApi = retrofit.create(AppLevelApi::class.java)

    @Provides
    @AuthAppLevelApi
    fun providesAuthAppLevelApi(@AppModule.AuthInterceptorOkHttpClient retrofit: Retrofit)
            : AppLevelApi = retrofit.create(AppLevelApi::class.java)

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "mvvm_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providesIntakeLogDao(database: AppDatabase): UserDao {
        return database.getUserDao()
    }

}