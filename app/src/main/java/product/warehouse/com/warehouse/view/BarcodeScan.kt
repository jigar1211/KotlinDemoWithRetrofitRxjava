package product.warehouse.com.warehouse.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import product.warehouse.com.warehouse.R
import java.util.*

class BarcodeScan : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private val FLASH_STATE = "FLASH_STATE"
    private val AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE"
    private val SELECTED_FORMATS = "SELECTED_FORMATS"
    private val CAMERA_ID = "CAMERA_ID"
    private lateinit var mScannerView: ZXingScannerView
    private var mFlash: Boolean = false
    private var mAutoFocus: Boolean = false
    private var mSelectedIndices: ArrayList<Int>? = null
    private var mCameraId = -1
    internal lateinit var purpose: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan)
        setToolBar()
        if (savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false)
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true)
            mSelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS)
            mCameraId = savedInstanceState.getInt(CAMERA_ID, -1)
        } else {
            mFlash = false
            mAutoFocus = true
            mSelectedIndices = null
            mCameraId = -1
        }
        val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
        mScannerView = ZXingScannerView(this)
        setupFormats()
        contentFrame.addView(mScannerView)
        getIntentData()
    }

    fun setupFormats() {
        val formats = ArrayList<BarcodeFormat>()
        if (mSelectedIndices == null || mSelectedIndices!!.isEmpty()) {
            mSelectedIndices = ArrayList()
            for (i in ZXingScannerView.ALL_FORMATS.indices) {
                mSelectedIndices!!.add(i)
            }
        }

        for (index in mSelectedIndices!!) {
            formats.add(ZXingScannerView.ALL_FORMATS[index])
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats)
        }
    }

    fun getIntentData() {
        if (intent.hasExtra("purpose")) {
            purpose = intent.getStringExtra("purpose")
        }
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setPadding(0, 0, 0, 0)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(false)
            ab.setDisplayShowTitleEnabled(false)
            val mInflater = LayoutInflater.from(this)
            val mCustomView = mInflater.inflate(R.layout.app_bar_layout, null)
            mCustomView.tvTitle.text = this.resources.getString(R.string.product_scan)
            mCustomView.tvTitle.isSelected = true
            mCustomView.tvTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
            mCustomView.tvTitle.setSingleLine(true)
            mCustomView.ivBack.visibility = View.VISIBLE
            mCustomView.ivBack.setOnClickListener { onBackPressed() }
            ab.customView = mCustomView
            ab.setDisplayShowCustomEnabled(true)
        }

    }

    override fun handleResult(result: Result) {
        val intent = Intent(this, ProductDetail::class.java)
        intent.putExtra("barcode", result.text)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (mScannerView != null) {
            mScannerView.setResultHandler(this)
            mScannerView.startCamera(mCameraId)
            mScannerView.flash = mFlash
            mScannerView.setAutoFocus(mAutoFocus)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(FLASH_STATE, mFlash)
        outState?.putBoolean(AUTO_FOCUS_STATE, mAutoFocus)
        outState?.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices)
        outState?.putInt(CAMERA_ID, mCameraId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var menuItem: MenuItem

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on)
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off)
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER)


        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on)
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off)
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_flash -> {
                mFlash = !mFlash
                if (mFlash) {
                    item.setTitle(R.string.flash_on)
                } else {
                    item.setTitle(R.string.flash_off)
                }
                mScannerView?.flash = mFlash
                return true
            }
            R.id.menu_auto_focus -> {
                mAutoFocus = !mAutoFocus
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on)
                } else {
                    item.setTitle(R.string.auto_focus_off)
                }
                mScannerView?.setAutoFocus(mAutoFocus)
                return true
            }
            else -> return super.onOptionsItemSelected(item)

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
