package com.example.students.di.network

import com.example.students.utils.GlobalPreferences
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://www.unik.kz/"

val networkModule = module {
    single { provideRetrofit() }
}

val logging = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BODY)
}
val httpClient = OkHttpClient.Builder().apply {
    addInterceptor(logging)
    addInterceptor(HeadersInterceptor())
}

private fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
//        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(httpClient.build())
        .build()


class HeadersInterceptor() : Interceptor {

    private val preferences: GlobalPreferences by inject(GlobalPreferences::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Accept", "*/*")
            .addHeader("Authorization", "Bearer ${preferences.getAccessToken()}")
            .build()

        return chain.proceed(req)
    }
}