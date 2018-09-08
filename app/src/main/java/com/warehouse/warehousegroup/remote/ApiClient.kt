package com.warehouse.warehousegroup.remote

import android.app.Activity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.warehouse.warehousegroup.remote.ApiInterface
import com.warehouse.warehousegroup.remote.ConnectivityInterceptor
import com.warehouse.warehousegroup.remote.RemoteHost
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiClient {

    fun getApiInterface(activity: Activity): ApiInterface {
        return getRetrofitInstance(activity).create(ApiInterface::class.java)
    }

    /**
    * Implement Retrofit instance
    * */
     fun getRetrofitInstance(activity: Activity): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<out X509Certificate>? {
                return arrayOf()
            }


            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ConnectivityInterceptor(activity))
                .addInterceptor { chain ->
                    val original = chain.request()
                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                            .header("Ocp-Apim-Subscription-Key", "f09e11ff7d4543ad8adc9808d5ebe34d") // <-- this is the important line
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
                .build()

        return Retrofit.Builder()
                .baseUrl(RemoteHost.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

}