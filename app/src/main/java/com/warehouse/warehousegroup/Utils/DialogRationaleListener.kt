package com.warehouse.warehousegroup.Utils

import android.content.Context
import android.support.v7.app.AlertDialog
import com.fondesa.kpermissions.extension.flatString
import com.fondesa.kpermissions.request.PermissionRequest
import com.fondesa.kpermissions.request.runtime.nonce.PermissionNonce
import product.warehouse.com.warehouse.R

class DialogRationaleListener(private val context: Context) : PermissionRequest.RationaleListener {

    override fun onPermissionsShouldShowRationale(permissions: Array<out String>, nonce: PermissionNonce) {
        val msg = String.format(context.getString(R.string.rationale_permissions),
                permissions.flatString())

        AlertDialog.Builder(context)
                .setTitle(R.string.permissions_required)
                .setMessage(msg)
                .setPositiveButton(R.string.request_again) { _, _ ->
                    // Send the request again.
                    nonce.use()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }
}