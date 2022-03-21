package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet : Button
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweet)
        btnTweet = findViewById(R.id.btntweet)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener {

            //grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            //make sure tweet isnt empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty Tweets not allowed", Toast.LENGTH_SHORT).show()
            } else {

            //make sure tweet is under chracter count
            if(tweetContent.length > 140) {
                Toast.makeText(this, "Tweet is too long! Limit is 140 characters", Toast.LENGTH_SHORT).show()
            } else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "successfully published tweet")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()


                    }

                })            }
            }


        }
    }
    companion object {
        val TAG = "Compose Activity"
    }
}