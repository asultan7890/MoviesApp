package com.example.moviesapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}

data class SearchResults(
    val Search: List<Search>,
    val totalResults: Int
)
class Search(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
)

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val title = intent.getStringExtra("Title")
        val year = intent.getStringExtra("Year")
        val movie = intent.getBooleanExtra("Movie", true)
        val series = intent.getBooleanExtra("Series", true)
        val page = intent.getIntExtra("Page", 1)

        val page_text = findViewById<TextView>(R.id.page)
        page_text.text = "Page " + page.toString()
        val next = findViewById<Button>(R.id.next)
        val prev = findViewById<Button>(R.id.previous)
        var total = 0
        val result = listOf<TextView>(findViewById(R.id.result1),findViewById(R.id.result2),
            findViewById(R.id.result3),findViewById(R.id.result4),findViewById(R.id.result5),
            findViewById(R.id.result6),findViewById(R.id.result7),findViewById(R.id.result8),
            findViewById(R.id.result9),findViewById(R.id.result10))

        val result1 = findViewById<TextView>(R.id.result1)
        val result2 = findViewById<TextView>(R.id.result2)
        val result3 = findViewById<TextView>(R.id.result3)
        val result4 = findViewById<TextView>(R.id.result4)
        val result5 = findViewById<TextView>(R.id.result5)
        val result6 = findViewById<TextView>(R.id.result6)
        val result7 = findViewById<TextView>(R.id.result7)
        val result8 = findViewById<TextView>(R.id.result8)
        val result9 = findViewById<TextView>(R.id.result9)
        val result10 = findViewById<TextView>(R.id.result10)

        var url = ""
        var gson = Gson()


        if (year.toString() == ""){
            if (movie and !series){
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&type=movie" + "&page=" + page
            }
            else if (!movie and series){
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&type=series" + "&page=" + page
            }
            else{
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&page=" + page
            }
        }
        else{
            if (movie and !series){
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&type=movie&y=" + year.toString() + "&page=" + page
            }
            else if (!movie and series){
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&type=series&y=" + year.toString() + "&page=" + page
            }
            else{
                url = "https://www.omdbapi.com/?apikey=cf10626c&s=" + title.toString() + "&y=" + year.toString() + "&page=" + page
            }
        }
        //Request and parse the json weather results
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                var responses = response.toString()
                var results = gson.fromJson(responses, SearchResults::class.java)
                total = results.totalResults
                //results.Search.size
                for (x in results.Search.indices){
                    result[x].text = results.Search[x].Title
                }
                /*result[0].text = results.Search[0].Title
                result[1].text = results.Search[1].Title
                result[2].text = results.Search[2].Title
                result[3].text = results.Search[3].Title
                result[4].text = results.Search[4].Title
                result[5].text = results.Search[5].Title
                result[6].text = results.Search[6].Title
                result[7].text = results.Search[7].Title
                result[8].text = results.Search[8].Title
                result[9].text = results.Search[9].Title*/


            },
            Response.ErrorListener { error ->
                result1.text = "ERROR: %s".format(error.toString())
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        prev.setOnClickListener {
            if (page == 1){
                Toast.makeText(this, "Already viewing the first page", Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this@ResultsActivity, ResultsActivity::class.java)
                intent.putExtra("Title", title.toString())
                intent.putExtra("Year", year.toString())
                intent.putExtra("Movie", movie)
                intent.putExtra("Series", series)
                intent.putExtra("Page", page - 1)
                startActivity(intent)
            }
        }

        next.setOnClickListener {
            if ((total-(10*page)) < 0){
                Toast.makeText(this, "No more results to view", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this@ResultsActivity, ResultsActivity::class.java)
                intent.putExtra("Title",title.toString())
                intent.putExtra("Year",year.toString())
                intent.putExtra("Movie",movie)
                intent.putExtra("Series",series)
                intent.putExtra("Page", page + 1)
                startActivity(intent)
            }
        }
    }
}
