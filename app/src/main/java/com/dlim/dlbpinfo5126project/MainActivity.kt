package com.dlim.dlbpinfo5126project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dlim.dlbpinfo5126project.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityMainBinding
    private var keyword: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = recyclerViewManager
        binding.recyclerView.setHasFixedSize(true)
    }

    fun onSearchButtonClick(view: View) {
        keyword = binding.editTextKeyword.text.toString()
        CoroutineScope(Dispatchers.Main).launch {
            val request = getArticleSearchDataFromCoroutine(keyword)
            println(request)
            if(request != null)
            {
                // update the ui
                binding.textViewInfo.text = "${getString(R.string.keywordRe)} ${keyword}"
                binding.recyclerView.adapter = RecyclerAdaptor(request.response.docs)
            }
            else
            {
                binding.textViewInfo.text = getString(R.string.noResults)
            }
        }

    }

    fun onCreditsButtonClick(view: View) {
        val intent = Intent(this,CreditsActivity::class.java)
        startActivity(intent)
    }

    fun onAddButtonClick(view: View) {
        val intent = Intent(this,CreditsActivity::class.java)
        startActivity(intent)
    }

    fun onReadButtonClick(view: View) {
        val intent = Intent(this,CreditsActivity::class.java)
        startActivity(intent)
    }

    private suspend fun getArticleSearchDataFromCoroutine(keywordSel:String):APIFormat? {
        val defer = CoroutineScope(Dispatchers.IO).async {
            val url =
                URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=$keywordSel&api-key=XzLQ6bzoAMZmb06CtY1bBoaYi02VHVgD")
            val connection = url.openConnection() as HttpsURLConnection
            if(connection.responseCode == 200)
            {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, APIFormat::class.java)
                inputStreamReader.close()
                inputSystem.close()
                return@async request
            }
            else {
                return@async null
            }
        }
        return defer.await()
    }
}