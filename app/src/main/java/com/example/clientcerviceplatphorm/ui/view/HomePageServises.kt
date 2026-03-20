package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.adapter.AdapterService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView

class HomePageServises : AppCompatActivity() {

    private lateinit var btnMenu: ImageButton
    private lateinit var recycler: RecyclerView
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_servises)

        // init views
        drawerLayout = findViewById(R.id.drawerLayout)
        btnMenu = findViewById(R.id.btnMenu)
        recycler = findViewById(R.id.recycler)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        val txname = headerView.findViewById<TextView>(R.id.txtName)
        val txemail = headerView.findViewById<TextView>(R.id.txtEmail)
        val txphone = headerView.findViewById<TextView>(R.id.txtPhone)
        val txlogout = headerView.findViewById<TextView>(R.id.txtLogout)

        // RecyclerView setup
        recycler.layoutManager = LinearLayoutManager(this)

        // get user
        val userId = intent.getStringExtra("id") ?: ""

        if (userId.isNotEmpty()) {
            viewModel.getUserById(userId)
        }

        viewModel.userById.observe(this) { user ->
            user?.let {
                txname.text = it.getName()
                txemail.text = it.getEmail()
                txphone.text = it.getPhone()
            }
        }

        // open drawer
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // logout
        txlogout.setOnClickListener {
            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

        // get services
        viewModelService.getServices()

        viewModelService.services.observe(this) { services ->
            if (services != null) {

                Toast.makeText(this, "Services: ${services.size}", Toast.LENGTH_SHORT).show()
                recycler.layoutManager = LinearLayoutManager(this)
                recycler.adapter = AdapterService(services)
            } else {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}