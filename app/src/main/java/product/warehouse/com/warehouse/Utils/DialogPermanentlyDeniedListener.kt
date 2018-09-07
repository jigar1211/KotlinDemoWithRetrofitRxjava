package product.warehouse.com.warehouse.Utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.fondesa.kpermissions.extension.flatString
import com.fondesa.kpermissions.request.PermissionRequest
import product.warehouse.com.warehouse.R

class DialogPermanentlyDeniedListener(private val context: Context) : PermissionRequest.PermanentlyDeniedListener {

    override fun onPermissionsPermanentlyDenied(permissions: Array<out String>) {
        val msg = String.format(context.getString(R.string.permanently_denied_permissions),
                permissions.flatString())

        AlertDialog.Builder(context)
                .setTitle(R.string.permissions_required)
                .setMessage(msg)
                .setPositiveButton(R.string.action_settings) { _, _ ->
                    // Open the app's settings.
                    val intent = createAppSettingsIntent()
                    context.startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun createAppSettingsIntent() = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
    }
}