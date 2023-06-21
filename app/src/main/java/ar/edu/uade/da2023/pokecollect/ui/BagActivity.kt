package ar.edu.uade.da2023.pokecollect.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import ar.edu.uade.da2023.pokecollect.R

class BagActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var pokemonButton: Button
    private lateinit var pokedexButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)
        setSupportActionBar(findViewById(R.id.toolbar))


        var prefs= getSharedPreferences("ar.edu.uade.da2023.pokecollect.sharedpref", Context.MODE_PRIVATE)
        var user = prefs.getString("user", "")


        //Funcion de los botones
        pokemonButton = findViewById(R.id.pokemonBtn)
        pokemonButton.setOnClickListener{
            val intent = Intent(this, PokemonActivity::class.java)
            startActivity(intent)
        }

        pokedexButton = findViewById(R.id.pokedexBtn)
        pokedexButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }
    }
}