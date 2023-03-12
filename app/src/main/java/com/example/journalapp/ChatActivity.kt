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
        val jsonBody:JSONObject = JSONObject()

        try {
            jsonBody.put("model","text-davinci-003")
            jsonBody.put("prompt",ques)
            jsonBody.put("max_tokens",4000)
            jsonBody.put("temperature",0)
        }catch (e:JSONException){
            e.printStackTrace()
        }

        val body:RequestBody = RequestBody.create(JSON,jsonBody.toString())
        val request:Request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
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
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.body!!.string())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getString("text")
                        addResponse(result.trim())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else addResponse("Failed to load response due to " + response.body!!.toString())

            }
        })
    }
}