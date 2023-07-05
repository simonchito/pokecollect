import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.da2023.pokecollect.data.PokemonsRepository
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val pokemonsRepository = PokemonsRepository()
    val pokemonInfo = MutableLiveData<PokemonInfo>()

    fun getPokemon(pokemonName: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                pokemonsRepository.getPokemon(pokemonName)
            }.onSuccess {
                pokemonInfo.postValue(it)
            }.onFailure {
                // Manejar el error si la obtención del Pokémon falla
            }
        }
    }
}
