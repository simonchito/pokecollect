package ar.edu.uade.da2023.pokecollect.ui

import Pokemon
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.uade.da2023.pokecollect.data.PokemonsRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class   MainViewModel : ViewModel() {
    //constante
    private val _TAG = "API-POKE"
    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("pokecollect")
    private val scope = CoroutineScope(coroutineContext)

    //dependencias
    private val pokemonsRepository = PokemonsRepository()


    //propiedades
    var pokemon = MutableLiveData<Pokemon>()


    //funciones
    fun onStart(){
        // cargar los datos de los pokemones
        scope.launch {
            kotlin.runCatching {
                pokemonsRepository.getPokemons()
            }.onSuccess {
                Log.d(_TAG, "Pokemon onSuccess")
                pokemon.postValue(it)
            }.onFailure {
                Log.e(_TAG,"Pokemon Error" + it)
            }
        }
    }
}

