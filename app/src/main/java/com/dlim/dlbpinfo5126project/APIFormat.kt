package com.dlim.dlbpinfo5126project

data class APIFormat (
    var copyright:String,
    var results: Results
)

data class Results (
    var s:List<String>
)
