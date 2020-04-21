package com.example.moviesapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.squareup.picasso.Picasso

data class Details(
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Actors: String,
    val Plot: String,
    val Poster: String,
    val imdbRating: String,
    val imdbID: String,
    val Type: String
)

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val id = intent.getStringExtra("ID")
        val title = findViewById<TextView>(R.id.detail_title)
        val poster = findViewById<ImageView>(R.id.poster)
        val date = findViewById<TextView>(R.id.date)
        val rating = findViewById<TextView>(R.id.rating)
        val runtime = findViewById<TextView>(R.id.runtime)
        val actors = findViewById<TextView>(R.id.actors)
        val plot = findViewById<TextView>(R.id.plot)
        val director = findViewById<TextView>(R.id.director)
        val imdb = findViewById<TextView>(R.id.imdb)
        val genre = findViewById<TextView>(R.id.genre)



        val url = "https://www.omdbapi.com/?apikey=cf10626c&i=$id"
        val gson = Gson()

        //Request and parse the json movie results
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val responses = response.toString()
                val results = gson.fromJson(responses, Details::class.java)

                title.text = results.Title
                Picasso.get().load(results.Poster).into(poster)
                date.text = results.Released
                rating.text = results.Rated
                runtime.text = results.Runtime
                actors.text = results.Actors
                plot.text = results.Plot
                director.text = results.Director
                imdb.text = results.imdbRating
                genre.text = results.Genre
            },
            Response.ErrorListener { error ->
                title.text = "ERROR: %s".format(error.toString())
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
