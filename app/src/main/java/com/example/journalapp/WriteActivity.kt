package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.journalapp.databinding.ActivityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WriteActivity : AppCompatActivity() {

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()
    private lateinit var journal:HashMap<String,Any>;
    private lateinit var emotion_map:HashMap<String,String>;
    private lateinit var journalRef:DocumentReference;
    var saved = false;
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val binding = ActivityWriteBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val day: String = intent.getStringExtra("day").toString()
        val month: String = intent.getStringExtra("month").toString()
        val year: String = intent.getStringExtra("year").toString()
        binding.day.text = day
        binding.monthYear.text = String.format(getString(R.string.month_year), month, year)



        var count = 0

        val grateful = binding.grateful
        val content = binding.content
        val save = binding.save
        val title = binding.title

        val title_txt = intent.getStringExtra("title")
        val content_txt = intent.getStringExtra("content")
        val grateful_txt = intent.getStringExtra("grateful")
        val liked_txt = intent.getStringExtra("liked")

        if(content_txt?.isNotEmpty() == true){
            content.textSize = 17f
        }

        title.setText(title_txt)
        content.setText(content_txt)
        grateful.setText(grateful_txt)


        content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.isNotEmpty() == true) content.textSize = 17f
                    else content.textSize = 40f
            }
        })


        save.setOnClickListener {
            if (binding.grateful.text.toString().isEmpty()) Toast.makeText(this, "Complete the Gratefulness Section !!", Toast.LENGTH_SHORT).show()

            else {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                journalRef = FirebaseFirestore.getInstance().collection("Journals").document("$userId-$day-$month-$year")

                val entry = binding.content.text.toString()

                journal = HashMap();
                emotion_map = HashMap();

                journal["title"] = binding.title.text.toString()
                journal["day"] = day
                journal["month"] = month
                journal["year"] = year
                journal["entry"] = entry
                journal["grateful"] = binding.grateful.text.toString()
                journal["userID"] = userId
                journal["liked"] = (liked_txt=="true")
                journal["emotions"] = emotion_map

                callAPI(entry)
            }
        }

        binding.back.setOnClickListener {
            count++
            if (saved || count == 2) finish()
            else Toast.makeText(
                this,
                "Changes are not saved!! Press again to discard changes !!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun callAPI(ques:String){
//        okHTTP
        val jsonBody = JSONObject()
        val messagesArray = JSONArray()
        val messageObject = JSONObject()
        val prompt = "You should detect all the emotions present in the content in percentage. you should give the output as a long string in the form of 'sad-20%|happy-10%'etc., Just give the output string, not even 1 extra word. I need at least 5 emotions. Here's the entry,"

        try {
            messageObject.put("role", "user")
            messageObject.put("content", prompt+ques)
            messagesArray.put(messageObject)
            jsonBody.put("model", "gpt-3.5-turbo")
            jsonBody.put("messages", messagesArray)
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val body: RequestBody = RequestBody.create(JSON,jsonBody.toString())
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization","Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@WriteActivity,"Some Technical Error occured!!", Toast.LENGTH_SHORT).show()
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
                            val result = jsonMessage.getString("content").trim()
                            var arr:List<String>;
                            Log.d("Emotions",result);

                            for(emotion in result.split("|")){
                                Log.d("Emotion",emotion);
                                arr = emotion.split("-")
                                emotion_map.put(arr[0],arr[1]);
                                journal["emotions"] = emotion_map
                            }

                            journalRef.set(journal)
                                .addOnSuccessListener {
                                    Toast.makeText(this@WriteActivity, "Journal is Saving !!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@WriteActivity, MainActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { Toast.makeText(this@WriteActivity, "Error Saving the Journal !!", Toast.LENGTH_SHORT).show() }

                            saved = true
                        }

                        else {
                            Log.d("ERROR : ","Failed to parse response")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("ERROR : ","Failed to parse response")
                    }
                } else {
                    Log.d("ERROR", "Failed to request");
                }
            }

        })
    }

}