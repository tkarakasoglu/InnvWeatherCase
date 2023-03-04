package com.tk.innovaweathercase.ui.home

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.tk.innovaweathercase.R
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.databinding.FragmentHomeBinding
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel
import com.tk.innovaweathercase.util.Utilities
import java.util.*

class WeatherInformationFragment : Fragment() {

    lateinit var weatherViewModel: WeatherViewModel
    lateinit var weatherInformationViewPagerAdapter: WeatherInformationViewPagerAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()
        initViewModel()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        weatherInformationViewPagerAdapter = WeatherInformationViewPagerAdapter(this)
        binding.weatherViewPager.adapter = null

        binding.addCityFloatingActionButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_citylist)
        })
    }

    private fun initViewModel() {
        weatherViewModel = (activity as WeatherActivity).weatherViewModel
        weatherViewModel.cityFavoritesMutableLiveData.observe(viewLifecycleOwner, Observer { response ->
            binding.weatherViewPager.adapter = weatherInformationViewPagerAdapter
            setViewPagerContent(response)
        })
    }

    private fun setViewPagerContent(cityList: List<City>) {
        weatherInformationViewPagerAdapter.setCityMutableList(cityList)
    }
}