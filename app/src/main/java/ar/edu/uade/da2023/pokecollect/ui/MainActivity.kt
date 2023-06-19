package ar.edu.uade.da2023.pokecollect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.da2023.pokecollect.R
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var exitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        setSupportActionBar(findViewById(R.id.toolbar))
        bindViewModel()

        exitButton = findViewById(R.id.ExitBtn)
        exitButton.setOnClickListener { exitApplication() }

    }


    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }


    private fun bindViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.pokemon.observe(this) { pokemon ->
            val pokemonNames = pokemon.results.map { it.name } // Obtener los nombres de los Pokémon
            // Actualizar la lista de la pantalla utilizando pokemonNames
            val listView = findViewById<ListView>(R.id.listViewPokedex)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pokemonNames)
            listView.adapter = adapter
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // Usuario no logueado
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun exitApplication() {
        // cerrar completamente la aplicación, puedes usar finishAffinity()
        finishAffinity()
    }

}