package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User

open class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        val baseView = layoutInflater.inflate(R.layout.activity_basse, null)
        val container = baseView.findViewById<FrameLayout>(R.id.container)

        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseView)
    }

    fun setupBottomNavigation(user: User) {

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnInter = findViewById<ImageButton>(R.id.btnInterventions)
        val btnProfile = findViewById<ImageButton>(R.id.btnprofile)


        setupUserPermission(user)

        btnHome.setOnClickListener {
            if (this !is HomePageServises) {
                val intent = Intent(this, HomePageServises::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }

        btnInter.setOnClickListener {
            if (this !is InterventionsActivity) {
                val intent = Intent(this, InterventionsActivity::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }

        btnProfile.setOnClickListener {
            if (this !is ProfilePage) {
                val intent = Intent(this, ProfilePage::class.java)
                intent.putExtra("id", user.getId())
                startActivity(intent)
            }
        }
    }

    private fun setupUserPermission(user: User) {

        val navAdd = findViewById<LinearLayout>(R.id.navAdd)
        val btnAdd = findViewById<ImageButton>(R.id.btnAjour)

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