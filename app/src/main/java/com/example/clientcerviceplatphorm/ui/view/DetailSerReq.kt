package com.example.clientcerviceplatphorm.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.model.StatusSR
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelServiceRequest
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class DetailSerReq : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnAccepte: Button
    private lateinit var btnComplete: Button

    private var serviceRN: ServiceRequest? = null

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelSerReq: ViewModelServiceRequest by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datail_ser_req)

        tvTitle = findViewById(R.id.tvTitle)
        tvName = findViewById(R.id.tvName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        tvDate = findViewById(R.id.tvDate)
        tvStatus = findViewById(R.id.tvStatus)
        btnComplete = findViewById(R.id.complete)
        btnAccepte = findViewById(R.id.accepte)

        val title = intent.getStringExtra("Title")
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val descr = intent.getStringExtra("descrSR")
        val date = intent.getSerializableExtra("date")
        val status = intent.getStringExtra("status")
        val userid = intent.getStringExtra("idUser") ?: ""
        val idSR = intent.getStringExtra("idSR") ?: ""

        tvTitle.text = title
        tvName.text = "Name: $name"
        tvPrice.text = "Price: $price"
        tvDescription.text = "Description: $descr"
        tvDate.text = "Date: $date"
        tvStatus.text = "Status: $status"


        btnAccepte.visibility = View.GONE
        btnComplete.visibility = View.GONE

        viewModelSerReq.getServiceRequestById(idSR)
        viewModelSerReq.serviceRequestById.observe(this) { serviceR ->
            serviceRN = serviceR
        }

        viewModelUser.getUserById(userid)
        viewModelUser.userById.observe(this) { user ->
            user?.let {
                if (user is User.FournisseurUser) {

                    btnAccepte.visibility = View.VISIBLE
                    btnComplete.visibility = View.VISIBLE

                    btnAccepte.setOnClickListener {
                        serviceRN?.let {
                            it.status = StatusSR.ACCEPTED
                            viewModelSerReq.updateServiceRequest(idSR, it)
                        }
                    }

                    btnComplete.setOnClickListener {
                        serviceRN?.let {
                            it.status = StatusSR.COMPLETED
                            viewModelSerReq.updateServiceRequest(idSR, it)
                        }
                    }
                }
            }
        }

        viewModelSerReq.updateServiceRequest.observe(this) { updated ->
            updated?.let {
                tvStatus.text = "Status: ${it.status}"
            }
        }
    }
}