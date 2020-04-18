package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val title = intent.getStringExtra("Title")
        val year = intent.getStringExtra("Year")
        val movie = intent.getBooleanExtra("Movie", true)
        val series = intent.getBooleanExtra("Series", true)

        val test = findViewById<TextView>(R.id.testtext)
        test.append(title.toString())
        test.append(year.toString())
        test.append(movie.toString())
        test.append(series.toString())
    }
}
