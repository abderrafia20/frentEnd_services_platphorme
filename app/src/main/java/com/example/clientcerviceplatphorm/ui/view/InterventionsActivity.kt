package com.example.clientcerviceplatphorm.ui.view

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class InterventionsActivity : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interventions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNameI = findViewById<TextView>(R.id.etNameI)

        val idUser = intent.getStringExtra("id") ?: ""
        viewModelUser.getUserById(idUser)

        viewModelUser.userById.observe(this) { user ->
            user?.let {
                etNameI.text = user.getName()
                setupBottomNavigation(it)
            }
        }
    }
}