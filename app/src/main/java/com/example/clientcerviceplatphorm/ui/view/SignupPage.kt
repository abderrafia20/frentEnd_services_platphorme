package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.*
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.example.clientcerviceplatphorm.utils.Validation

class SignupPage : AppCompatActivity() {

    private val viewModel: ViewModelUser by viewModels()
    private val valid = Validation()

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordC: EditText
    private lateinit var etphone: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var btnSignup: Button
    private lateinit var txtLogin: TextView
    private lateinit var txtError: TextView
    private lateinit var showPasswordCheckBox: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        initViews()
        setupSpinner()
        setupObservers()
        setupPasswordToggle()
        viewModel.getUsers() // fetch all users for validation

        btnSignup.setOnClickListener { handleSignup() }
        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordC = findViewById(R.id.etPasswordC)
        spinnerRole = findViewById(R.id.spinnerRole)
        btnSignup = findViewById(R.id.btnSignup)
        txtLogin = findViewById(R.id.txtlogin)
        txtError = findViewById(R.id.txterror)
        etphone = findViewById(R.id.etphone)
        showPasswordCheckBox = findViewById(R.id.showPassCheckBox)
    }

    private fun setupPasswordToggle() {

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.transformationMethod = null
                etPasswordC.transformationMethod = null
            } else {
                val method = PasswordTransformationMethod.getInstance()
                etPassword.transformationMethod = method
                etPasswordC.transformationMethod = method
            }
            etPassword.setSelection(etPassword.text.length)
            etPasswordC.setSelection(etPasswordC.text.length)
        }
    }

    private fun setupSpinner() {
        val roles = RoleUser.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.createdUser.observe(this) { user ->
            user?.let {
                Toast.makeText(this, "Signup success: ${it.getName()}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let { txtError.text = it }
        }
    }

    private fun handleSignup() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etPasswordC.text.toString().trim()
        val role = RoleUser.valueOf(spinnerRole.selectedItem.toString())
        val phone = etphone.text.toString().trim()

        txtError.text = ""

        if (name.isEmpty() || email.isEmpty()  || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            txtError.text = "Fill all fields"
            return
        }

        if (password != confirmPassword) {
            txtError.text = "Passwords do not match"
            return
        }

        if (valid.valideemail(email) == null || valid.validpass(password) == null) {
            txtError.text = "Invalid email or password"
            return
        }

        val user: User = when(role) {
            RoleUser.ADMIN -> User.AdminUser(Admin(name=name, email=email, phone=phone, password=password))
            RoleUser.CLIENT -> User.ClientUser(Client(name=name, email=email, phone=phone, password=password))
            RoleUser.FOURNISOUR -> User.FournisseurUser(Fournisseur(name=name, email=email, phone=phone, password=password))
        }

        viewModel.createUser(user)
    }
}