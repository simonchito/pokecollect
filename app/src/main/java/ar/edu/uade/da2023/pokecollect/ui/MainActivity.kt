package ar.edu.uade.da2023.pokecollect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.da2023.pokecollect.R
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        bindViewModel()

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

}