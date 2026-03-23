package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class ProfilePage : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var btnSignOut: Button
    private lateinit var btnDeleteAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        txtName = findViewById(R.id.etNameP)
        txtEmail = findViewById(R.id.etEmailP)
        txtPhone = findViewById(R.id.etPhoneP)
        btnSignOut = findViewById(R.id.btnSignOut)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)

        val idUser = intent.getStringExtra("id") ?: ""
        if (idUser.isNotEmpty()) {
            viewModelUser.getUserById(idUser)
        }


        viewModelUser.userById.observe(this) { user ->
            user?.let {
                txtName.text = it.getName()
                txtEmail.text = it.getEmail()
                txtPhone.text = it.getPhone()
                setupBottomNavigation(it)
            }
        }


        viewModelUser.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }


        btnSignOut.setOnClickListener {
            getSharedPreferences("USER_PREF", MODE_PRIVATE).edit().clear().apply()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }


        btnDeleteAccount.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete your account? This action cannot be undone!")
            builder.setPositiveButton("Yes") { dialog, _ ->
                if (idUser.isNotEmpty()) {
                    viewModelUser.deleteUser(idUser)


                    viewModelUser.userById.observe(this) { userAfterDelete ->
                        if (userAfterDelete == null) {
                            getSharedPreferences("USER_PREF", MODE_PRIVATE).edit().clear().apply()
                            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, SignupPage::class.java))
                            finish()
                        }
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }
}