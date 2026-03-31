package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser

class LoginPage : AppCompatActivity() {

    private val viewModel: ViewModelUser by viewModels()

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtSignup: TextView
    private lateinit var txtError: TextView
    private lateinit var showPasswordCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        initViews()
        setupObservers()
        setupPasswordToggle()
        viewModel.getUsers()


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isEmpty() || password.isEmpty()) {
                showError("Please fill all fields")
                return@setOnClickListener
            }
            
            txtError.visibility = View.GONE
            viewModel.login(email, password)
        }

        txtSignup.setOnClickListener {
            startActivity(Intent(this, SignupPage::class.java))
            finish()
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtSignup = findViewById(R.id.txtSignup)
        txtError = findViewById(R.id.txterror)
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox)
    }

    private fun setupPasswordToggle() {
        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.transformationMethod = null
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { user ->
            user?.let {
                Toast.makeText(this, "Login success: ${it.getName()}", Toast.LENGTH_SHORT).show()

                val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("userId", it.getId())
                editor.apply()

                val intent = Intent(this, HomePageServises::class.java)
                intent.putExtra("id", it.getId())
                startActivity(intent)
                finish()
            } ?: run {
                showError("Email or password incorrect")
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let { showError(it) }
        }
    }

    private fun showError(message: String) {
        txtError.text = message
        txtError.visibility = View.VISIBLE
    }
}