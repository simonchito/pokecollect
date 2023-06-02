package ar.edu.uade.da2023.pokecollect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.da2023.pokecollect.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViewModel()

    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
    private fun bindViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.pokemons.observe(this) {
            //actualizar lista de la pantalla
        }




    }

}