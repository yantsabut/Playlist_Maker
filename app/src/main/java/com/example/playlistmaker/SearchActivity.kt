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

    private val iTunesBaseUrl = "http://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private var searchHistoryList = ArrayList<Track>()
    private lateinit var clearButton: ImageView
    private lateinit var backArrowImageView: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusLayout: LinearLayout
    private lateinit var statusImage: ImageView
    private lateinit var statusCaption: TextView
    private lateinit var statusAddText: TextView
    private lateinit var btnReload: Button
    private lateinit var historySearchText: TextView
    private lateinit var btnClearHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        statusLayout = findViewById(R.id.status)
        statusImage = findViewById(R.id.status_img)
        statusCaption = findViewById(R.id.status_caption)
        statusAddText = findViewById(R.id.status_add_text)
        backArrowImageView = findViewById(R.id.backArrowImageView)
        clearButton = findViewById(R.id.clearIcon)
        historySearchText = findViewById(R.id.history_search_text)
        btnClearHistory = findViewById(R.id.clear_history_btn)
        recyclerView = findViewById(R.id.recyclerView)
        btnReload = findViewById(R.id.reload_btn)
        btnReload.setOnClickListener {
            iTunesSearch()
        }

        inputEditText = findViewById(R.id.inputEditText)
        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(TEXT_SEARCH, ""))
        }


        backArrowImageView.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            searchHistoryList = SearchHistory.fill()
            viewResult(
                if (searchHistoryList.size > 0)
                    TrackSearchStatus.ShowHistory
                else TrackSearchStatus.Empty
            )
        }

        btnClearHistory.setOnClickListener {
            searchHistoryList.clear()
            SearchHistory.clear()
            viewResult(TrackSearchStatus.Empty)
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

        searchHistoryList = SearchHistory.fill()
        recyclerView.adapter = adapter
        viewResult(
            if (searchHistoryList.size > 0)
                TrackSearchStatus.ShowHistory
            else TrackSearchStatus.Empty
        )

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesSearch()
                true
            }
            false
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            trackList.clear()
            viewResult(TrackSearchStatus.Success)
            View.GONE
        } else {
            View.VISIBLE
        }
    }

        private fun viewResult(status: TrackSearchStatus) {
        when (status) {
            TrackSearchStatus.Empty -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.GONE
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
            }
            TrackSearchStatus.Success -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                adapter.trackList = trackList
                adapter.notifyDataSetChanged()
            }
            TrackSearchStatus.NoDataFound -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.nodatafound)
                statusAddText.visibility = View.GONE
                btnReload.visibility = View.GONE
                statusCaption.setText(R.string.no_data_found)
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
            }
            TrackSearchStatus.ConnectionError -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.connect_error)
                statusAddText.visibility = View.VISIBLE
                btnReload.visibility = View.VISIBLE
                statusCaption.setText(R.string.connect_error)
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
            }
            TrackSearchStatus.ShowHistory -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                historySearchText.visibility = View.VISIBLE
                btnClearHistory.visibility = View.VISIBLE
                adapter.trackList = searchHistoryList
                adapter.notifyDataSetChanged()
            }
        }
    }


    private fun iTunesSearch() {
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponce> {
                override fun onResponse(
                    call: Call<TrackResponce>,
                    response: Response<TrackResponce>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            viewResult(TrackSearchStatus.NoDataFound)
                        } else {
                            viewResult(TrackSearchStatus.Success)
                        }
                    } else {
                        viewResult(TrackSearchStatus.ConnectionError)
                    }
                }


                override fun onFailure(call: Call<TrackResponce>, t: Throwable) {
                    viewResult(TrackSearchStatus.ConnectionError)
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH, inputEditText.text.toString())
    }
}
