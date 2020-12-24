package com.esgi.streamapp.Activities.PersonnalLists

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.esgi.streamapp.R
import com.google.android.material.tabs.TabLayout

class PersonnalListsActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personnal_lists_activity)
        viewPager = this.findViewById(R.id.viewPager)
        tabLayout = this.findViewById(R.id.tab_layout)
        initTabs()
    }

    private fun initTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(FavouritesFragment(), "Favoris")
        adapter.addFragment(HistoryFragment(), "Historique")

        viewPager?.adapter = adapter

        tabLayout?.setupWithViewPager(viewPager)
    }

    fun getContext(): Context{
        return this@PersonnalListsActivity
    }
}