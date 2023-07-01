package ar.edu.uade.da2023.pokecollect.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.pokecollect.R
import ar.edu.uade.da2023.pokecollect.data.local.toLocal
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import ar.edu.uade.da2023.pokecollect.model.atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.atributos.Sprites
import ar.edu.uade.da2023.pokecollect.model.atributos.Type
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BagActivity : AppCompatActivity(), BagAdapter.OnRemoveFavoriteListener {

    private lateinit var pokemonButton: Button
    private lateinit var pokedexButton: Button
    private lateinit var searchVw: SearchView
    private lateinit var adapter: BagAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var userDocument: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)
        setSupportActionBar(findViewById(R.id.toolbar))

        adapter = BagAdapter(emptyList(), this)

        // Funcionalidad del SearchView
        searchVw = findViewById(R.id.searchVw)
        searchVw.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filterFavorites(newText)
                return true
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.listBagRecyclerV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Obtener el ID del usuario actualmente autenticado
        val firebaseUser = firebaseAuth.currentUser
        userId = firebaseUser?.uid ?: ""

        // Función de los botones
        pokemonButton = findViewById(R.id.pokemonBtn)
        pokemonButton.setOnClickListener {
            val intent = Intent(this, PokemonActivity::class.java)
            startActivity(intent)
        }

        pokedexButton = findViewById(R.id.pokedexBtn)
        pokedexButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        fetchFavoritesFromFirestore()
    }

    private fun removeFromFavorites(userDocument: DocumentReference, pokemon: PokemonInfo) {
        val favoriteMap = pokemon.toLocal()
        userDocument.update("favorites", FieldValue.arrayRemove(favoriteMap))
    }

    override fun onRemoveFavorite(pokemon: PokemonInfo) {
        removeFromFavorites(userDocument, pokemon)
        adapter.removeFavorite(pokemon)
    }

    private fun fetchFavoritesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        userDocument = db.collection("users").document(userId)

        userDocument.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val favoritesData = document.get("favorites") as? List<Map<String, Any>>
                Log.d("BagActivity", "favoritesData: $favoritesData")
                if (favoritesData != null) {
                    val favorites = favoritesData.map { map ->
                        PokemonInfo(
                            abilities = Gson().fromJson(
                                map["abilities"] as? String,
                                object : TypeToken<List<Ability>>() {}.type
                            ),
                            id = (map["id"] as? Long)?.toInt() ?: 0,
                            name = map["name"] as? String ?: "",
                            order = map["order"] as? Int
                                ?: map["order"]?.toString()?.toInt() ?: 0,
                            sprites = Gson().fromJson(map["sprites"] as? String, Sprites::class.java),
                            types = Gson().fromJson(
                                map["types"] as? String,
                                object : TypeToken<List<Type>>() {}.type
                            )
                        )
                    }
                    Log.d("BagActivity", "favorites: $favorites")
                    adapter.setFavorites(favorites)
                }
            }
        }
    }
}
