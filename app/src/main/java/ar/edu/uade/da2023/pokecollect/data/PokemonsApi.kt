package ar.edu.uade.da2023.pokecollect.data

import Pokemon
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path

interface PokemonsApi {

    @GET("pokemon")
    suspend fun getPokemons(): Response<Pokemon>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Call<PokemonInfo>

}
