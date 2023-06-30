package ar.edu.uade.da2023.pokecollect.data

import Pokemon
import android.util.Log
import ar.edu.uade.da2023.pokecollect.data.local.toLocal
import ar.edu.uade.da2023.pokecollect.model.Poke
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        var nextUrl: String? = null
        val pokemonList = mutableListOf<Poke>()

        try {
            do {
                val response = if (nextUrl.isNullOrEmpty()) {
                    api.getPokemons()
                } else {
                    api.getPokemonsByUrl(nextUrl)
                }

                if (response.isSuccessful) {
                    val result = response.body()
                    result?.results?.let {
                        pokemonList.addAll(it)
                    }
                    nextUrl = result?.next
                } else {
                    Log.e(TAG, "Error en llamada API: ${response.message()}")
                    return Pokemon(0, null, null, emptyList())
                }
            } while (nextUrl != null)

            return Pokemon(pokemonList.size, null, null, pokemonList)
        } catch (e: Exception) {
            Log.e(TAG, "Error en llamada API: ${e.message}")
            return Pokemon(0, null, null, emptyList())
        }
    }



    suspend fun getPokemon(name: String): PokemonInfo {
        Log.d(TAG, "Pokemon DataSource GET")
        return try {
            val response = api.getPokemon(name).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("No se recibieron datos del Pokemon")
                val pokemonInfo = response.body()
                //guarda pokemon a la informacion local
                var pokemoninfoLocal = pokemonInfo?.toLocal()
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