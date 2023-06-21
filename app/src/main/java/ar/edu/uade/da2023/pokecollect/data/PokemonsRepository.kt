package ar.edu.uade.da2023.pokecollect.data

import Pokemon
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo

class PokemonsRepository {
    //dependencias
    private val pokemonsDataSource = PokemonsDataSource()

    suspend fun getPokemons() : Pokemon {
        return pokemonsDataSource.getPokemons()
    }

    suspend fun getPokemon(name: String): PokemonInfo {
        return pokemonsDataSource.getPokemon(name)
    }
}