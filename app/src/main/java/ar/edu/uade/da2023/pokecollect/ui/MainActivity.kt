package ar.edu.uade.da2023.pokecollect.ui

import Pokemon
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.da2023.pokecollect.R
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var exitButton: Button
    private lateinit var pokemonButton: Button
    private lateinit var bagButton: Button

    private lateinit var searchView: SearchView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var originalPokemonNames: List<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        setSupportActionBar(findViewById(R.id.toolbar))
        bindViewModel()

        sharedPreferences = getSharedPreferences("pokemon_cache", Context.MODE_PRIVATE)

        searchView = findViewById(R.id.searchMainVw)

        //Configuracion del boton exit, al apretarlo cierra la aplicacion completamente
        exitButton = findViewById(R.id.ExitBtn)
        exitButton.setOnClickListener { exitApplication() }

        //Configuracion del boton pokemon, al apretarlo abre esa actividad
        pokemonButton = findViewById(R.id.pokemonBtn)
        pokemonButton.setOnClickListener{
            val intent = Intent(this, PokemonActivity::class.java)
            startActivity(intent)
        }

        //Configuracion del boton bag, al apretarlo abre esa actividad
        bagButton = findViewById(R.id.bagBtn)
        bagButton.setOnClickListener{
            val intent = Intent(this, BagActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }


    private fun bindViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("pokemon_cache", Context.MODE_PRIVATE)

        // Obtener la lista de pokemons almacenada en caché
        val cachedPokemonList = sharedPreferences.getString("pokemon_list", null)
        if (!cachedPokemonList.isNullOrEmpty()) {
            val cachedPokemon = Gson().fromJson<Pokemon>(cachedPokemonList, Pokemon::class.java)
            updatePokemonList(cachedPokemon)
        } else {
            viewModel.onStart()
        }

        viewModel.pokemon.observe(this) { pokemon ->
            updatePokemonList(pokemon)

            // Guardar la lista de pokemons en caché
            val jsonPokemon = Gson().toJson(pokemon)
            sharedPreferences.edit().putString("pokemon_list", jsonPokemon).apply()
        }
    }

    private fun updatePokemonList(pokemon: Pokemon) {
        originalPokemonNames = pokemon.results.map { it.name }
        val filteredPokemonNames = originalPokemonNames.toList()

        val listView = findViewById<ListView>(R.id.listViewPokedex)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredPokemonNames.map { it.replaceFirstChar { it.uppercaseChar() } })
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedPokemon = filteredPokemonNames[position]
            val intent = Intent(this@MainActivity, PokemonActivity::class.java)
            intent.putExtra("pokemonName", selectedPokemon)
            startActivity(intent)
        }

        val searchView = findViewById<SearchView>(R.id.searchMainVw)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredNames = originalPokemonNames.filter { it.contains(newText, true) }
                    .map { it.replaceFirstChar { it.uppercaseChar() } }
                adapter.clear()
                adapter.addAll(filteredNames)
                adapter.notifyDataSetChanged()
                return true
            }
        })
    }




    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            // Usuario no logueado
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            val db = FirebaseFirestore.getInstance()
            val userCollection = db.collection("users")
            val userDocument = userCollection.document(firebaseUser.uid)

            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    // El documento del usuario no existe, se debe crear con los datos iniciales
                    val userData = hashMapOf(
                        "email" to firebaseUser.email,
                        "favorites" to emptyList<Map<String, Any>>() // Lista vacía de favoritos
                    )

                    userDocument.set(userData)
                        .addOnSuccessListener {
                            // El usuario se guardó correctamente en Firestore
                            // Continúa con la lógica de tu aplicación después de guardar el usuario
                        }
                        .addOnFailureListener { exception ->
                            // Ocurrió un error al guardar el usuario en Firestore
                            // Maneja el error según tus necesidades
                        }
                }
            }
        }
    }



    private fun exitApplication() {
        // Cerrar sesión en Firebase Authentication
        FirebaseAuth.getInstance().signOut()


        // Cerrar sesión en Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            // Cerrar completamente la aplicación
            finishAffinity()
        }
    }

}