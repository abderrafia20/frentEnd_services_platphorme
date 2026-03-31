package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelService
import com.example.clientcerviceplatphorm.ui.viewmodel.ViewModelUser
import com.google.android.material.navigation.NavigationView
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream

class CreateServicePage : BaseActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var etTitle: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var error: TextView
    private lateinit var imgService: ImageButton
    private lateinit var btnAction: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var txnameHeader: TextView
    private lateinit var txemailHeader: TextView

    private val viewModel: ViewModelService by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()

    private var fournisseurId: String = ""
    private var fournisseurName: String = ""
    private var serviceId: String? = null
    private var isUpdate: Boolean = false
    private var encodedImage: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imgService.setImageURI(it)
            encodedImage = encodeImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_service_page)

        fournisseurId = intent.getStringExtra("idFournis") ?: ""
        fournisseurName = intent.getStringExtra("nameF") ?: ""
        serviceId = intent.getStringExtra("idService")
        isUpdate = intent.getBooleanExtra("isUpdate", false)

        initViews()
        
        if (isUpdate && serviceId != null) {
            btnAction.text = "Update Service"
            viewModel.getServiceById(serviceId!!)
        }

        findViewById<TextView>(R.id.nameFournir).text = fournisseurName

        setupDrawer()
        setupListeners()
        observeData()
    }

    private fun initViews() {
        etTitle = findViewById(R.id.edtTitle)
        etPrice = findViewById(R.id.edtPrice)
        etDescription = findViewById(R.id.edtDescription)
        error = findViewById(R.id.error)
        imgService = findViewById(R.id.imgService)
        btnAction = findViewById(R.id.btnCreateService)
        drawerLayout = findViewById(R.id.drawerLayout)
    }

    private fun setupListeners() {
        imgService.setOnClickListener { pickImage.launch("image/*") }
        
        btnAction.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val desc = etDescription.text.toString().trim()

            if (title.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
                error.text = "Please fill all fields"
                error.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull() ?: 0.0
            val service = Service(
                id = serviceId,
                nameFournisseur = fournisseurName,
                fournisseurId = fournisseurId,
                title = title,
                description = desc,
                price = price,
                image = encodedImage
            )

            if (isUpdate && serviceId != null) {
                viewModel.updateService(serviceId!!, service)
                Toast.makeText(this, "Service Updating...", Toast.LENGTH_SHORT).show()
                finish() // Or observe update success
            } else {
                viewModel.createService(service)
            }
        }
    }

    private fun encodeImage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val bytes = outputStream.toByteArray()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun observeData() {
        viewModel.createService.observe(this) {
            Toast.makeText(this, "Service Created Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.SrviceById.observe(this) { service ->
            service?.let {
                etTitle.setText(it.title)
                etPrice.setText(it.price.toString())
                etDescription.setText(it.description)
                encodedImage = it.image
                if (!it.image.isNullOrEmpty()) {
                    val imageBytes = Base64.decode(it.image, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imgService.setImageBitmap(bitmap)
                }
            }
        }

        viewModelUser.getUserById(fournisseurId)
        viewModelUser.userById.observe(this) { user ->
            user?.let {
                txnameHeader.text = it.getName()
                txemailHeader.text = it.getEmail()
                setupBottomNavigation(it)
            }
        }
    }

    private fun setupDrawer() {
        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        val header = findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
        txnameHeader = header.findViewById(R.id.txtName)
        txemailHeader = header.findViewById(R.id.txtEmail)
        
        header.findViewById<View>(R.id.txtLogout).setOnClickListener {
            getSharedPreferences("USER_PREF", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, LoginPage::class.java))
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}
}