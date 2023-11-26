package com.dlim.dlbpinfo5126project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dlim.dlbpinfo5126project.model.Article

class MainViewModel : ViewModel() {
    var mArticle = MutableLiveData<Article>()
    private var mHeadline: String = ""
    private var mByline: String = ""
    private var mPubDate: String = ""
    private var mAbstract: String = ""
    private var mWebUrl: String = ""
    init {
        println("viewModel init")
    }

    fun updateArticle(headlineA: String?, bylineA:String?, pub_dateA:String?, abstractA:String?, web_urlA:String?, keywordA:String){
        if (headlineA.isNullOrBlank()) {
            mHeadline = "N/A"
        }
        else {
            mHeadline = headlineA
        }
        if (bylineA.isNullOrBlank()) {
            mByline = "N/A"
        }
        else {
            mByline = bylineA
        }
        if (pub_dateA.isNullOrBlank()) {
            mPubDate = "N/A"
        }
        else {
            mPubDate = pub_dateA
        }
        if (abstractA.isNullOrBlank()) {
            mAbstract = "N/A"
        }
        else {
            mAbstract = abstractA
        }
        if (web_urlA.isNullOrBlank()) {
            mWebUrl = "N/A"
        }
        else {
            mWebUrl = web_urlA
        }


        mArticle.value = Article(mHeadline, mByline, mPubDate, mAbstract, mWebUrl, keywordA)
    }
}