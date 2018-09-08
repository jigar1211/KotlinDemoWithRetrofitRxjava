package com.warehouse.warehousegroup.view

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.loading.*
import product.warehouse.com.warehouse.R
import com.warehouse.warehousegroup.Utils.ProgressDialog
import com.warehouse.warehousegroup.model.PriceModel
import com.warehouse.warehousegroup.remote.ApiClient

class ProductDetail : AppCompatActivity() {

    private lateinit var priceModel: PriceModel
    private var isDialogVisible = false
    private lateinit var progressdialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener {
            onBackPressed()
        }
        getPriceData()
    }

    /**
     * API call for getting product search results
     * */

    private fun getPriceData() {
        val barcode: String = intent.getStringExtra("barcode")
        showProgress()
        ApiClient.getApiInterface(this).getPriceList(barcode, MainActivity.userId, MainActivity.userId, "208", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        priceModel = result.body()!!
                        mappingData()
                    } else {
                        Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                    dismissProgress()
                }, { throwable ->
                    Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
                    dismissProgress()
                }, {
                    dismissProgress()
                })
    }

    /**
     * mapping data with layout widgets
     * */

    private fun mappingData() {
        tvTitle.text = priceModel.Product.Description
        ivProductImage.setImageURI(Uri.parse(priceModel.Product.ImageURL))
        tvPrice.text = priceModel.Product.Price.Type + " " + priceModel.Product.Price.Price
        tvProductDescription.text = priceModel.Product.Description
        tvProductName.text = priceModel.Product.Class0
        tvScanDateTime.text = priceModel.ScanDateTime
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

    /**
     * handel back button of device
     * */
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
