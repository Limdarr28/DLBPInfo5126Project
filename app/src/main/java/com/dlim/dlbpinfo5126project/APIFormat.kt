package com.dlim.dlbpinfo5126project

data class APIFormat (
    var copyright:String,
    var response: Response
)

data class Response (
    var docs:List<Docs>
)

data class Docs(
    var headline:Headline,
    var byline:Byline,
    var pub_date:String,
    var abstract:String,
    var web_url:String
)

data class Headline(
    var main:String
)

data class Byline(
    var original:String
)
