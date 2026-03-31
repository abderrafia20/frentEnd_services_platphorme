package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        val baseView = layoutInflater.inflate(R.layout.activity_basse, null)
        val container = baseView.findViewById<FrameLayout>(R.id.container)

        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseView)
    }

    fun setupBottomNavigation(user: User) {

        val btnHome = findViewById<ImageView>(R.id.btnHome)
        val btnInter = findViewById<ImageView>(R.id.btnInterventions)
        val btnProfile = findViewById<ImageView>(R.id.btnprofile)

        // Find containers for better click area
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navInter = findViewById<LinearLayout>(R.id.navInterventions)
        val navProfile = findViewById<LinearLayout>(R.id.navprofile)

        setupUserPermission(user)

        val homeClickListener = View.OnClickListener {
            if (this !is HomePageServises) {
                val intent = Intent(this, HomePageServises::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }
        navHome.setOnClickListener(homeClickListener)
        btnHome.setOnClickListener(homeClickListener)

        val interClickListener = View.OnClickListener {
            if (this !is InterventionsActivity) {
                val intent = Intent(this, InterventionsActivity::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }
        navInter.setOnClickListener(interClickListener)
        btnInter.setOnClickListener(interClickListener)

        val profileClickListener = View.OnClickListener {
            if (this !is ProfilePage) {
                val intent = Intent(this, ProfilePage::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }
        navProfile.setOnClickListener(profileClickListener)
        btnProfile.setOnClickListener(profileClickListener)
        
        // Highlight active tab
        when(this) {
            is HomePageServises -> btnHome.setColorFilter(getColor(R.color.colorPrimary))
            is InterventionsActivity -> btnInter.setColorFilter(getColor(R.color.colorPrimary))
            is ProfilePage -> btnProfile.setColorFilter(getColor(R.color.colorPrimary))
        }
    }

    private fun setupUserPermission(user: User) {
        val navAdd = findViewById<LinearLayout>(R.id.navAdd)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAjour)

        when (user) {
            is User.AdminUser -> {
                navAdd.visibility = View.VISIBLE
                btnAdd.setOnClickListener {
                    startActivity(Intent(this, CreateServicePage::class.java))
                }
            }
            is User.FournisseurUser -> {
                navAdd.visibility = View.VISIBLE
                btnAdd.setOnClickListener {
                    val intent = Intent(this, CreateServicePage::class.java)
                    intent.putExtra("idFournis", user.getId())
                    intent.putExtra("nameF", user.getName())
                    startActivity(intent)
                }
            }
            is User.ClientUser -> {
                navAdd.visibility = View.GONE
            }
        }
    }
}