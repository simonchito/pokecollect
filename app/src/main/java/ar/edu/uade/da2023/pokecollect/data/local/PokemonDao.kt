package ar.edu.uade.da2023.pokecollect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getAll() : List<PokemonInfoLocal>

    @Query("SELECT * FROM Pokemon WHERE name = :name LIMIT 1")
    fun getByName(name: String) : PokemonInfoLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(vararg pokemonInfo: PokemonInfoLocal)

    @Delete
    fun delete(pokemonInfo: PokemonInfoLocal)

}