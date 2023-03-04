package com.tk.innovaweathercase.ui.citylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.databinding.FragmentCitylistBinding
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel

class CityListFragment : Fragment(), OnCityFavoritesChangeListener {

    private lateinit var weatherViewModel: WeatherViewModel
    lateinit var cityListRecyclerViewAdapter: CityListRecyclerViewAdapter

    private var _binding: FragmentCitylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()
        initViewModel()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCityFavoritesChange(city: City, isFav: Boolean) {
        weatherViewModel.setCityFavorites(city, isFav)
    }

    private fun initUI() {
        cityListRecyclerViewAdapter = CityListRecyclerViewAdapter(this)
        binding.citiesRecyclerView.apply {
            adapter = cityListRecyclerViewAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initViewModel() {
        weatherViewModel = (activity as WeatherActivity).weatherViewModel
        weatherViewModel.cityListMutableLiveData.observe(viewLifecycleOwner, Observer { response ->
            setRecyclerViewContent(response)
        })
    }

    private fun setRecyclerViewContent(cityList: List<City>) {
        cityListRecyclerViewAdapter.setCityMutableList(cityList)
    }
}