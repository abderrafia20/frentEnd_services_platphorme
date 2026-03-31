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
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.model.ServiceRequestItem
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.model.adapter.AdapterServiceRequest
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelServiceRequest
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView

class InterventionsActivity : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()
    private val viewModelServiceRequest: ViewModelServiceRequest by viewModels()

    private lateinit var etNameI: TextView
    private lateinit var recyclerR: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageButton
    
    private lateinit var txnameHeader: TextView
    private lateinit var txemailHeader: TextView
    
    private var idUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interventions)

        initViews()
        setupMenu()

        idUser = intent.getStringExtra("id") ?: ""

        viewModelUser.getUserById(idUser)
        viewModelUser.getUsers()
        viewModelService.getServices()
        viewModelServiceRequest.getServiceRequests()

        observeData()
    }

    private fun initViews() {
        etNameI = findViewById(R.id.etNameI)
        recyclerR = findViewById(R.id.recyclerR)
        drawerLayout = findViewById(R.id.drawerLayout)
        btnMenu = findViewById(R.id.btnMenu)
        
        recyclerR.layoutManager = LinearLayoutManager(this)

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

    private fun observeData() {
        viewModelUser.userById.observe(this) { user ->
            val allUsers = viewModelUser.users.value ?: emptyList()
            val services = viewModelService.services.value ?: emptyList()
            val serviceRequests = viewModelServiceRequest.serviceRequests.value ?: emptyList()

            if (user != null) {
                txnameHeader.text = user.getName()
                txemailHeader.text = user.getEmail()
                updateRecycler(user, allUsers, services, serviceRequests)
            }
        }

        viewModelService.services.observe(this) { services ->
            val user = viewModelUser.userById.value ?: return@observe
            val allUsers = viewModelUser.users.value ?: emptyList()
            val serviceRequests = viewModelServiceRequest.serviceRequests.value ?: emptyList()

            updateRecycler(user, allUsers, services, serviceRequests)
        }

        viewModelServiceRequest.serviceRequests.observe(this) { serviceRequests ->
            val user = viewModelUser.userById.value ?: return@observe
            val allUsers = viewModelUser.users.value ?: emptyList()
            val services = viewModelService.services.value ?: emptyList()

            updateRecycler(user, allUsers, services, serviceRequests)
        }

        viewModelUser.users.observe(this) { allUsers ->
            val user = viewModelUser.userById.value ?: return@observe
            val services = viewModelService.services.value ?: emptyList()
            val serviceRequests = viewModelServiceRequest.serviceRequests.value ?: emptyList()

            updateRecycler(user, allUsers, services, serviceRequests)
        }
    }

    private fun updateRecycler(
        user: User,
        allUsers: List<User>,
        services: List<Service>,
        serviceRequests: List<ServiceRequest>
    ) {
        etNameI.text = user.getName()
        setupBottomNavigation(user)

        val items = when (user) {
            is User.AdminUser -> {
                serviceRequests.mapNotNull { sr ->
                    val service = services.find { it.id == sr.serviceId } ?: return@mapNotNull null
                    val client = allUsers.find { it.getId() == sr.clientId } ?: return@mapNotNull null
                    val fournisseur = allUsers.find { it.getId() == sr.fournisseurId } ?: return@mapNotNull null

                    ServiceRequestItem(
                        serviceRequest = sr,
                        title = service.title,
                        clientName = "Client: ${client.getName()} | Fornisseur: ${fournisseur.getName()}",
                        price = "${service.price}$"
                    )
                }
            }
            is User.FournisseurUser -> {
                val myRequests = serviceRequests.filter { it.fournisseurId == user.getId() }
                myRequests.mapNotNull { sr ->
                    val service = services.find { it.id == sr.serviceId } ?: return@mapNotNull null
                    val client = allUsers.find { it.getId() == sr.clientId } ?: return@mapNotNull null

                    ServiceRequestItem(
                        serviceRequest = sr,
                        title = service.title,
                        clientName = client.getName(),
                        price = "${service.price}$"
                    )
                }
            }
            is User.ClientUser -> {
                val myRequests = serviceRequests.filter { it.clientId == user.getId() }
                myRequests.mapNotNull { sr ->
                    val service = services.find { it.id == sr.serviceId } ?: return@mapNotNull null
                    val fournisseur = allUsers.find { it.getId() == sr.fournisseurId } ?: return@mapNotNull null

                    ServiceRequestItem(
                        serviceRequest = sr,
                        title = service.title,
                        clientName = fournisseur.getName(),
                        price = "${service.price}$"
                    )
                }
            }
            else -> emptyList()
        }

        recyclerR.adapter = AdapterServiceRequest(items) { item ->
            val intent = Intent(this, DetailSerReq::class.java)
            intent.putExtra("idUser", user.getId())
            intent.putExtra("Title", item.title)
            intent.putExtra("name", item.clientName)
            intent.putExtra("price", item.price)
            intent.putExtra("idSR", item.serviceRequest.id)
            intent.putExtra("descrSR", item.serviceRequest.decreption)
            intent.putExtra("date", item.serviceRequest.date)
            intent.putExtra("status", "${item.serviceRequest.status}")
            startActivity(intent)
        }
    }
}
