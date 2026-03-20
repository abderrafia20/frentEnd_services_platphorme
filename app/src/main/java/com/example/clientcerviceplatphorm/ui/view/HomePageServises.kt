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

    private lateinit var txname: TextView
    private lateinit var txemail: TextView
    private lateinit var txphone: TextView
    private lateinit var txlogout: TextView

    private val viewModel: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_servises)

        initViews()
        setupRecycler()
        getUserId()
        observeUser()
        setupMenu()
        setupLogout()
        loadServices()
        observeServices()
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        btnMenu = findViewById(R.id.btnMenu)
        recycler = findViewById(R.id.recycler)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        txname = headerView.findViewById(R.id.txtName)
        txemail = headerView.findViewById(R.id.txtEmail)
        txphone = headerView.findViewById(R.id.txtPhone)
        txlogout = headerView.findViewById(R.id.txtLogout)
    }

    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserId() {
        userId = intent.getStringExtra("id") ?: ""
        if (userId.isNotEmpty()) {
            viewModel.getUserById(userId)
        }
    }

    private fun observeUser() {
        viewModel.userById.observe(this) { user ->
            user?.let {
                txname.text = it.getName()
                txemail.text = it.getEmail()
                txphone.text = it.getPhone()
            }
        }
    }

    private fun setupMenu() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupLogout() {
        txlogout.setOnClickListener {
            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    private fun loadServices() {
        viewModelService.getServices()
    }

    private fun observeServices() {
        viewModelService.services.observe(this) { services ->
            if (services != null) {
                Toast.makeText(this, "Services: ${services.size}", Toast.LENGTH_SHORT).show()
                recycler.adapter = AdapterService(services)
            } else {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}