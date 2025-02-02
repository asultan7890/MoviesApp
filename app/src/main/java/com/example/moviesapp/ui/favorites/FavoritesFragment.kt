package com.example.moviesapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.moviesapp.Details
import com.example.moviesapp.ListAdapter
import com.example.moviesapp.MySingleton
import com.example.moviesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class FavoritesFragment : Fragment() {
    private lateinit var user: FirebaseUser

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        // refresh button
        val refresh = root.findViewById<Button>(R.id.refresh)
        refresh.setOnClickListener {
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
        }

        // get favorites list from database
        val db = Firebase.firestore
        user = FirebaseAuth.getInstance().currentUser!!
        val doc = db.collection("users").document(user.email.toString())
        doc.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val favorites = document.get("favorites") as List<String>
                    var favlist = mutableListOf<List<String>>()

                    // display message if there are no favorites
                    if (favorites.drop(1).isEmpty()){
                        Toast.makeText(activity,"No favorites to display", Toast.LENGTH_LONG).show()
                    }
                    // use api to get information about favorites based on their id's
                    else {
                        for (x in favorites.drop(1)) {
                            val url = "https://www.omdbapi.com/?apikey=cf10626c&i=$x"
                            val gson = Gson()

                            // request and parse the json movie results
                            val jsonObjectRequest = JsonObjectRequest(
                                Request.Method.GET, url, null,
                                Response.Listener { response ->
                                    val responses = response.toString()
                                    val results = gson.fromJson(responses, Details::class.java)

                                    val item = listOf(x,results.Title, results.Year)
                                    favlist.add(item)
                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(activity,"Error loading from API: $error", Toast.LENGTH_LONG).show()
                                })

                            // Access the RequestQueue through your singleton class.
                            activity?.let { MySingleton.getInstance(it).addToRequestQueue(jsonObjectRequest) } }

                        // display favorites using recycler view
                        Thread.sleep(500)
                        val recycler = root.findViewById<RecyclerView>(R.id.recycler)
                        recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        val listadapter = ListAdapter(favlist)
                        recycler.adapter = listadapter }
                } }
            .addOnFailureListener { exception ->
                Toast.makeText(activity,"Error loading from database: $exception", Toast.LENGTH_LONG).show() }

        return root
    }
}
