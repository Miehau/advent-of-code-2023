val numberRegex = "(\\d+)".toRegex()
val combinationRegex = "(\\d+) (\\w+)".toRegex()
fun main() {
    fun buildMinColourRequiredMap(
        next: MatchResult,
        acc: MutableMap<String, Int>
    ): MutableMap<String, Int> {
        val (number, colour) = next.destructured
        if (acc.containsKey(colour)) {
            if (acc[colour]!! < number.toInt()) {
                acc[colour] = number.toInt()
            }
        } else {
            acc[colour] = number.toInt()
        }
        return acc
    }

    fun mapToGameCombinationPair(line: String): Pair<String, MutableMap<String, Int>> {
        val gameAndCombinations = line.split(":")
        val game = numberRegex.find(gameAndCombinations[0])!!.value
        val combinations = gameAndCombinations
            .drop(1)
            .flatMap { comb -> comb.split(";") }
            .flatMap { comb -> combinationRegex.findAll(comb) }
            .fold(mutableMapOf<String, Int>()) { acc, next ->
                buildMinColourRequiredMap(next, acc)
            }
        return Pair(game, combinations)
    }

    fun part1(input: List<String>): Int {
        return input
            .map { line -> mapToGameCombinationPair(line) }
            .filter { (_, combinations) ->
                combinations["red"]!! <= 12 && combinations["green"]!! <= 13 && combinations["blue"]!! <= 14
            }
            .sumOf { it.first.toInt() }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line -> mapToGameCombinationPair(line) }
            .map { (game, combinations) -> combinations.values.fold(1) {acc, next ->  acc * next} }
            .fold(0) { acc, next -> acc + next}
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("day2_test")
    part1(testInput).println()

    val input = readInput("day2")
    part1(input).println()
    part2(input).println()
}

