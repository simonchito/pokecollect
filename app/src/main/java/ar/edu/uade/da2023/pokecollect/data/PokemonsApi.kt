package ar.edu.uade.da2023.pokecollect.data

import ar.edu.uade.da2023.pokecollect.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET

interface PokemonsApi {

    @GET("pokemon")
    suspend fun getPokemons () : Call<ArrayList<Pokemon>>


}