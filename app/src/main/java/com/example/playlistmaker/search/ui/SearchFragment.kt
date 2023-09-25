package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.root.listeners.BottomNavigationListener
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchingViewModel
import com.example.playlistmaker.search.ui.models.TracksState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null

    private lateinit var binding: FragmentSearchBinding

    var textFromSearchWidget = ""

    private val viewModel: SearchingViewModel by viewModel()

    private var isClickAllowed = true

    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            clickToTrackList(track = it)
        }
    }

    private val historyAdapter = TrackAdapter {
        if (clickDebounce()) {
            clickToHistoryTrackList(it)
        }
    }

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var notFoundWidget: LinearLayout
    private lateinit var badConnectionWidget: LinearLayout
    private lateinit var badConnectionTextView: TextView
    private lateinit var historyWidget: LinearLayout
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw IllegalArgumentException("Activity must implement BottomNavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEditText = binding.inputEditText
        clearButton = binding.clearIcon
        notFoundWidget = binding.notFoundWidget
        badConnectionWidget = binding.badConnectionWidget
        badConnectionTextView = binding.badConnection
        historyWidget = binding.historyWidget
        clearHistoryButton = binding.clearHistoryButton
        progressBar = binding.progressBar

        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(EDIT_TEXT_VALUE, ""))
        }

        viewModel.tracksState.observe(viewLifecycleOwner) { tracksState ->
            render(tracksState)
        }

        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        historyRecyclerView = binding.historyRecycleView
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = historyAdapter

        viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            historyAdapter.tracks = historyList
            historyAdapter.notifyDataSetChanged()
        }


        clearHistoryButton.setOnClickListener {
            viewModel.clearHistoryList()
            historyAdapter.notifyDataSetChanged()
            historyWidget.visibility = View.GONE
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyWidget.visibility = if (hasFocus && inputEditText.text.isEmpty() && viewModel.getHistoryList().isNotEmpty()) View.VISIBLE else View.GONE
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // some code
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                textFromSearchWidget = inputEditText.text.toString()

                // On Focus Actions
                historyWidget.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true && viewModel.getHistoryList().isNotEmpty()) View.VISIBLE else View.GONE

                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(p0: Editable?) {
                // some code
            }
        }

        inputEditText.addTextChangedListener(textWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRequest(inputEditText.text.toString())
                true
            }
            false
        }

        KeyboardVisibilityEvent.setEventListener(
            activity = requireActivity(),
            lifecycleOwner = viewLifecycleOwner,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (isOpen) {
                        onKeyboardVisibilityChanged(true)
                    } else {
                        onKeyboardVisibilityChanged(false)
                    }
                }
            }
        )

    }

    override fun onStop() {
        super.onStop()
        viewModel.saveHistoryList()
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    override fun onDestroyView() {
        viewModel.onDestroy()
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        if (inputEditText.text.toString().isEmpty()) {
            viewModel.refreshTrackState()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE, textFromSearchWidget)
    }

    private fun clearButtonVisibility(s:CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickToTrackList(track: Track) {
        viewModel.addTrackToHistoryList(track)

        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track))
    }

    private fun clickToHistoryTrackList(track: Track) {
        viewModel.transferTrackToTop(track)

        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track))
    }

    private fun showPlaceholder(flag: Boolean?, message: String = "") {
        if (flag != null) {
            if (flag == true) {
                badConnectionWidget.visibility = View.GONE
                notFoundWidget.visibility = View.VISIBLE
            } else {
                notFoundWidget.visibility = View.GONE
                badConnectionWidget.visibility = View.VISIBLE
                badConnectionTextView.text = message
            }
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
        } else {
            notFoundWidget.visibility = View.GONE
            badConnectionWidget.visibility = View.GONE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }


    private fun render(tracksState: TracksState) {
        when {

            tracksState.isLoading -> showLoading(true)

            else -> {

                showLoading(false)

                if (tracksState.isFailed != null) {

                    when {
                        tracksState.isFailed -> showPlaceholder(false, getString(R.string.server_error))
                        else -> showPlaceholder(false, getString(R.string.bad_connection))
                    }

                } else {

                    when {
                        tracksState.tracks.isEmpty() && inputEditText.text.toString().isNotEmpty() -> showPlaceholder(true)
                        else -> {
                            adapter.tracks.clear()
                            adapter.tracks.addAll(tracksState.tracks)
                            adapter.notifyDataSetChanged()
                            showPlaceholder(null)
                        }
                    }

                }
            }
        }
    }

    private fun showLoading(isLoaded: Boolean) {
        progressBar.visibility = if (isLoaded) View.VISIBLE else View.GONE
    }

    private fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        if (isVisible) {
            bottomNavigationListener?.toggleBottomNavigationViewVisibility(false)
        } else {
            bottomNavigationListener?.toggleBottomNavigationViewVisibility(true)
        }
    }

    companion object {
        const val EDIT_TEXT_VALUE = "EDIT_TEXT_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}