package com.example.journalapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth


class DaysFragment : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var month: String
    private lateinit var year: String
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var daysInMonthLst:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        month = arguments?.getString("month").toString()
        year = arguments?.getString("year").toString()
        initWidgets()
    }

    private fun setMonthView() {
        val calendarAdapter = CalendarAdapter(daysInMonthLst, this)
        val layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.month_name).text = month
        view.findViewById<TextView>(R.id.year_name).text = year
        calendarRecyclerView = view.findViewById(R.id.calRecyclerView)
        setMonthView()
    }

    private fun initWidgets() {
        daysInMonthLst = arrayListOf()
        daysInMonthArray(month, year)
    }


    private fun daysInMonthArray(month: String, year: String){
        val date = LocalDate.of(year.toInt(), Month.valueOf(month.uppercase()), 1)
        val yearMonth:YearMonth = YearMonth.from(date)
        val daysInMonth:Int = yearMonth.lengthOfMonth()
        val firstOfMonth:LocalDate = date.withDayOfMonth(1)
        val dayOfWeek: Int = firstOfMonth.dayOfWeek.value

        for(i in 1..42){
            if(i<=dayOfWeek || i>daysInMonth+dayOfWeek) daysInMonthLst.add("")
            else daysInMonthLst.add("${i-dayOfWeek}")
        }
    }

    override fun onItemClick(position: Int, dayText: String, dot:View) {
        if(dayText!="") showAlertDialog(dayText)

    }

    @SuppressLint("MissingInflatedId")
    private fun showAlertDialog(dayText: String) {
        val dialogView:View = layoutInflater.inflate(R.layout.dialog_layout, null)
//        dialogView.findViewById<TextView>(R.id.dialog).text = "Today is $dayText"
        dialogView.findViewById<TextView>(R.id.title).text = "A Busy Day !!"
        dialogView.findViewById<TextView>(R.id.content).text = "Today was a busy day, I started the day by going for a morning walk and enjoying the beauty of nature..."

        val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.dialog)
        builder.setView(dialogView).create()
        builder.show()

        val bundle = Bundle()

        dialogView.findViewById<View>(R.id.read).setOnClickListener{
            val intent : Intent = Intent(activity, ViewJournalActivity::class.java)
            intent.putExtra(month, "month")
            intent.putExtra(year, "year")
            intent.putExtra(dayText, "day")
            startActivity(intent)
        }

    }
}