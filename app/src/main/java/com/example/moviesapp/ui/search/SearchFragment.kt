package com.example.moviesapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviesapp.R
import com.example.moviesapp.ResultsActivity

class SearchFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        // set variables for components of screen
        val title = root.findViewById<TextView>(R.id.title)
        val year = root.findViewById<TextView>(R.id.year)
        val movie = root.findViewById<CheckBox>(R.id.checkBox)
        val series = root.findViewById<CheckBox>(R.id.checkBox2)
        val search = root.findViewById<Button>(R.id.button)

        // search functionality
        search.setOnClickListener {
            if (title.text.toString() == ""){
                Toast.makeText(activity, "Please enter a title", Toast.LENGTH_LONG).show()
            }
            else if (!movie.isChecked and !series.isChecked){
                Toast.makeText(activity, "Please select Movie, Series, or both of them", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(activity, ResultsActivity::class.java)
                intent.putExtra("Title",title.text.toString())
                intent.putExtra("Year",year.text.toString())
                intent.putExtra("Movie",movie.isChecked)
                intent.putExtra("Series",series.isChecked)
                intent.putExtra("Page", 1)
                startActivity(intent)
            }
        }
        return root
    }
}
