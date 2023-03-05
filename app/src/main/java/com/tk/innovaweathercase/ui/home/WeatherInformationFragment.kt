package com.tk.innovaweathercase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tk.innovaweathercase.R
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.databinding.FragmentHomeBinding
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel

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
        binding.weatherViewPager.adapter = weatherInformationViewPagerAdapter

        binding.addCityFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_citylist)
        }
    }

    private fun initViewModel() {
        weatherViewModel = (activity as WeatherActivity).weatherViewModel
        weatherViewModel.cityFavoritesMutableLiveData.observe(viewLifecycleOwner) { response ->
            setViewPagerContent(response)
        }
    }

    private fun setViewPagerContent(cityList: List<City>) {
        weatherInformationViewPagerAdapter.setCityMutableList(cityList)
    }
}