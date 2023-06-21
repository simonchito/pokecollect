package ar.edu.uade.da2023.pokecollect.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import ar.edu.uade.da2023.pokecollect.R
import ar.edu.uade.da2023.pokecollect.data.PokemonsDataSource
import ar.edu.uade.da2023.pokecollect.data.PokemonsRepository
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.launch

class PokemonActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private val _TAG = "API-POKE"
    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("pokecollect")
    private val scope = CoroutineScope(coroutineContext)

    //dependencias
    private val pokemonsRepository = PokemonsRepository()


    //propiedades

    var pokemonInfo = MutableLiveData<PokemonInfo>()

    private lateinit var sharedPrefs: SharedPreferences
    private var lastPokemonName: String? = null


    //botones
    private lateinit var pokedexButton: Button
    private lateinit var bagButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)
        setSupportActionBar(findViewById(R.id.toolbar))


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        lastPokemonName = sharedPrefs.getString("lastPokemonName", null)
        val pokemonName = intent.getStringExtra("pokemonName")
        Log.d(_TAG,"pokemonInfo value: ${pokemonName}")

        val actualPokemonName = pokemonName ?: lastPokemonName


        // Verifico si se recibió el nombre del Pokémon correctamente
        if (actualPokemonName != null) {
            // Llamar a la función getPokemon() de tu repositorio
            scope.launch {
                kotlin.runCatching {
                    pokemonsRepository.getPokemon(actualPokemonName)
                }.onSuccess {
                    Log.d(_TAG, "PokemonInfo onSuccess")
                    pokemonInfo.postValue(it)
                    // Guardar el nombre del último Pokémon buscado en SharedPreferences
                    sharedPrefs.edit().putString("lastPokemonName", actualPokemonName).apply()
                }.onFailure {
                    Log.e(_TAG, "PokemonInfo Error", it)
                }
            }
        }
        Log.d(_TAG, "pokemonValue: ${pokemonInfo.value}")

        // Verifico si se recibió el nombre del Pokémon correctamente
        if (pokemonName != null) {
            // Llamar a la función getPokemon() de tu repositorio
            scope.launch {
                kotlin.runCatching {
                    pokemonsRepository.getPokemon(pokemonName)
                }.onSuccess {
                    Log.d(_TAG, "PokemonInfo onSuccess")
                    pokemonInfo.postValue(it)

                }.onFailure {
                    Log.e(_TAG, "PokemonInfo Error", it)
                }
            }
        }
        Log.d(_TAG, "pokemonValue: ${pokemonInfo.value}")

        pokemonInfo.observe(this) { pokemon ->
            // Acciones a realizar cuando se actualiza el valor de pokemonInfo
            Log.d(_TAG, "PokemonInfo actualizado: $pokemon")

            val pokemonImg = findViewById<ImageView>(R.id.pokemonImg)
            val namepokemonTxv = findViewById<TextView>(R.id.namepokemonTxv)
            val typeTxv = findViewById<TextView>(R.id.typeTxv)
            val skillTxv = findViewById<TextView>(R.id.skillTxv)
            val figureTxv = findViewById<TextView>(R.id.figureTxv)


            Glide.with(this)
                .load(pokemon.sprites.front_default) // Reemplaza `front_default` con la propiedad adecuada de PokemonInfo que contiene la URL de la imagen
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: establece la estrategia de almacenamiento en caché
                .into(pokemonImg)

            // Asignar los nombres y tipos
            namepokemonTxv.text = pokemon.name
            typeTxv.text = pokemon.types.joinToString(", ") { it.name }

            // Asignar las habilidades
            skillTxv.text = pokemon.abilities.joinToString(", ") { it.ability.name }

            // Asignar la figura
            figureTxv.text = pokemon.order.toString()
        }
        //Toolbar function buttons

        pokedexButton = findViewById(R.id.pokedexBtn)
        pokedexButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Configuracion del boton bag, al apretarlo abre esa actividad
        bagButton = findViewById(R.id.bagBtn)
        bagButton.setOnClickListener{
            val intent = Intent(this, BagActivity::class.java)
            startActivity(intent)
        }
        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }


    }

}