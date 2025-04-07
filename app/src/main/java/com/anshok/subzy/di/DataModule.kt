package com.anshok.subzy.di

import androidx.room.Room
import com.anshok.subzy.data.NetworkClient
import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.data.local.db.AppDatabase
import com.anshok.subzy.data.local.impl.LocalDataSourceImpl
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.data.remote.currency.network.CbrApiService
import com.anshok.subzy.data.remote.interceptors.HeaderInterceptor
import com.anshok.subzy.data.remote.interceptors.LoggingInterceptor
import com.anshok.subzy.data.remote.logo.LogoRemoteDataSource
import com.anshok.subzy.data.remote.logo.impl.LogoRemoteDataSourceImpl
import com.anshok.subzy.data.remote.logo.network.LogoApiService
import com.anshok.subzy.data.remote.logo.network.RetrofitNetworkClient
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val dataModule = module {

    // Room database
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

    // Local data
    single<LocalDataSource> {
        LocalDataSourceImpl(
            subscriptionDao = get(),
            categoryDao = get(),
            paymentMethodDao = get(),
            reminderDao = get()
        )
    }

    // 🌐 Retrofit: Logo API
    single<LogoApiService> {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor)
            .addInterceptor(HeaderInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.logo.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LogoApiService::class.java)
    }

    // 🌐 Retrofit: CBR API (Currency)
    single<CbrApiService> {
        Retrofit.Builder()
            .baseUrl("https://www.cbr.ru/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(CbrApiService::class.java)
    }

    // 🌐 RemoteDataSources
    single<LogoRemoteDataSource> { LogoRemoteDataSourceImpl(get()) }

    // 🌐 NetworkClient (Logo)
    single<NetworkClient> {
        RetrofitNetworkClient(
            logoApiService = get(),
            context = androidContext()
        )
    }

    // Preferences
    single { UserPreferences(androidContext()) }

    // 🔧 JSON
    single { Gson() }
}