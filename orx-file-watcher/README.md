# orx-file-watcher

A file watcher for OPENRNDR

## Usage

Monitoring a single file.

```kotlin
application {
    program {
        val watchedText = watchFile(File("someFile.txt")) {
            it.readText()
        }
        extend {
            val theText = watchedText()
        }
    }
}
```

Making a map of monitored files.

```kotlin
application {
    program {
        val watchedTexts = mutableMap<String, ()->String>()
         watchedTexts["text"] = watchFile(File("someFile.txt")) {
            it.readText()
         }

        extend {
            val theText = watchedTexts.getValue("text")()
        }
    }
}
```
