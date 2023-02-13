package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class HomeFragment : Fragment() {

    private lateinit var year:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViews()
    }


    private fun setupTextViews() {

        val jan = view?.findViewById<TextView>(R.id.jan)
        val feb = view?.findViewById<TextView>(R.id.feb)
        val march = view?.findViewById<TextView>(R.id.march)
        val apr = view?.findViewById<TextView>(R.id.apr)
        val may = view?.findViewById<TextView>(R.id.may)
        val june = view?.findViewById<TextView>(R.id.june)
        val july = view?.findViewById<TextView>(R.id.july)
        val aug = view?.findViewById<TextView>(R.id.aug)
        val sep = view?.findViewById<TextView>(R.id.sep)
        val oct = view?.findViewById<TextView>(R.id.oct)
        val nov = view?.findViewById<TextView>(R.id.nov)
        val dec = view?.findViewById<TextView>(R.id.dec)

        kotlin.run{
            jan?.setOnClickListener {
                startDaysFragment(jan.text.toString())
            }

            feb?.setOnClickListener { startDaysFragment(feb.text.toString()) }
            march?.setOnClickListener { startDaysFragment(march.text.toString()) }
            apr?.setOnClickListener { startDaysFragment(apr.text.toString()) }
            may?.setOnClickListener { startDaysFragment(may.text.toString()) }
            june?.setOnClickListener { startDaysFragment(june.text.toString()) }
            july?.setOnClickListener { startDaysFragment(july.text.toString()) }
            aug?.setOnClickListener { startDaysFragment(aug.text.toString()) }
            sep?.setOnClickListener { startDaysFragment(sep.text.toString()) }
            oct?.setOnClickListener { startDaysFragment(oct.text.toString()) }
            nov?.setOnClickListener { startDaysFragment(nov.text.toString()) }
            dec?.setOnClickListener { startDaysFragment(dec.text.toString()) }
        }

    }

    private fun startDaysFragment(month: String) {
        val daysFragment = DaysFragment()
        val bundle = Bundle()
        val year = view?.findViewById<EditText>(R.id.year)?.text.toString()
        if((year.toString()).length==4){
            try{    /*Now, you have to create a Hashset called 'JournalDates' and populate it here!! And then you pass it to the 'DaysFragment' which in turn is passed to the
                      'CalendarAdapter' class to place the red dots on 'OnBindViewHolder()'. So, After placing the red dots properly, Now, when a date is clicked in the 'DaysFragment'
                       the 'OnItemClick()' function should check for the background and if the background is null, the 'WriteActivity' should start, if a 'Red dot' is present,
                       the 'showAlertDialog()' function should be called which in turn calls 'startViewJournal()' which starts the 'ViewJournal' activity!!*/

                val db = FirebaseFirestore.getInstance()
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val journalDates = ArrayList<String>()

                db.collection("Journals")
                    .whereEqualTo("userID",uid)
                    .whereEqualTo("month", month)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            for(document in it.result){
                                Log.d("journalDate", document.get("day").toString())
                                journalDates.add(document.get("day").toString())
                            }
                            Log.d("journalDates", journalDates.toString())
                            year.toInt()
                            bundle.putStringArrayList("journalDates", journalDates)
                            bundle.putString("year", year)
                            bundle.putString("month", month)
                            daysFragment.arguments = bundle
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.main_container, daysFragment)
                                ?.addToBackStack(null)
                                ?.commit()

                            val manager = requireActivity().supportFragmentManager
                            manager.beginTransaction().remove(this).commit()

                        }else{
                            Log.e("Error fetching document", "Document Varla vro!!")
                        }
                    }
            }catch(_:java.lang.NumberFormatException){
                Toast.makeText(requireContext(), "Enter a Valid Year !!", Toast.LENGTH_SHORT).show()
            }
        }
        else Toast.makeText(requireContext(), "Enter a Valid Year !!", Toast.LENGTH_SHORT).show()
    }

}