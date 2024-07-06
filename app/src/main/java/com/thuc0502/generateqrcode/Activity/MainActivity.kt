package com.thuc0502.generateqrcode.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thuc0502.generateqrcode.Fragment.CreateFragment
import com.thuc0502.generateqrcode.Fragment.HistoryFragment
import com.thuc0502.generateqrcode.Fragment.MyQrFragment
import com.thuc0502.generateqrcode.Fragment.ScanFragment
import com.thuc0502.generateqrcode.R
import com.thuc0502.generateqrcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v ,insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left ,systemBars.top ,systemBars.right ,systemBars.bottom)
            insets
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, CreateFragment())
            .commit()
        binding.bottomNavigationView.setOnItemSelectedListener {
            val fragment = when(it.itemId){
                R.id.menu_create -> CreateFragment()
                R.id.menu_history -> HistoryFragment()
                R.id.menu_scan -> ScanFragment()
                R.id.menu_myqr -> MyQrFragment()
                else -> throw IllegalArgumentException("Unknown menu item")
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout , fragment)
                .commit()
            true
        }
    }
}
