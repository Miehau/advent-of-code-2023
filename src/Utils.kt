import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/resources/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun Char?.toStringOrEmpty(): String {
    return this?.toString() ?: ""
}

fun valueOrZero(value: Int): Int {
    return if (value < 0) {
        0
    } else {
        value
    }
}

fun valueOrLast(value: Int, collection: Collection<Any>): Int {
    return if (value >= collection.size) {
        collection.size
    } else {
        value
    }
}