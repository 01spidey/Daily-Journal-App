package com.example.journalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var custom_adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        recyclerView = findViewById(R.id.recycler_view_items)
        recyclerView.layoutManager = GridLayoutManager(this,3,RecyclerView.HORIZONTAL,false)
        custom_adapter = RecyclerAdapter(getList(), this)
        recyclerView.adapter = custom_adapter

    }

    private fun getList():ArrayList<String>{
        val lst = ArrayList<String>()
        for (i in 1..20) {
            lst.add("Item - $i");
        }
        return lst
    }
}
