package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private var tempText = ""

    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusLayout: LinearLayout
    private lateinit var statusImage: ImageView
    private lateinit var statusCaption: TextView
    private lateinit var statusAddText: TextView
    private lateinit var btnReload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        statusLayout = findViewById<LinearLayout>(R.id.status)
        statusImage = findViewById<ImageView>(R.id.status_img)
        statusCaption = findViewById<TextView>(R.id.status_caption)
        statusAddText = findViewById<TextView>(R.id.status_add_text)
        btnReload = findViewById<Button>(R.id.reload_btn)

        btnReload.setOnClickListener {
            iTunesSearch()
        }

        inputEditText = findViewById<EditText>(R.id.search_edit_text)
        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(TEXT_SEARCH,""))
        }

        val btnBack = findViewById<View>(R.id.search_back_btn)

        btnBack.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.removeBtn)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter.trackList = trackList
        recyclerView.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesSearch()
                true
            }
            false
        }
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            viewSearchResult(TrackSearchStatus.Success)
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }

    private fun viewSearchResult(status : TrackSearchStatus) {
        when (status) {
            TrackSearchStatus.Success -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            TrackSearchStatus.NoDataFound -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.nodatafound)
                statusAddText.visibility = View.GONE
                btnReload.visibility = View.GONE
                statusCaption.setText(R.string.no_data_found)
            }
            TrackSearchStatus.ConnectionError -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.connect_error)
                statusAddText.visibility = View.VISIBLE
                btnReload.visibility = View.VISIBLE
                statusCaption.setText(R.string.connect_error)
            }
        }
    }

    private fun iTunesSearch(){
        if (inputEditText.text.isNotEmpty()){
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponce> {
                override fun onResponse(call: Call<TrackResponce>,
                                        response: Response<TrackResponce>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            viewSearchResult(TrackSearchStatus.NoDataFound)
                        } else {
                            viewSearchResult(TrackSearchStatus.Success)
                        }
                    } else {
                        viewSearchResult(TrackSearchStatus.ConnectionError)
                    }
                }

                override fun onFailure(call: Call<TrackResponce>, t: Throwable) {
                    viewSearchResult(TrackSearchStatus.ConnectionError)
                }

            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH, inputEditText.text.toString())
    }
}



