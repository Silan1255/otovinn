package com.example.otovinncase.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otovinncase.R
import com.example.otovinncase.data.model.BaseResponse
import com.example.otovinncase.data.model.Discover
import com.example.otovinncase.data.model.Menu
import com.example.otovinncase.data.model.Slider
import com.example.otovinncase.data.model.Story
import com.example.otovinncase.databinding.FragmentMainBinding
import com.example.otovinncase.ui.main.adapter.MenuAdapter
import com.example.otovinncase.ui.main.adapter.SliderAdapter
import com.example.otovinncase.ui.main.adapter.StationsAdapter
import com.example.otovinncase.ui.main.adapter.StoriesAdapter
import com.example.otovinncase.ui.main.viewModel.MainVM
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    //region variables
    private lateinit var binding: FragmentMainBinding
    private lateinit var mainVM: MainVM
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var storyList: ArrayList<Story>
    private lateinit var sliderList: ArrayList<Slider>
    private lateinit var menuList: ArrayList<Menu>
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable
    //endregion

    //region lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        addListeners()
        getDiscover()
        setupSpeechRecognizer()
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    override fun onStop() {
        super.onStop()
        removeObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
    //endregion

    //region init
    private fun initialize() {
        storyList = arrayListOf()
        menuList = arrayListOf()
        sliderList = arrayListOf()
        mainVM = ViewModelProvider(this)[MainVM::class.java]
    }

    private fun setupStories() {
        binding.rvStories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storiesAdapter = StoriesAdapter(storyList, requireContext())
        binding.rvStories.adapter = storiesAdapter
    }

    private fun setupMenu() {
        binding.rvMenu.layoutManager = GridLayoutManager(context, 4)
        menuAdapter = MenuAdapter(menuList, requireContext())
        binding.rvMenu.adapter = menuAdapter
    }

    private fun setupViewPager() {
        binding.pager.adapter = SliderAdapter(sliderList, requireContext())
        TabLayoutMediator(binding.tabs, binding.pager) { _, _ -> }.attach()
        setupAutoScroll()
    }

    private fun setupAutoScroll() {
        autoScrollHandler = Handler(Looper.getMainLooper())
        autoScrollRunnable = object : Runnable {
            override fun run() {
                val nextItem = (binding.pager.currentItem + 1) % binding.pager.adapter?.itemCount!!
                binding.pager.setCurrentItem(nextItem, true)
                autoScrollHandler.postDelayed(this, 5000)
            }
        }
        autoScrollHandler.postDelayed(autoScrollRunnable, 5000)
    }

    private fun setupStations(discover: Discover) {
        binding.rvNearMe.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLastServicesReceived.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestWeek.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        discover.stations.forEach { stations ->
            when (stations.order) {
                1 -> {
                    binding.rvNearMe.adapter = stations.station?.let { it1 ->
                        StationsAdapter(it1, requireContext())
                    }
                }

                2 -> {
                    binding.rvLastServicesReceived.adapter = stations.station?.let { it1 ->
                        StationsAdapter(it1, requireContext())
                    }
                }

                3 -> {
                    binding.rvBestWeek.adapter = stations.station?.let { it1 ->
                        StationsAdapter(it1, requireContext())
                    }
                }
            }
        }
    }
    //endregion

    //region tool
    private fun getDiscover() {
        mainVM.getDiscover()
    }
    //endregion

    //region listeners
    private fun addListeners() {
        binding.btnMicrophone.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                startListening()
            }
        }
    }

    private fun startListening() {
        speechRecognizer.startListening(recognizerIntent)
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.wrong_voice),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    val text = it[0]
                    binding.edtSearch.setText(text)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startListening()
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.wrong_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setObservers() {
        mainVM.discoverData.observe(viewLifecycleOwner, discoverDataObserver)
    }

    private fun removeObservers() {
        mainVM.discoverData.removeObserver(discoverDataObserver)
    }
    //endregion

    //region data
    private val discoverDataObserver = Observer<BaseResponse<Discover>> { response ->
        response.data.let {
            storyList.addAll(it.stories)
            menuList.addAll(it.menu)
            sliderList.addAll(it.slider)
            setupStories()
            setupMenu()
            setupViewPager()
            setupStations(it)
        }
    }
    //endregion
}
