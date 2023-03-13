package com.example.journalapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.ActivityChatBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var welcomeImageView: ImageView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton:ImageButton
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter:MessageAdapter

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()
    var ready = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater);
        setContentView(binding.root)

        messageList = ArrayList();


        welcomeImageView = binding.welcomeImage
        messageEditText = binding.messageEditText
        sendButton = binding.sendBtn


//        setup recycler View
        recyclerView = binding.chatRecyclerView
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter
        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linearLayoutManager

        var question:String;

        sendButton.setOnClickListener{
            question = messageEditText.text.toString().trim()
            addToChat(question,"me")
            callAPI(question)
            messageEditText.setText("")
            welcomeImageView.visibility = View.GONE
        }
    }

    fun addToChat(message_txt: String, sentBy:String){
        runOnUiThread{
                val message = Message(message_txt,sentBy)
                messageList.add(message)

                messageAdapter.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
        }
    }

    fun addResponse(response:String){
        addToChat(response,"bot")
    }

    fun callAPI(ques:String){
//        okHTTP
        val jsonBody = JSONObject()
        val messagesArray = JSONArray()
        val messageObject = JSONObject()
        val prompt = "You are professional psychologist and a good friend of mine and whenever I talk,you need to talk to me like that and " +
                "always say things to make me feel better and you should not disclose that you are an AI language model and instead you should say you are my best friend. At the same time, talk short and precise.Here is my conversation,"

        try {
            messageObject.put("role", "user")
            messageObject.put("content", prompt+ques)
            messagesArray.put(messageObject)
            jsonBody.put("model", "gpt-3.5-turbo")
            jsonBody.put("messages", messagesArray)
        }catch (e:JSONException){
            e.printStackTrace()
        }

        val body:RequestBody = RequestBody.create(JSON,jsonBody.toString())
        val request:Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization","Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var responseJson: JSONObject? = null
                    try {
                        val responseBody = response.body?.string()
                        if (!responseBody.isNullOrEmpty()) {
                            responseJson = JSONObject(responseBody)
                            val choicesArray = responseJson.getJSONArray("choices")
                            val jsonMessage = choicesArray.getJSONObject(0).getJSONObject("message")
                            val result = jsonMessage.getString("content")
                            addResponse(result.trim())
                        } else {
                            addResponse("Empty response body")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        addResponse("Failed to parse response")
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body?.string())
                }
            }

        })
    }
}