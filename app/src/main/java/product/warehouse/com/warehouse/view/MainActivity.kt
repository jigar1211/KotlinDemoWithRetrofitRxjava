package product.warehouse.com.warehouse.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.fondesa.kpermissions.request.runtime.nonce.PermissionNonce
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.loading.*
import product.warehouse.com.warehouse.R
import product.warehouse.com.warehouse.Utils.DialogPermanentlyDeniedListener
import product.warehouse.com.warehouse.Utils.DialogRationaleListener
import product.warehouse.com.warehouse.Utils.ProgressDialog
import product.warehouse.com.warehouse.remote.ApiClient

class MainActivity : AppCompatActivity(), View.OnClickListener, PermissionRequest.AcceptedListener,
        PermissionRequest.DeniedListener,
        PermissionRequest.PermanentlyDeniedListener,
        PermissionRequest.RationaleListener {
    companion object {
        lateinit var userId: String
    }
    private lateinit var productQAT: String
    private var isDialogVisible = false
    private lateinit var progressdialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mappingData()
        getNewUserId()
    }

    private val request by lazy {
        permissionsBuilder(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .build()
    }

    private fun getNewUserId() {
        showProgress()
        ApiClient.getApiInterface(this).getUserID()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        userId = result.body()!!.userId
                        productQAT = result.body()!!.product
                    } else {
                        Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                }, { throwable ->
                    Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
                    dismissProgress()
                }, {
                    dismissProgress()
                })
    }

    /**
     * mapping data of widgets
     * */
    private fun mappingData() {
        request.send()
        request.acceptedListener(this)
        request.deniedListener(this)
        request.permanentlyDeniedListener(DialogPermanentlyDeniedListener(this))
        request.rationaleListener(DialogRationaleListener(this))
        etProductSearch.setOnClickListener(this)
        ivBarcodeScan.setOnClickListener(this)
        tvTitle.text = this.resources.getString(R.string.product_scan)
    }

    /**
     * Click events of widgets
     * */
    override fun onClick(v: View) {
        var intent: Intent? = null

        when (v.id) {

            R.id.etProductSearch -> {
                val intent = Intent(this, ProductSearch::class.java)
                intent.putExtra("UserId", userId)
                startActivity(intent)
            }

            R.id.ivBarcodeScan -> { startActivity(Intent(this, BarcodeScan::class.java)) }
        }
    }


    /**
     * Method to display progress
     */
    fun showProgress() {
        if (!isFinishing && !isDialogVisible) {
            progressdialog = ProgressDialog(this, android.R.style.Theme_Translucent_NoTitleBar)
            progressdialog.setContentView(R.layout.loading)
            progressdialog.setCancelable(false)
            progressdialog.show()
            progressdialog.loading_icon
                    .startAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_anim))
            isDialogVisible = true
        }
    }

    /**
     * Method to dismiss progress
     */
    fun dismissProgress() {

        if (!isFinishing && isDialogVisible && progressdialog.isShowing) {
            progressdialog.dismiss()
            isDialogVisible = false
        }
    }

    override fun onPermissionsAccepted(permissions: Array<out String>) {

    }

    override fun onPermissionsDenied(permissions: Array<out String>) {

    }

    override fun onPermissionsPermanentlyDenied(permissions: Array<out String>) {

    }

    override fun onPermissionsShouldShowRationale(permissions: Array<out String>, nonce: PermissionNonce) {

    }
}
