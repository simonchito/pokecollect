package ar.edu.uade.da2023.pokecollect.model

data class Pokemon(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Poke>
)