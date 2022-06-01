package ru.gb.veber.kotlinmvvm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessaging
import ru.gb.veber.kotlinmvvm.MyFirebaseMessagingService
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.ActivityMainBinding
import ru.gb.veber.kotlinmvvm.view.fragment.ContentProviderFragment

import ru.gb.veber.kotlinmvvm.view.fragment.FragmentMain

private const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("TAG", "onReceive() called with: context = $context, intent = $intent")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentMain()).commit()
        }
//        FirebaseMessaging.getInstance().token.addOnCompleteListener{
//            if(it.isSuccessful){
//                return@addOnCompleteListener
//            }
//            val token = it.result
//        }
        startService(Intent(this, MyFirebaseMessagingService::class.java))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                loadResultsReceiver,
                IntentFilter(TEST_BROADCAST_INTENT_FILTER)
            )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.menu_content_provider -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.fragment_container, ContentProviderFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadResultsReceiver)
    }
}
