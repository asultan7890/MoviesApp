package com.example.moviesapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.squareup.picasso.Picasso

// data class to store json results
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
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        // get variables
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
        val fav = findViewById<ToggleButton>(R.id.favorite_button)

        val url = "https://www.omdbapi.com/?apikey=cf10626c&i=$id"
        val gson = Gson()

        // request and parse the json movie results
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val responses = response.toString()
                val results = gson.fromJson(responses, Details::class.java)

                // update screen with details from API
                title.text = results.Title
                Picasso.get().load(results.Poster).into(poster)
                date.text = results.Released
                rating.text = results.Rated
                runtime.text = results.Runtime
                actors.text = results.Actors
                plot.text = results.Plot
                director.text = "Directed By: " + results.Director
                imdb.text = "IMDB Rating: " + results.imdbRating + "/10"
                genre.text = results.Genre
            },
            Response.ErrorListener { error ->
                title.text = "ERROR: %s".format(error.toString())
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        val db = Firebase.firestore
        user = FirebaseAuth.getInstance().currentUser!!

        // check if movie/series is already favorited
        // if it is, make sure favorites button is toggled on
        val doc = db.collection("users").document(user.email.toString())
        doc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val favorites = document.get("favorites") as List<*>
                    for (x in favorites){
                        if (x.toString() == id){
                            if (!fav.isChecked){
                                fav.toggle()} } }}}
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Error loading from database: $exception",Toast.LENGTH_LONG).show() }

        // favorites button functionality
        fav.setOnCheckedChangeListener { _, isChecked ->
            // save new movie/series to favorites database
            if (isChecked){
                doc.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val favorites = document.get("favorites") as List<*>
                            var found = false
                            for (x in favorites){
                                if (x.toString() == id){
                                    found = true }
                            if (!found){
                                doc.update("favorites", FieldValue.arrayUnion(id))
                            }}}}
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error adding to database: $exception",Toast.LENGTH_LONG).show() }
            }
            // delete movie/series from favorites database
            if (!isChecked){
                doc.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val favorites = document.get("favorites") as List<*>
                            var found = false
                            for (x in favorites){
                                if (x.toString() == id){
                                    found = true }
                            if (found){
                                doc.update("favorites", FieldValue.arrayRemove(id))
                            }}}}
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,"Error removing from database: $exception",Toast.LENGTH_LONG).show() }
            }
        }
    }
    // back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
