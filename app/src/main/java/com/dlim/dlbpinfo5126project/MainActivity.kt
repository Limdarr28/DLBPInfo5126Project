package com.dlim.dlbpinfo5126project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlim.dlbpinfo5126project.databinding.ActivityMainBinding
import com.dlim.dlbpinfo5126project.model.Article
import com.dlim.dlbpinfo5126project.viewmodel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var keyword: String = ""
    private var articles: MutableList<Article> = emptyList<Article>().toMutableList()
    lateinit var viewModel:MainViewModel
    private var index: Int = 0
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val articleObserver = Observer<Article> {
                newArticle ->
            binding.textViewHeadLine.text = ""
            binding.textViewAuthor.text = ""
            binding.textViewPubDate.text = ""
            binding.textViewAbstract.text = ""
            binding.textViewWebURL.text = ""
        }

        viewModel.mArticle.observe(this, articleObserver)
    }

    fun onSearchButtonClick(view: View) {
        keyword = binding.editTextKeyword.text.toString()
        if (!keyword.isNullOrBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                val request = getArticleSearchDataFromCoroutine(keyword)
                println(request)
                if (request != null) {
                    // update the ui
                    binding.textViewInfo.text = "${getString(R.string.keywordRe)} ${keyword}"
                    articles = emptyList<Article>().toMutableList()
                    index = 0
                    request.response.docs.forEach {
                        var dateStartString =
                            String.format("%19.19s", request.response.docs[index].pub_date)
                        val localDateTime = LocalDateTime.parse(dateStartString)
                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        val output = formatter.format(localDateTime)
                        articles.add(
                            Article(
                                request.response.docs[index].headline.main,
                                request.response.docs[index].byline.original, output,
                                request.response.docs[index].abstract,
                                request.response.docs[index].web_url, keyword
                            )
                        )

                        index++
                    }

                    index = 0

                    updateViewModel()
                } else {
                    binding.textViewInfo.text = getString(R.string.noResults)
                }
            }
        }
        else {
            binding.textViewInfo.text = getString(R.string.noKeyword)
        }

    }

    fun onNextButtonClick(view: View) {
        if (articles.isNotEmpty()) {
            if (index >= articles.size - 1) {
                index = 0
            }

            updateViewModel()
            index++
        }
    }

    fun onBackButtonClick(view: View) {
        if (articles.isNotEmpty()) {
            if (index <= 0) {
                index = articles.size - 1
            }

            updateViewModel()
            index--
        }
    }
    fun onCreditsButtonClick(view: View) {
        val intent = Intent(this,CreditsActivity::class.java)
        startActivity(intent)
    }

    fun onAddButtonClick(view: View) {
        if (articles.isEmpty()) {
            binding.textViewInfo.text = getString(R.string.emptyList)
        }
        else {
            db.collection("articles")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        db.collection("articles").document(document.id)
                            .delete()
                    }
                    for (article in articles) {

                        if (article.headline.isNullOrBlank()) {
                            article.headline = "N/A"
                        }
                        if (article.byline.isNullOrBlank()) {
                            article.byline = "N/A"
                        }
                        if (article.pub_date.isNullOrBlank()) {
                            article.pub_date = "N/A"
                        }
                        if (article.abstract.isNullOrBlank()) {
                            article.abstract = "N/A"
                        }
                        if (article.web_url.isNullOrBlank()) {
                            article.web_url = "N/A"
                        }

                        // Version 1 create or add to users collection
                        db.collection("articles")
                            .add(article)
                            .addOnSuccessListener {
                                binding.textViewInfo.text = getString(R.string.addDataInfo)
                            }
                            .addOnFailureListener {
                                binding.textViewInfo.text = getString(R.string.addDataInfoFail)
                            }
                    }
                }
        }
    }

    fun onReadButtonClick(view: View) {
        db.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                db.collection("articles")
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.size() > 0) {
                            articles = emptyList<Article>().toMutableList()

                            for (document in result) {
                                var headline: String = "headline"
                                var byline: String = "byline"
                                var date: String = "pub_date"
                                var abstract: String = "abstract"
                                var weburl: String = "web_url"
                                var keywordK: String = "keyword"

                                articles.add(
                                    Article(
                                        document.data.get(headline).toString(),
                                        document.data.get(byline).toString(),
                                        document.data.get(date).toString(),
                                        document.data.get(abstract).toString(),
                                        document.data.get(weburl).toString(),
                                        document.data.get(keywordK).toString()
                                    )
                                )
                                keyword = document.data.get(keywordK).toString()
                            }

                            index = 0

                            binding.editTextKeyword.setText(keyword, TextView.BufferType.EDITABLE);
                            binding.textViewInfo.text =
                                getString(R.string.readDataInfo1) + keyword +
                                        getString(R.string.readDataInfo2)
                            updateViewModel()
                        }
                        else {
                            binding.textViewInfo.text = getString(R.string.emptyDatabase)
                        }
                    }
                            .addOnFailureListener { exception ->
                                binding.textViewInfo.text = getString(R.string.readDataInfoFail)
                            }

                    }



    }

    fun onLogoutButtonClick(view: View) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this,R.string.logged_out, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
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

    private fun updateViewModel() {
        viewModel.updateArticle(articles[index].headline,articles[index].byline,
            articles[index].pub_date, articles[index].abstract, articles[index].web_url, keyword)

        val articleObserver = Observer<Article> {
                article ->
            binding.textViewHeadLine.text = article.headline
            binding.textViewAuthor.text = article.byline
            binding.textViewPubDate.text = article.pub_date
            binding.textViewAbstract.text = article.abstract
            binding.textViewWebURL.text = article.web_url
        }

        viewModel.mArticle.observe(this, articleObserver)
    }
}