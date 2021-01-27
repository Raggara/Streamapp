package com.esgi.streamapp.Activities.PersonnalLists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.esgi.streamapp.Activities.MainList.MainListActivity
import com.esgi.streamapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout


class PersonnalListsActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personnal_lists_activity)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        viewPager = this.findViewById(R.id.viewPager)
        tabLayout = this.findViewById(R.id.tab_layout)
        bottomNav = this.findViewById(R.id.navBar)
        bottomNav?.selectedItemId = R.id.lists
        bottomNav?.setOnNavigationItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@PersonnalListsActivity, MainListActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navigate -> {
                    Toast.makeText(this, "navigate", Toast.LENGTH_SHORT)
                }
                R.id.preferences -> {
                    Toast.makeText(this, "pref", Toast.LENGTH_SHORT)
                }
            }
            true
        }

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