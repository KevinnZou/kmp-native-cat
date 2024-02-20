import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

fun main(args: Array<String>) {
    // Grab the required file path argument
    val fileArgs = args.filterNot { it.startsWith("-") }
    if (fileArgs.size != 1) {
        println("Usage: missing path to file: ./k-cat <file>")
        return
    }
    val filePath = fileArgs.first()
    // Parse for optional `-n` argument
    var numbering = false;
    args.forEach {
        when (it) {
            "-n" -> numbering = true
            "--numbering" -> numbering = true
        }
    }
    FileSystem.SYSTEM.source(filePath.toPath()).use { fileSource ->
        fileSource.buffer().use { bufferedFileSource ->
            var number = 0
            while (true) {
                val line = bufferedFileSource.readUtf8Line() ?: break
                if (numbering) {
                    val formattedNumber = number.toString().padEnd(2, ' ')
                    println("$formattedNumber $line")
                } else {
                    println(line)
                }
                number += 1
            }
        }
    }
}