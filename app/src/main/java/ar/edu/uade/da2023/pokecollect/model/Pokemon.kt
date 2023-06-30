import ar.edu.uade.da2023.pokecollect.model.Poke

data class Pokemon(
    val count: Int = 0,
    val next: String?,
    val previous: String?,
    val results: List<Poke>
)