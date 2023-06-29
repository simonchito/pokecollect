package ar.edu.uade.da2023.pokecollect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg pokemonInfo: PokemonInfoLocal)

    @Delete
    fun delete(pokemonInfo: PokemonInfoLocal)

}