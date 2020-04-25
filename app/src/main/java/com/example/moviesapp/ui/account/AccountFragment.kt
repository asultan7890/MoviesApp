package com.example.moviesapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviesapp.LoginActivity
import com.example.moviesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountFragment : Fragment() {

    private lateinit var user: FirebaseUser

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        val logout = root.findViewById<Button>(R.id.logout)
        user = FirebaseAuth.getInstance().currentUser!!
        val userinfo = root.findViewById<TextView>(R.id.userinfo)
        userinfo.append(user.email)
        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return root
    }
}
