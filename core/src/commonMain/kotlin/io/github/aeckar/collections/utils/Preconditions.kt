package io.github.aeckar.collections.utils

internal inline fun <R> checkBounds(access: () -> R): R {
    return try {
        access()
    } catch (_: IndexOutOfBoundsException) {
        throw NoSuchElementException("Iterator is exhausted")
    }
}