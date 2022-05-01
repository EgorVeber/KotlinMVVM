package ru.gb.veber.kotlinmvvm.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.ActivityMainBinding
import ru.gb.veber.kotlinmvvm.view.fragment.CitysFragment
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private val receiverConnectivity = LoadResultsReceiver(null)
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CitysFragment()).commit()
        }
        registerReceiver(
            receiverConnectivity,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.let {
            it.subtitle = resources.getString(R.string.list_citys)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiverConnectivity)
    }
}