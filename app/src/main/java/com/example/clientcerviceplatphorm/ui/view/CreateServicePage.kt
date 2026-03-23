package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class CreateServicePage : BaseActivity() {

    private lateinit var nameFournir: TextView
    private lateinit var error: TextView
    private lateinit var etTitle: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAjout: Button

    private val viewModel: ViewModelService by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()

    private var fournisseurId: String = ""
    private var fournisseurName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_service_page)

        initViews()
        getIntentData()
        setupClick()
        observeCreateService()
        observeError()

        viewModelUser.getUserById(fournisseurId)
        viewModelUser.userById.observe(this) {user ->
            user?.let {
                setupBottomNavigation(it)
            }

        }

    }

    private fun initViews() {
        nameFournir = findViewById(R.id.nameFournir)
        error = findViewById(R.id.error)
        etTitle = findViewById(R.id.edtTitle)
        etPrice = findViewById(R.id.edtPrice)
        etDescription = findViewById(R.id.edtDescription)
        btnAjout = findViewById(R.id.btnCreateService)
        val navAdd = findViewById<LinearLayout>(R.id.navAdd)
        navAdd.visibility = View.VISIBLE
    }

    private fun getIntentData() {
        fournisseurId = intent.getStringExtra("idFournis") ?: ""
        fournisseurName = intent.getStringExtra("nameF") ?: ""

        nameFournir.text = fournisseurName
    }

    private fun setupClick() {
        btnAjout.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val price = etPrice.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty() || price.isEmpty() || description.isEmpty()) {
                error.text = "Fill in all the boxes"
                return@setOnClickListener
            }

            val service = Service(
                nameFournisseur = fournisseurName,
                fournisseurId = fournisseurId,
                title = title,
                description = description,
                price = price.toDouble()
            )

            viewModel.createService(service)
        }
    }

    private fun observeCreateService() {
        viewModel.createService.observe(this) { service ->
            service?.let {
                Toast.makeText(this, "Service created successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePageServises::class.java)
                intent.putExtra("id", it.fournisseurId)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun observeError() {
        viewModel.error.observe(this) { message ->
            message?.let {
                error.text = it
            }
        }
    }
}