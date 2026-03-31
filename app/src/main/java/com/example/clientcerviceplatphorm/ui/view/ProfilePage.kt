package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.model.adapter.AdapterService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView

class ProfilePage : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var recyclerV: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageButton
    private lateinit var tvServicesTitle: TextView

    private lateinit var txnameHeader: TextView
    private lateinit var txemailHeader: TextView

    private var idF = ""
    private var idUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        initViews()
        setupMenu()

        idUser = intent.getStringExtra("id") ?: ""

        if (idUser.isNotEmpty()) {
            viewModelUser.getUserById(idUser)
        }

        viewModelUser.userById.observe(this) { user ->
            user?.let {
                txtName.text = it.getName()
                txtEmail.text = it.getEmail()
                txtPhone.text = it.getPhone()

                // Update Header
                txnameHeader.text = it.getName()
                txemailHeader.text = it.getEmail()

                if (it is User.FournisseurUser) {
                    idF = it.getId()
                    viewModelService.getServices()
                    tvServicesTitle.visibility = View.VISIBLE
                } else {
                    tvServicesTitle.visibility = View.GONE
                }

                setupBottomNavigation(it)
            }
        }

        viewModelService.services.observe(this) { services ->
            if (idF.isNotEmpty()) {
                val myServices = services.filter { it.fournisseurId == idF }

                recyclerV.adapter = AdapterService(myServices) { service ->
                    val intent = Intent(this, DetailServicePage::class.java)
                    intent.putExtra("idService", service.id)
                    intent.putExtra("idUser", idUser)
                    startActivity(intent)
                }
            }
        }

        viewModelUser.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        txtName = findViewById(R.id.etNameP)
        txtEmail = findViewById(R.id.etEmailP)
        txtPhone = findViewById(R.id.etPhoneP)
        recyclerV = findViewById(R.id.recyclerP)
        tvServicesTitle = findViewById(R.id.tvServicesTitle)
        recyclerV.layoutManager = LinearLayoutManager(this)

        drawerLayout = findViewById(R.id.drawerLayout)
        btnMenu = findViewById(R.id.btnMenu)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        txnameHeader = headerView.findViewById(R.id.txtName)
        txemailHeader = headerView.findViewById(R.id.txtEmail)


        headerView.findViewById<View>(R.id.txtLogout).setOnClickListener {
            logout()
        }
        headerView.findViewById<View>(R.id.txtUpdate).setOnClickListener {
            val intent = Intent(this, UpdateAccountPage::class.java)
            intent.putExtra("id", idUser)
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
                viewModelUser.deleteUser(idUser)
                logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupMenu() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}