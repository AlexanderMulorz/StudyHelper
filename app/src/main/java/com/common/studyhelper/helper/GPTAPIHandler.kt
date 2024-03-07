package com.common.studyhelper.helper

import android.util.Log
import com.common.studyhelper.BuildConfig

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class GPTAPIHandler {

    private val client = OkHttpClient()

    private fun getResponse(question: String, callback: (String) -> Unit){
        val apiKey = BuildConfig.GPT_API_KEY
        val url = "https://api.openai.com/v1/chat/completions"

        val requestBody="""
            {
            "model": "gpt-3.5-turbo",
            "messages": [
                 {
                   "role": "user",
                   "content": "summarize this: $question" 
                 }],
            "temperature": 0.8
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed",e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body=response.body?.string()
                if (body != null) {
                    Log.v("data",body)
                }
                else{
                    Log.v("data","empty")
                }


                callback(body.toString())
            }
        })
    }
    fun summarizeText(transcript: String, callback: (String) -> Unit){
        val tokenlenght = countToken(transcript)
        val request = "Tell me a joke"//
        val apiKey = "BuildConfig.GPT_API_KEY"


        getResponse(transcript){ response ->
            val jsonObject= JSONObject(response)
            val jsonArray: JSONArray =jsonObject.getJSONArray("choices")
            val textMessage=jsonArray.getJSONObject(0).getJSONObject("message")
            val text_Result=textMessage.getString("content")
            val summary = text_Result.toString()
            callback(summary)
        }
    }

    fun countToken(text: String): Int {
        val regex = """\S+""".toRegex()
        val token = regex.findAll(text).map{it.value}
        return token.count()
    }
}