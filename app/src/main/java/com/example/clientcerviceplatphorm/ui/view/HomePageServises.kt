package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.model.adapter.AdapterService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView

class HomePageServises : BaseActivity() {

    private lateinit var btnMenu: ImageButton
    private lateinit var recycler: RecyclerView
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var txnameHeader: TextView
    private lateinit var txemailHeader: TextView
    private lateinit var etSearch: EditText

    private var allServices: List<Service> = emptyList()

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_servises)

        initViews()
        setupRecycler()
        getUserId()
        setupMenu()
        observeUser()
        loadServices()
        observeServices()
        setupSearch()
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        btnMenu = findViewById(R.id.btnMenu)
        etSearch = findViewById(R.id.etSearch)
        recycler = findViewById(R.id.recycler)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        txnameHeader = headerView.findViewById(R.id.txtName)
        txemailHeader = headerView.findViewById(R.id.txtEmail)
        
        // Drawer Listeners
        headerView.findViewById<View>(R.id.txtLogout).setOnClickListener {
            logout()
        }
        headerView.findViewById<View>(R.id.txtUpdate).setOnClickListener {
            val intent = Intent(this, UpdateAccountPage::class.java)
            intent.putExtra("id", userId)
            startActivity(intent)
        }
        headerView.findViewById<View>(R.id.txtDelete).setOnClickListener {
            showDeleteConfirmDialog()
        }
    }

    private fun logout() {
        getSharedPreferences("USER_PREF", MODE_PRIVATE).edit().clear().apply()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginPage::class.java))
        finishAffinity()
    }

    private fun showDeleteConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Yes") { _, _ ->
                viewModelUser.deleteUser(userId)
                logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserId() {
        userId = intent.getStringExtra("id") ?: ""
        if (userId.isNotEmpty()) {
            viewModelUser.getUserById(userId)
        }
    }

    private fun observeUser() {
        viewModelUser.userById.observe(this) { user ->
            user?.let {
                txnameHeader.text = it.getName()
                txemailHeader.text = it.getEmail()
                setupBottomNavigation(it)
            }
        }
    }

    private fun setupMenu() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun loadServices() {
        viewModelService.getServices()
    }

    private fun observeServices() {
        viewModelService.services.observe(this) { services ->
            if (services != null) {
                allServices = services
                recycler.adapter = AdapterService(services){ service ->
                    val intent = Intent(this, DetailServicePage::class.java)
                    intent.putExtra("idService", service.id)
                    intent.putExtra("idUser", userId)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener {
            val query = it.toString().lowercase()
            val filteredList = allServices.filter { service ->
                service.title.lowercase().contains(query) ||
                service.nameFournisseur.lowercase().contains(query)||
                service.price.toString().lowercase().contains(query)
            }
            recycler.adapter = AdapterService(filteredList) { service ->
                val intent = Intent(this, DetailServicePage::class.java)
                intent.putExtra("idService", service.id)
                intent.putExtra("idUser", userId)
                startActivity(intent)
            }
        }
    }
}