package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.model.StatusSR
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelServiceRequest
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class DetailServicePage : AppCompatActivity() {

    private val viewModelService: ViewModelService by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelServiceRequest: ViewModelServiceRequest by viewModels()

    private lateinit var toolbar: Toolbar
    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtFournisseur: TextView
    private lateinit var imgService: ImageView
    private lateinit var notImageT: TextView

    private lateinit var errorSR: TextView
    private lateinit var viewCreatSR: LinearLayout
    private lateinit var viewAdminFournisseur: LinearLayout
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private var currentService: Service? = null
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_service_page)

        initViews()
        
        val serviceId = intent.getStringExtra("idService") ?: ""
        userId = intent.getStringExtra("idUser") ?: ""

        if (serviceId.isNotEmpty()) {
            viewModelService.getServiceById(serviceId)
        }
        if (userId.isNotEmpty()) {
            viewModelUser.getUserById(userId)
        }

        observeData()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        txtTitle = findViewById(R.id.txtTitleD)
        txtDescription = findViewById(R.id.txtDescriptionD)
        txtPrice = findViewById(R.id.txtPriceD)
        txtFournisseur = findViewById(R.id.txtFournisseurD)
        imgService = findViewById(R.id.imgServiceD)
        notImageT = findViewById(R.id.txtNoImage)
        viewCreatSR = findViewById(R.id.VcreateSR)
        
        viewAdminFournisseur = findViewById(R.id.viewAdminFournisseur)
        btnUpdate = findViewById(R.id.bntUpdate)
        btnDelete = findViewById(R.id.bntDelete)
    }

    private fun observeData() {
        viewModelUser.userById.observe(this) { user ->
            checkPermissions(user, currentService)
        }

        viewModelService.SrviceById.observe(this) { service ->
            currentService = service
            service?.let {
                txtTitle.text = it.title
                txtDescription.text = it.description
                txtPrice.text = "Price: ${it.price} DH"
                txtFournisseur.text = "Provider: ${it.nameFournisseur}"
                
                if (!it.image.isNullOrEmpty()) {
                    try {
                        val imageBytes = Base64.decode(it.image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        imgService.setImageBitmap(bitmap)
                        notImageT.visibility = View.GONE
                    } catch (e: Exception) {
                        imgService.visibility = View.GONE
                        notImageT.visibility = View.VISIBLE
                    }
                }

                setupCreateRequest(userId, it.fournisseurId, it.id ?: "")
                checkPermissions(viewModelUser.userById.value, it)
            }
        }

        viewModelService.deleteSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Service deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun checkPermissions(user: User?, service: Service?) {
        if (user == null || service == null) return

        when (user) {
            is User.AdminUser -> {
                toolbar.title = "Admin View"
                viewAdminFournisseur.visibility = View.VISIBLE
                setupAdminFournisseurActions(service)
            }
            is User.FournisseurUser -> {
                toolbar.title = "Service Details"
                if (user.getId() == service.fournisseurId) {
                    viewAdminFournisseur.visibility = View.VISIBLE
                    setupAdminFournisseurActions(service)
                } else {
                    viewAdminFournisseur.visibility = View.GONE
                }
            }
            is User.ClientUser -> {
                toolbar.title = "Order Service"
                viewCreatSR.visibility = View.VISIBLE
                viewAdminFournisseur.visibility = View.GONE
            }
        }
    }

    private fun setupAdminFournisseurActions(service: Service) {
        btnUpdate.setOnClickListener {
            val intent = Intent(this, CreateServicePage::class.java)
            intent.putExtra("idService", service.id)
            intent.putExtra("isUpdate", true)
            intent.putExtra("idFournis", service.fournisseurId)
            intent.putExtra("nameF", service.nameFournisseur)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Service")
                .setMessage("Are you sure you want to delete this service?")
                .setPositiveButton("Delete") { _, _ ->
                    service.id?.let { viewModelService.deleteService(it) }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupCreateRequest(idc: String, idF: String, idS: String) {
        val btnCreateServiceR = findViewById<Button>(R.id.btnCreateServiceR)
        val edtDescriptionSR = findViewById<EditText>(R.id.edtDescriptionSR)
        errorSR = findViewById(R.id.errorSR)

        btnCreateServiceR.setOnClickListener {
            if (idc.isNotEmpty() && idc != idF) {
                val description = edtDescriptionSR.text.toString().trim()
                if (description.isEmpty()) {
                    errorSR.text = "Please enter a description"
                    errorSR.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                val serviceR = ServiceRequest(
                    clientId = idc,
                    fournisseurId = idF,
                    serviceId = idS,
                    decreption = description,
                    status = StatusSR.PENDING
                )
                viewModelServiceRequest.createServiceRequest(serviceR)
                viewModelServiceRequest.createServiceRequest.observe(this) { request ->
                    request?.let {
                        Toast.makeText(this, "Order sent successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "You cannot order your own service", Toast.LENGTH_SHORT).show()
            }
        }
    }
}