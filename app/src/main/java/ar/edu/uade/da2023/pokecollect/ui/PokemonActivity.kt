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
import ar.edu.uade.da2023.pokecollect.data.PokemonsRepository
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Locale

class PokemonActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private val _TAG = "API-POKE"
    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("pokecollect")
    private val scope = CoroutineScope(coroutineContext)

    private val favoriteList = ArrayList<PokemonInfo>()
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    //dependencias
    private val pokemonsRepository = PokemonsRepository()


    //propiedades

    var pokemonInfo = MutableLiveData<PokemonInfo>()

    private lateinit var sharedPrefs: SharedPreferences
    private var lastPokemonName: String? = null


    //botones
    private lateinit var pokedexButton: Button
    private lateinit var bagButton: Button

    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)
        setSupportActionBar(findViewById(R.id.toolbar))


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        lastPokemonName = sharedPrefs.getString("lastPokemonName", null)
        val pokemonName = intent.getStringExtra("pokemonName")
        Log.d(_TAG,"pokemonInfo value: ${pokemonName}")

        val actualPokemonName = pokemonName ?: lastPokemonName
        firebaseAuth = FirebaseAuth.getInstance()


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
                .load(pokemon.sprites.front_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //  establece la estrategia de almacenamiento en caché
                .into(pokemonImg)



            namepokemonTxv.text = HtmlCompat.fromHtml("<b>Name:</b> ${pokemon.name?.capitalize()}", HtmlCompat.FROM_HTML_MODE_COMPACT)
            typeTxv.text = HtmlCompat.fromHtml("<b>Type:</b> ${
                if (pokemon.types.isNotEmpty()) {
                    pokemon.types.joinToString(", ") { it.type?.name?.capitalize() ?: "" }
                } else {
                    "None"
                }
            }", HtmlCompat.FROM_HTML_MODE_COMPACT)

            // Asignar las habilidades
            skillTxv.text = HtmlCompat.fromHtml("<b>Skill:</b> ${pokemon.abilities.joinToString(", ") { it.ability.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            } ?: "" }}", HtmlCompat.FROM_HTML_MODE_COMPACT)

            figureTxv.text = HtmlCompat.fromHtml("<b>Figure:</b> ${pokemon.order}", HtmlCompat.FROM_HTML_MODE_COMPACT)
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
        val favoritoBtn = findViewById<Button>(R.id.favoritoBtn)
        // Inicializar el estado de favorito del Pokémon
        var isPokemonFavorite = false

        favoritoBtn.setOnClickListener {
            if (!isPokemonFavorite) {
                // Agregar el pokemonInfo a la lista de favoritos
                addToFavorites(pokemonInfo)
                // Actualizar la imagen del botón
                favoritoBtn.setBackgroundResource(android.R.drawable.btn_star_big_on)
                // Actualizar el estado del botón de favoritos
                isPokemonFavorite = true
            } else {
                // Eliminar el pokemonInfo de la lista de favoritos
                removeFromFavorites(pokemonInfo)
                // Actualizar la imagen del botón
                favoritoBtn.setBackgroundResource(android.R.drawable.btn_star_big_off)
                // Actualizar el estado del botón de favoritos
                isPokemonFavorite = false
            }
        }


    }

    private fun saveFavoritesToFirestore(favoriteList: List<PokemonInfo>) {
        // Obtener el ID del usuario actualmente autenticado
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Crear un documento para el usuario en la colección "users"
            val userDocument = userCollection.document(userId)

            // Convertir la lista de favoritos en un mapa
            val favoritesData = favoriteList.map { pokemon ->
                mapOf(
                    "id" to pokemon.id,
                    "name" to pokemon.name,
                    "order" to pokemon.order,
                    "abilities" to pokemon.abilities,
                    "types" to pokemon.types,
                    "sprites" to pokemon.sprites
                )
            }

            // Guardar la lista de favoritos en el documento del usuario en Firestore
            userDocument.update("favorites", favoritesData)
                .addOnSuccessListener {
                    // La lista de favoritos se guardó correctamente en Firestore
                }
                .addOnFailureListener { exception ->
                    // Ocurrió un error al guardar la lista de favoritos en Firestore
                }
        }
    }
    private fun addToFavorites(pokemonInfo: MutableLiveData<PokemonInfo>) {
        // Agregar el pokémon a la lista de favoritos local
        val pokemon = pokemonInfo.value
        if (pokemon != null) {
            favoriteList.add(pokemon)
        }

        // Guardar la lista de favoritos actualizada en Cloud Firestore
        saveFavoritesToFirestore(favoriteList)
    }

    private fun removeFromFavorites(pokemonInfo: MutableLiveData<PokemonInfo>) {
        // Eliminar el pokémon de la lista de favoritos local
        val pokemon = pokemonInfo.value
        if (pokemon != null) {
            favoriteList.remove(pokemon)
        }

        // Guardar la lista de favoritos actualizada en Cloud Firestore
        saveFavoritesToFirestore(favoriteList)
    }
}

