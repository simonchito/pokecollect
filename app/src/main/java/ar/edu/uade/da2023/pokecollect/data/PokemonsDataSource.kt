package ar.edu.uade.da2023.pokecollect.data

import android.util.Log
import ar.edu.uade.da2023.pokecollect.model.Pokemon
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonsDataSource {
    //constantes
    private val _base_url = "https://pokeapi.co/api/v2/"
    private val _tag = "API-POKE"


    suspend fun getPokemons() : ArrayList<Pokemon> {

        Log.d(_tag , "Pokemons DataSource GET")

        val api = Retrofit.Builder()
            .baseUrl(_base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokemonsApi::class.java)

        var result = api.getPokemons().execute()

        return if (result.isSuccessful){
            Log.d(_tag ,"Resultado Exitoso")
            result.body() ?: ArrayList<Pokemon>()
        } else{
            Log.e(_tag , "Error en llamada API" + result.message())
            ArrayList<Pokemon>()
        }
    }

}