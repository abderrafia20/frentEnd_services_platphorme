package com.example.clientcerviceplatphorm.ui.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.model.Client
import com.example.clientcerviceplatphorm.model.Fournisseur
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class UpdateAccountPage : BaseActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnUpdate: Button

    private val viewModelUser: ViewModelUser by viewModels()
    private var userId: String = ""
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
            }
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                currentUser?.let { user ->
                    val updatedUser = when (user) {
                        is User.AdminUser -> User.AdminUser(user.admin.copy(name = name, email = email, phone = phone))
                        is User.ClientUser -> User.ClientUser(user.client.copy(name = name, email = email, phone = phone))
                        is User.FournisseurUser -> User.FournisseurUser(user.fournisseur.copy(name = name, email = email, phone = phone))
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