import ar.edu.uade.da2023.pokecollect.model.Poke

data class Pokemon @JvmOverloads constructor(
    val count: Int = 0,
    val results: List<Poke> = emptyList()
)