package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedFragment : Fragment(),SavedAdapter.OnItemListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var savedAdapter: SavedAdapter
    private lateinit var likedJournals:ArrayList<Journal>
    private lateinit var db:FirebaseFirestore
    private lateinit var dialog_layout:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.savedRecyclerView)
        likedJournals = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = FirebaseFirestore.getInstance()
        eventChangeListener()
    }

    private fun eventChangeListener(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("Journals")
            .whereEqualTo("liked", true)
            .whereEqualTo("userID", uid)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    for(document in it.result){
                        likedJournals.add(document.toObject(Journal::class.java))
                    }
                    savedAdapter = SavedAdapter(this, likedJournals,requireContext())
                    recyclerView.adapter = savedAdapter

                }else Log.d("ERROR : ", "Error fetching the documents!!")
            }
    }


    override fun onItemClick(position: Int, layout: View) {

            val journalEntry:Journal  = likedJournals[position]
            val intent: Intent = Intent(context, ViewJournalActivity::class.java)
            intent.putExtra("month", journalEntry.month)
            intent.putExtra("year", journalEntry.year)
            intent.putExtra("day", journalEntry.day)
            intent.putExtra("title", journalEntry.title)
            intent.putExtra("content", journalEntry.entry)
            intent.putExtra("grateful", journalEntry.grateful)
            intent.putExtra("uid",journalEntry.userID)
            intent.putExtra("liked",journalEntry.liked.toString())

            startActivity(intent)
    }
}
