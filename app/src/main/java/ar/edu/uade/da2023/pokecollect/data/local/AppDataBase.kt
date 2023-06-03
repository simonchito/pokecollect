package ar.edu.uade.da2023.pokecollect.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Database(entities = [PokemonInfoLocal::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun pokemonDao() : PokemonDao

    companion object {
        @Volatile //garantiza lectura o escritura desde multiples hilos de ejecucion
        private var instance:AppDataBase? = null

        fun getInstance(context: Context): AppDataBase = instance?: synchronized(this){
            instance ?: buildDatabase(context)
        }


        private fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java,"app_database")
                .fallbackToDestructiveMigration()
                .build()
    }
    suspend fun clean(context: Context) = coroutineScope {
        launch(Dispatchers.IO) {
            getInstance(context).clearAllTables()
        }
    }
}