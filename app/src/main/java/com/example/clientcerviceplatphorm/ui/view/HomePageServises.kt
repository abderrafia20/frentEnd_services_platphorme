package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView

class HomePageServises : AppCompatActivity() {

    private lateinit var btnMenu: ImageButton
    private val viewModel: ViewModelUser by viewModels()
    private lateinit var recycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_page_servises)



        val userId = intent.getStringExtra("id")?: "not fond"

        if (userId != "not fond") {
            viewModel.getUserById(userId)
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        val navigateView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigateView.getHeaderView(0)
        val txname = headerView.findViewById<TextView>(R.id.txtName)
        val txemail = headerView.findViewById<TextView>(R.id.txtEmail)
        val txphone = headerView.findViewById<TextView>(R.id.txtPhone)
        val txlougout = headerView.findViewById<TextView>(R.id.txtLogout)

        recycler = findViewById(R.id.recycler)
        btnMenu = findViewById(R.id.btnMenu)


        viewModel.userById.observe(this){user ->
            user?.let {
                txname.text = user.getName()
                txemail.text = user.getEmail()
                txphone.text = user.getPhone()
            }
        }


        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        txlougout.setOnClickListener {
            Toast.makeText(this, "lougout this acount!", Toast.LENGTH_SHORT).show()

            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

    }



}