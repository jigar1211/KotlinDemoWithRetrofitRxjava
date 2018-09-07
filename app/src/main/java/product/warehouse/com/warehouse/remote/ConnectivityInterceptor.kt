package product.warehouse.com.warehouse.remote

import android.app.Activity
import okhttp3.Interceptor
import okhttp3.Response
import product.warehouse.com.warehouse.R
import product.warehouse.com.warehouse.Utils.CommonUtils
import java.io.IOException

class ConnectivityInterceptor internal constructor(private val activity: Activity) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val isNetworkActive = CommonUtils.isOnline(activity)
        return if (!isNetworkActive) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }

    inner class NoConnectivityException : IOException() {
        override var message = activity.getString(R.string.no_internet_available)
    }
}
