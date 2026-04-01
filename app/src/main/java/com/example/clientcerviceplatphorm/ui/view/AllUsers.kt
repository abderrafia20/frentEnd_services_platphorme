package com.example.clientcerviceplatphorm.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.model.adapter.AdapterUser
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class AllUsers : AppCompatActivity() {

    private val viewModelUser: ViewModelUser by viewModels()
    private lateinit var recyclerAllUsers: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_users)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        viewModelUser.getUsers()
        
        viewModelUser.users.observe(this) { users ->
            if (users != null) {
                recyclerAllUsers.adapter = AdapterUser(users) { user ->
                    when (user){
                        is User.AdminUser -> Toast.makeText(this, "is Admin", Toast.LENGTH_SHORT).show()
                        is User.FournisseurUser -> Toast.makeText(this, "is Furnisseur", Toast.LENGTH_SHORT).show()
                        is User.ClientUser -> Toast.makeText(this, "is Client", Toast.LENGTH_SHORT).show()

                    }

                }
            }
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbarAllUsers)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerAllUsers = findViewById(R.id.recyclerAllUsers)
        recyclerAllUsers.layoutManager = LinearLayoutManager(this)
    }
}