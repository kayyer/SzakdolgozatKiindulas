package hu.bme.aut.eventes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.eventes.fragments.*

class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = when(position){
            0 -> AllEventFragment()
            1 -> MyEventFragment()
            2 -> CreateEventFragment()
            else -> AllEventFragment()
        }

        override fun getCount() : Int = NUM_PAGES

        companion object{
            const val NUM_PAGES = 3
        }
    }
