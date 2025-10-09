package com.example.salud_y_vida

import android.os.Bundle
import android.os.Message
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    //Importando
    private lateinit var  drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

    //Declarando
    drawerLayout = findViewById(R.id.main)
    navigationView = findViewById(R.id.navegacion)
    toolbar = findViewById(R.id.toolbar)

    //Configurando toolbar
    setSupportActionBar(toolbar)

    //Mostrar hamburguesa icon
    val hamburguesa = ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    )
        drawerLayout.addDrawerListener(hamburguesa)
        hamburguesa.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> showToast("Inicio")
                R.id.nav_citas -> showToast("Mis Citas")
                R.id.nav_perfil -> showToast("Perfil")
            }
            drawerLayout.closeDrawers()
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //showToast
    private fun showToast (message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}