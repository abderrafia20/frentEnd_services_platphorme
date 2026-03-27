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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User
import com.example.clientcerviceplatphorm.model.adapter.AdapterService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class ProfilePage : BaseActivity() {

    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelService: ViewModelService by viewModels()

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var btnSignOut: Button
    private lateinit var btnDeleteAccount: Button
    private lateinit var recyclerV: RecyclerView

    private var idF = ""
    private var idUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_page)


        txtName = findViewById(R.id.etNameP)
        txtEmail = findViewById(R.id.etEmailP)
        txtPhone = findViewById(R.id.etPhoneP)
        btnSignOut = findViewById(R.id.btnSignOut)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)
        recyclerV = findViewById(R.id.recyclerP)
        recyclerV.layoutManager = LinearLayoutManager(this)

        idUser = intent.getStringExtra("id") ?: ""

        if (idUser.isNotEmpty()) {
            viewModelUser.getUserById(idUser)
        }

        viewModelUser.userById.observe(this) { user ->
            user?.let {
                txtName.text = it.getName()
                txtEmail.text = it.getEmail()
                txtPhone.text = it.getPhone()

                if (it is User.FournisseurUser) {
                    idF = it.getId()
                    viewModelService.getServices()
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
                    intent.putExtra("idUser", idF)
                    startActivity(intent)
                }
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
                    getSharedPreferences("USER_PREF", MODE_PRIVATE).edit().clear().apply()
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignupPage::class.java))
                    finish()
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