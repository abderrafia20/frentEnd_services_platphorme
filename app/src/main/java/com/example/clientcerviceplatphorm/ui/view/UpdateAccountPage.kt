package com.example.clientcerviceplatphorm.ui.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class UpdateAccountPage : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnUpdate: Button

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()
    private var userId: String = ""
    private val servicesF: MutableList<Service> = mutableListOf()
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account_page)

        etName = findViewById(R.id.etNameUpdate)
        etEmail = findViewById(R.id.etEmailUpdate)
        etPhone = findViewById(R.id.etPhoneUpdate)
        btnUpdate = findViewById(R.id.btnUpdateAccount)

        userId = intent.getStringExtra("id") ?: ""

        if (userId.isNotEmpty()) {
            viewModelUser.getUserById(userId)
        }

        viewModelUser.userById.observe(this) { user ->
            user?.let {
                currentUser = it
                etName.setText(it.getName())
                etEmail.setText(it.getEmail())
                etPhone.setText(it.getPhone())
                
                if (it is User.FournisseurUser) {
                    viewModelService.getServices()
                }
            }
        }

        viewModelService.services.observe(this) { services ->
            if (services != null && userId.isNotEmpty()) {
                val filtered = services.filter { it.fournisseurId == userId }
                servicesF.clear()
                servicesF.addAll(filtered)
            }
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                currentUser?.let { user ->
                    val updatedUser = when (user) {
                        is User.AdminUser -> User.AdminUser(user.admin.copy(name = name, email = email, phone = phone))
                        is User.ClientUser -> User.ClientUser(user.client.copy(name = name, email = email, phone = phone))
                        is User.FournisseurUser -> User.FournisseurUser(user.fournisseur.copy(name = name, email = email, phone = phone))
                    }



                    if (user is User.FournisseurUser) {
                        servicesF.forEach { s ->
                            s.nameFournisseur = name
                            viewModelService.updateService(s.id.toString(), s)
                        }
                    }

                    viewModelUser.updateUser(userId, updatedUser)
                    Toast.makeText(this, "Account updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}