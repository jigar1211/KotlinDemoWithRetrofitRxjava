package com.warehouse.warehousegroup.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_search.*
import kotlinx.android.synthetic.main.appbar_layout_product_search.*
import product.warehouse.com.warehouse.R
import com.warehouse.warehousegroup.adapter.SearchAdapter
import com.warehouse.warehousegroup.model.SearchModel
import com.warehouse.warehousegroup.remote.ApiClient


class ProductSearch : AppCompatActivity(), OnDSListener {
    lateinit var searchResult: SearchModel
    lateinit var searchListAdapter: SearchAdapter
    private lateinit var droidSpeech: DroidSpeech
    private var recognizingSpeech = false
    private var flagSearchIcon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_search)
        mappingData()
    }

    /**
     * API call for get search result
     * */
    private fun getSearchData(newText: String) {
        val userId: String = intent.getStringExtra("UserId")
        findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
        ApiClient.getApiInterface(this).getSearchList("", "",
                "", "", newText, "", userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        searchResult = result.body()!!
                        setCustomersAdapter()
                    } else {
                        Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                    findViewById<View>(R.id.progress_bar).visibility = View.GONE
                }, {
                    findViewById<View>(R.id.progress_bar).visibility = View.GONE
                }, {
                    findViewById<View>(R.id.progress_bar).visibility = View.GONE
                })

    }

    private fun setCustomersAdapter() {
        searchListAdapter = SearchAdapter(this, searchResult.Results)
        rvSearchList.layoutManager = LinearLayoutManager(this)
        rvSearchList.adapter = searchListAdapter
    }

    private fun mappingData() {

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            flagSearchIcon = if (hasFocus) {
                setSearchHintIcon(R.drawable.arrow_back)
                true
            } else {
                setSearchHintIcon(android.R.drawable.ic_menu_search)
                false
            }
        }
        droidSpeech = DroidSpeech(this, null)
        droidSpeech.setOnDroidSpeechListener(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getSearchData(newText)
                return false
            }
        })
    }

    /**
     * set hint icon for search
     * */
    private fun setSearchHintIcon(resourceId: Int) {
        val searchHintIcon = findViewById(this.searchView!!,
                "android:id/search_mag_icon") as ImageView
        searchHintIcon.setImageResource(resourceId)
        if (flagSearchIcon) {
            searchHintIcon.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun findViewById(v: View, id: String): View {
        return v.findViewById(v.context.resources.getIdentifier(id, null, null))
    }

    /**
     * handle search speech recognition
     * */
    fun handleSpeechRecognition(view: View) {
        if (!recognizingSpeech) {
            recognizingSpeech = true
            droidSpeech.startDroidSpeechRecognition()
            (view as ImageView).setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE

        } else {
            droidSpeech.closeDroidSpeechOperations()
            recognizingSpeech = false
            (view as ImageView).setImageResource(android.R.drawable.ic_btn_speak_now)
            findViewById<View>(R.id.progress_bar).visibility = View.GONE
        }
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

    }

    override fun onDroidSpeechSupportedLanguages(currentSpeechLanguage: String?, supportedSpeechLanguages: MutableList<String>?) {

    }

    override fun onDroidSpeechError(errorMsg: String?) {

    }

    override fun onDroidSpeechClosedByUser() {

    }

    override fun onDroidSpeechLiveResult(liveSpeechResult: String?) {

    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        searchView?.setQuery(finalSpeechResult, false)
    }

    /**
     * handel back button of device
     * */
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
