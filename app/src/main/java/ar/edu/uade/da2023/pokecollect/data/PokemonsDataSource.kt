package ar.edu.uade.da2023.pokecollect.data

import Pokemon
import android.util.Log
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class PokemonsDataSource {
    private val BASE_URL = "https://pokeapi.co/api/v2/"
    private val TAG = "API-POKE"

    private val api: PokemonsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PokemonsApi::class.java)
    }

    suspend fun getPokemons(): Pokemon {
        Log.d(TAG, "Pokemons DataSource GET")

        return try {
            val response = api.getPokemons()
            if (response.isSuccessful) {
                val pokemonList = response.body()?.results ?: emptyList()
                val count = response.body()?.count ?: 0
                Pokemon(count, pokemonList)
            } else {
                Log.e(TAG, "Error en llamada API: ${response.message()}")
                Pokemon(0, emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en llamada API: ${e.message}")
            Pokemon(0, emptyList())
        }
    }

    suspend fun getPokemon(name: String): PokemonInfo {
        Log.d(TAG, "Pokemon DataSource GET")
        return try {
            val response = api.getPokemon(name).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("No se recibieron datos del Pokemon")
                val pokemonInfo = response.body()
                if (pokemonInfo != null) {
                    Log.d(TAG, "Response body: ${pokemonInfo.toString()}")
                    pokemonInfo
                } else {
                    throw Exception("No se recibieron datos del Pokemon")
                }
            } else {
                Log.e(TAG, "Error en llamada API: ${response.message()}")
                throw Exception("Error en llamada API")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en llamada API: ${e.message}")
            throw Exception("Error en llamada API")
        }
    }

}