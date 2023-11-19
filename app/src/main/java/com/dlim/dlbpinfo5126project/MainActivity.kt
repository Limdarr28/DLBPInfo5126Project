package com.dlim.dlbpinfo5126project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dlim.dlbpinfo5126project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onSearchButtonClick(view: View) {
        binding.textViewInfo.text = "Hello There"

    }

    fun onCreditsButtonClick(view: View) {
        val intent = Intent(this,CreditsActivity::class.java)
        startActivity(intent)
    }

}