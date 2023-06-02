package ar.edu.uade.da2023.pokecollect.data

import ar.edu.uade.da2023.pokecollect.model.Pokemon

class PokemonsRepository {
    //dependencias
    private val pokemonsDataSource = PokemonsDataSource()

    suspend fun getPokemons() : ArrayList<Pokemon> {

        return pokemonsDataSource.getPokemons()

    }

}