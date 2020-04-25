package com.example.moviesapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.ListAdapter
import com.example.moviesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesFragment : Fragment() {
    private lateinit var user: FirebaseUser

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        val db = Firebase.firestore
        user = FirebaseAuth.getInstance().currentUser!!
        val doc = db.collection("users").document(user.email.toString())
        doc.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val favorites = document.get("favorites") as List<String>

                    //Toast.makeText(activity,favorites.toString(),Toast.LENGTH_LONG).show()
                    val recycler = root.findViewById<RecyclerView>(R.id.recycler)
                    recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

                    val listadapter = ListAdapter(favorites.drop(1))
                    recycler.adapter = listadapter}}
            .addOnFailureListener { exception ->
                Toast.makeText(activity,"Error loading from database: $exception", Toast.LENGTH_LONG).show() }

        return root
    }
}
