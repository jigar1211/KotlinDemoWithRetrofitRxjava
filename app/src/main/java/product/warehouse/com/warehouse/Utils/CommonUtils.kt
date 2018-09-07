package product.warehouse.com.warehouse.Utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

object CommonUtils {
    fun isOnline(activity: Activity): Boolean {
        var isConnected: Boolean? = false
        try {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            isConnected = cm.activeNetworkInfo != null
                    && cm.activeNetworkInfo.isAvailable
                    && cm.activeNetworkInfo.isConnected
            if (isConnected) return isConnected
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isConnected!!
    }
}