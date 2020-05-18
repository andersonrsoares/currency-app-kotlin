package br.com.currencyconverter.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.com.currencyconverter.BuildConfig
import br.com.currencyconverter.api.ConvertResponse
import br.com.currencyconverter.api.CurrencyDto
import br.com.currencyconverter.api.FixerApiService
import br.com.currencyconverter.extras.Constants
import br.com.currencyconverter.persistence.AppDatabase
import br.com.currencyconverter.persistence.CurrencyDao
import br.com.currencyconverter.repository.CurrencyRepository
import br.com.currencyconverter.repository.CurrencyRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
object AppModule{

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application
            .getSharedPreferences(
                "${application.packageName}_prefs",
                Context.MODE_PRIVATE
            )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(ConvertResponse::class.java,  JsonDeserializer<ConvertResponse> { arg0, _, _ ->
                val obj = arg0.asJsonObject
                var response: ConvertResponse
                if(obj.has("rates")){
                    val s = obj.getAsJsonObject("rates").asJsonObject
                    val list = arrayListOf<CurrencyDto>()

                    s.keySet().iterator().forEach {
                        list.add(CurrencyDto(s.get(it).asDouble,it))
                    }
                    obj.remove("rates")
                    response = Gson().fromJson(obj, ConvertResponse::class.java)
                    response.rates = list
                }else{
                    response = Gson().fromJson(arg0, ConvertResponse::class.java)
                }
                response
            })
            .create()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpCache(
        application: Application
    ): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
       return Cache(application.cacheDir, cacheSize)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpInterceptorAuth(): Interceptor {
        return Interceptor { chain ->
            val url: HttpUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("access_key", "ded1ea21b214709a30f82793ca5df5d6")
                .build()


            val request = chain.request().newBuilder()
                .url(url)
                .addHeader("Content-Type", "application/json")

            return@Interceptor chain.proceed(request.build())
        }
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpBuilder(cache:Cache,interceptor: Interceptor,logInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {

        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .connectTimeout(6000, TimeUnit.MILLISECONDS)
            .readTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .cache(cache)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(logInterceptor)
        }

        return okHttpClientBuilder
    }



    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(gsonBuilder:  Gson, okHttpClientBuilder: OkHttpClient.Builder): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .client(okHttpClientBuilder.build())
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): FixerApiService {
        return retrofitBuilder
            .build()
            .create(FixerApiService::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCurrencyRepository(
        apiService: FixerApiService,
        currencyDao: CurrencyDao
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(
            apiService,
            currencyDao
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCurrencyDao(db: AppDatabase): CurrencyDao {
        return db.getCurrencyDao()
    }

}