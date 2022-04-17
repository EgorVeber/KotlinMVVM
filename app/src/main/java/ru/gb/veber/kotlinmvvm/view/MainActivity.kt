package ru.gb.veber.kotlinmvvm.view

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.ActivityMainBinding
import ru.gb.veber.kotlinmvvm.view.fragment.CitysFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().
            replace(R.id.fragment_container, CitysFragment()).commit()
        }
    }
    fun initToolbar()
    {
        setSupportActionBar(binding.toolbarMain)
        val toggle = ActionBarDrawerToggle(this,
            binding.drawerLayout, binding.toolbarMain,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar!!.subtitle = resources.getString(R.string.list_citys)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}