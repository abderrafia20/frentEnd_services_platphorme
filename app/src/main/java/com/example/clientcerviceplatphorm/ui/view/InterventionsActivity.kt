package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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

class InterventionsActivity : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()
    private val viewModelServiceRequest: ViewModelServiceRequest by viewModels()

    private lateinit var etNameI: TextView
    private lateinit var recyclerR: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interventions)

        etNameI = findViewById(R.id.etNameI)
        recyclerR = findViewById(R.id.recyclerR)

        recyclerR.layoutManager = LinearLayoutManager(this)

        val idUser = intent.getStringExtra("id") ?: ""

        viewModelUser.getUserById(idUser)
        viewModelUser.getUsers()
        viewModelService.getServices()
        viewModelServiceRequest.getServiceRequests()

        observeData()
    }

    private fun observeData() {

        viewModelUser.userById.observe(this) { user ->
            val allUsers = viewModelUser.users.value ?: emptyList()
            val services = viewModelService.services.value ?: emptyList()
            val serviceRequests = viewModelServiceRequest.serviceRequests.value ?: emptyList()

            if (user != null) {
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