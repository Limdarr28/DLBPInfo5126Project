package com.dlim.dlbpinfo5126project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dlim.dlbpinfo5126project.databinding.ActivityCreditsBinding
class CreditsActivity  : AppCompatActivity() {
    private lateinit var binding:ActivityCreditsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



    fun onHomeButtonClick(view: View) {
        finish()
    }
}