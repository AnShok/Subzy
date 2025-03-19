package com.anshok.subzy.di

import androidx.room.Room
import com.anshok.subzy.data.NetworkClient
import com.anshok.subzy.data.interceptors.HeaderInterceptor
import com.anshok.subzy.data.interceptors.LoggingInterceptor
import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.data.remote.RemoteDataSource
import com.anshok.subzy.data.local.db.AppDatabase
import com.anshok.subzy.data.local.impl.LocalDataSourceImpl
import com.anshok.subzy.data.remote.impl.RemoteDataSourceImpl
import com.anshok.subzy.data.remote.search.network.LogoApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    // Инициализация Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "subscription_database"
        ).build()
    }

    // DAO
    single { get<AppDatabase>().subscriptionDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().paymentMethodDao() }
    single { get<AppDatabase>().reminderDao() }

    // LocalDataSource
    single<LocalDataSource> {
        LocalDataSourceImpl(
            subscriptionDao = get(),
            categoryDao = get(),
            paymentMethodDao = get(),
            reminderDao = get()
        )
    }

    // RemoteDataSource
    single<RemoteDataSource> {
        RemoteDataSourceImpl(logoApiService = get())
    }

    // NetworkClient
    single<NetworkClient> {
        com.anshok.subzy.data.remote.search.network.RetrofitNetworkClient(
            logoApiService = get(),
            context = androidContext()
        )
    }

    // Retrofit и LogoApiService
    single<LogoApiService> {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor) // Логирование запросов
            .addInterceptor(HeaderInterceptor) // Добавление заголовков
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.logo.dev/") // Базовый URL вашего API
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LogoApiService::class.java)
    }

    // Gson (опционально, если используется вне Retrofit)
    single { Gson() }
}