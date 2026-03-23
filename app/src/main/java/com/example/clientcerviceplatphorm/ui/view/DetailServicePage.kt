package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.model.StatusSR
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelServiceRequest
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import kotlin.getValue
class DetailServicePage : AppCompatActivity() {

    private val viewModelService: ViewModelService by viewModels()

    private val viewModelUser: ViewModelUser by viewModels()

    private val viewModelServiceRequest: ViewModelServiceRequest by viewModels ()

    private lateinit var txtHeaderD: TextView

    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtFournisseur: TextView

    private lateinit var descriptionSR: String

    private lateinit var errorSR: TextView
    private lateinit var viewCreatSR: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_service_page)

        txtHeaderD = findViewById(R.id.txtHeaderD)
        txtTitle = findViewById(R.id.txtTitleD)
        txtDescription = findViewById(R.id.txtDescriptionD)
        txtPrice = findViewById(R.id.txtPriceD)
        txtFournisseur = findViewById(R.id.txtFournisseurD)
        viewCreatSR = findViewById(R.id.VcreateSR)

        val serviceId = intent.getStringExtra("idService") ?: ""
        val userId = intent.getStringExtra("idUser") ?: ""


        if (serviceId.isNotEmpty()) {
            viewModelService.getServiceById(serviceId)
        }
        if (userId.isNotEmpty()) {
            viewModelUser.getUserById(userId)
        }


        viewModelUser.userById.observe(this) { user ->
            user?.let {

                when(it) {
                    is User.AdminUser -> txtHeaderD.text = "Admin view"
                    is User.FournisseurUser -> txtHeaderD.text = "Detail Service"
                    is User.ClientUser -> {
                        txtHeaderD.text = "Comender this service"
                        viewCreatSR.visibility = View.VISIBLE
                    }
                }
            }


        }
        viewModelService.SrviceById.observe(this) { service ->
            service?.let {

                txtTitle.text = it.title
                txtDescription.text = it.description
                txtPrice.text = "price : ${it.price} DH"
                txtFournisseur.text = "Made by : ${it.nameFournisseur}"
                createServiceRequest(userId, it.fournisseurId, serviceId)

            }
        }

    }
    private fun createServiceRequest(idc: String, idF: String, idS: String) {

        val btnCreateServiceR = findViewById<Button>(R.id.btnCreateServiceR)
        btnCreateServiceR.setOnClickListener {
            if (idc != "" && idc != idF ) {
                descriptionSR = findViewById<EditText>(R.id.edtDescriptionSR).text.toString().trim()
                errorSR = findViewById(R.id.errorSR)
                val serviceR = ServiceRequest(clientId = idc, fournisseurId = idF, serviceId = idS, decreption = descriptionSR, status = StatusSR.PENDING )
                viewModelServiceRequest.createServiceRequest(serviceR)
                viewModelServiceRequest.createServiceRequest.observe(this) {serviceRequest ->
                    serviceRequest?.let {
                        Toast.makeText(this, "Service Request created successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomePageServises::class.java)
                        intent.putExtra("id", idc)
                        startActivity(intent)
                        finish()

                    }
                }
            }
        }

    }
}