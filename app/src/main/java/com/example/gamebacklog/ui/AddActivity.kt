package com.example.gamebacklog.ui

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.gamebacklog.model.GameViewModel
import kotlinx.android.synthetic.main.activity_add.*
import android.widget.Toast
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game
import java.text.SimpleDateFormat


class AddActivity : AppCompatActivity() {

    private lateinit var gameViewModel: GameViewModel
    val numOfMonths = 12
    val firstMonth = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_add_activity)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        fab.setOnClickListener {
            if (checkForCorrectValues()) {
                val formatter = SimpleDateFormat(getString(R.string.format_date))
                gameViewModel.insertGame(
                    Game(
                        etTitle.text.toString(),
                        etPlatform.text.toString(),
                        getString(R.string.game_release, getCurrentDateString(getString(R.string.splitter_space), true)),
                        formatter.parse(getCurrentDateString(getString(R.string.splitter_min), false)))
                )
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkForCorrectValues(): Boolean {
        if (etDay.text!!.isBlank() || etMonth.text!!.isBlank() || etYear.text!!.isBlank()
            || !correctDate())
        {
            Toast.makeText(this, getString(R.string.field_check_date), Toast.LENGTH_LONG).show()
            return false
        }
        else if (etTitle.text!!.isBlank()) {
            Toast.makeText(this, getString(R.string.field_check_title), Toast.LENGTH_LONG).show()
            return false
        }
        else if (etPlatform.text!!.isBlank()) {
            Toast.makeText(this, getString(R.string.field_check_platform), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun correctDate() : Boolean {
        val df = SimpleDateFormat(getString(R.string.format_date))
        df.isLenient = false
        val date = getCurrentDateString(getString(R.string.splitter_min), false)
        try {
            df.parse(date)
        } catch (e: Exception) {
            return false
        }

        return true
    }

    private fun getCurrentDateString(splitter: String, monthText: Boolean) : String {
        var dateString = etDay.text.toString() + splitter
        if (!monthText)
            dateString = dateString + etMonth.text.toString()
        else {
            val incorrectMonth = etMonth.text.toString()
            val monthNumber = etMonth.text.toString().toIntOrNull()
            val months = getResources().getStringArray(R.array.months)
            if (monthNumber != null && monthNumber in firstMonth..numOfMonths) {
                dateString = dateString + months[monthNumber-1]
            }
            else
                dateString = dateString + incorrectMonth
        }
        return dateString + splitter + etYear.text.toString()
    }
}
