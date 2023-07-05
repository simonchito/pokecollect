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
import ar.edu.uade.da2023.pokecollect.data.local.toLocal
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Locale
import android.R.drawable.btn_star_big_on
import android.R.drawable.btn_star_big_off

class PokemonActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private val _TAG = "API-POKE"

    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("pokecollect")
    private val scope = CoroutineScope(coroutineContext)

    private val favoriteList = ArrayList<PokemonInfo>()
    private var firestore = FirebaseFirestore.getInstance()
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
        val pokemonName = intent.getStringExtra("pokemonName")?.toLowerCase()
        Log.d(_TAG, "pokemonInfo value: ${pokemonName}")

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
                    updateFavoriteButtonState()
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



            namepokemonTxv.text = HtmlCompat.fromHtml(
                "<b>Name:</b> ${pokemon.name?.capitalize()}",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            typeTxv.text = HtmlCompat.fromHtml("<b>Type:</b> ${
                if (pokemon.types.isNotEmpty()) {
                    pokemon.types.joinToString(", ") { it.type?.name?.capitalize() ?: "" }
                } else {
                    "None"
                }
            }", HtmlCompat.FROM_HTML_MODE_COMPACT)

            // Asignar las habilidades
            skillTxv.text = HtmlCompat.fromHtml(
                "<b>Skill:</b> ${
                    pokemon.abilities.joinToString(", ") {
                        it.ability.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        } ?: ""
                    }
                }", HtmlCompat.FROM_HTML_MODE_COMPACT
            )

            figureTxv.text = HtmlCompat.fromHtml(
                "<b>Figure:</b> ${pokemon.order}",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
        //Toolbar function buttons

        pokedexButton = findViewById(R.id.pokedexBtn)
        pokedexButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Configuracion del boton bag, al apretarlo abre esa actividad
        bagButton = findViewById(R.id.bagBtn)
        bagButton.setOnClickListener {
            val intent = Intent(this, BagActivity::class.java)
            startActivity(intent)
        }
        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }



        val favoritoBtn = findViewById<Button>(R.id.favoritoBtn)

        // Inicializar el estado de favorito del Pokémon
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        favoritoBtn.setOnClickListener {
            val pokemon = pokemonInfo.value
            if (pokemon != null) {
                val pokemonValue = pokemon.name ?: ""

                val user = firebaseAuth.currentUser
                if (user != null) {
                    val userId = user.uid
                    val userDocument = firestore.collection("users").document(userId)

                    // Consulta si el pokemon está en la tabla de favoritos del usuario
                    userDocument.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            val favorites =
                                document.data?.get("favorites") as? List<Map<String, Any>>
                            if (favorites != null) {
                                val isPokemonFavorite = favorites.any { it["name"] == pokemonValue }
                                if (isPokemonFavorite) {
                                    // El pokemon ya está en la tabla de favoritos, no es necesario agregarlo
                                    removeFromFavorites(userDocument, pokemon)
                                    favoritoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(android.R.drawable.btn_star_big_off))
                                } else {
                                    // El pokemon no está en la tabla de favoritos, agregarlo
                                    addToFavorites(userDocument, pokemon)
                                    favoritoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(android.R.drawable.btn_star_big_on))
                                }
                            } else {
                                // La lista de favoritos está vacía, agregar el primer pokemon
                                addToFavorites(userDocument, pokemon)
                                favoritoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(android.R.drawable.btn_star_big_on))
                            }
                        }
                    }
                }
            }
        }



    }

    override fun onResume() {
        super.onResume()
        updateFavoriteButtonState()
    }


    private fun updateFavoriteButtonState() {
        val favoritoBtn = findViewById<Button>(R.id.favoritoBtn)
        val pokemon = pokemonInfo.value
        if (pokemon != null) {
            val pokemonValue = pokemon.name ?: ""

            val user = firebaseAuth.currentUser
            if (user != null) {
                val userId = user.uid
                val userDocument = firestore.collection("users").document(userId)

                userDocument.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val favorites = document.data?.get("favorites") as? List<Map<String, Any>>
                        if (favorites != null) {
                            val isPokemonFavorite = favorites.any { it["name"] == pokemonValue }
                            if (isPokemonFavorite) {
                                favoritoBtn.setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    null,
                                    resources.getDrawable(android.R.drawable.btn_star_big_on)
                                )
                            } else {
                                favoritoBtn.setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    null,
                                    resources.getDrawable(android.R.drawable.btn_star_big_off)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun addToFavorites(userDocument: DocumentReference, pokemon: PokemonInfo) {
        val favoriteMap = pokemon.toLocal()
        userDocument.update("favorites", FieldValue.arrayUnion(favoriteMap))
    }

    private fun removeFromFavorites(userDocument: DocumentReference, pokemon: PokemonInfo) {
        val favoriteMap = pokemon.toLocal()
        userDocument.update("favorites", FieldValue.arrayRemove(favoriteMap))
    }
}
