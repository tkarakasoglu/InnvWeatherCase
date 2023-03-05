package com.tk.innovaweathercase.ui.home

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.ui.home.detail.WeatherInformationDetailFragment

class WeatherInformationViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var cityMutableList = mutableListOf<City>()

    override fun createFragment(position: Int): Fragment = WeatherInformationDetailFragment(cityMutableList[position])

    override fun getItemCount(): Int {
        return cityMutableList.size
    }

    override fun getItemId(position: Int): Long {
        return cityMutableList[position].geonameId.toLong()
    }
    override fun containsItem(itemId: Long): Boolean = cityMutableList.any { it.geonameId.toLong() == itemId }

    @SuppressLint("NotifyDataSetChanged")
    fun setCityMutableList(cityList: List<City>) {

        cityMutableList = cityList.toMutableList()
        notifyDataSetChanged()
    }
}