package ru.gb.veber.kotlinmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.container,MainFragment.newInstance()).commit()
    }
}