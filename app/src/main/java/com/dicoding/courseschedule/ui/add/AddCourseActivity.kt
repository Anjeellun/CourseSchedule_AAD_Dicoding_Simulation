package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = AddCourseVMFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                val edCourseName = findViewById<TextInputEditText>(R.id.edcoursename).text.toString()
                val spinnerDay = findViewById<Spinner>(R.id.spinnerday).selectedItem.toString()
                val spinnerDayNumber = getDayNumberByDayName(spinnerDay)
                val edLecturer = findViewById<TextInputEditText>(R.id.edlecturer).text.toString()
                val edNote = findViewById<TextInputEditText>(R.id.ednote).text.toString()

                when {
                    edCourseName.isEmpty() -> false
                    startTime.isEmpty() -> false
                    endTime.isEmpty() -> false
                    spinnerDayNumber == -1 -> false
                    edLecturer.isEmpty() -> false
                    edNote.isEmpty() -> false
                    else -> {
                        viewModel.insertCourse(
                            edCourseName,
                            spinnerDayNumber,
                            startTime,
                            endTime,
                            edLecturer,
                            edNote
                        )
                        finish()
                        true
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePicker(view: View) {
        val tag = when (view.id) {
            R.id.ibstarttime -> "start_time"
            R.id.ibendtime -> "end_time"
            else -> "default"
        }

        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            "start_time" -> {
                findViewById<TextView>(R.id.tvstarttime).text = timeFormat.format(calendar.time)
                startTime = timeFormat.format(calendar.time)
            }
            "end_time" -> {
                findViewById<TextView>(R.id.tvendtime).text = timeFormat.format(calendar.time)
                endTime = timeFormat.format(calendar.time)
            }
        }
    }

    private fun getDayNumberByDayName(dayName: String): Int {
        val days = resources.getStringArray(R.array.day)
        return days.indexOf(dayName)
    }
}