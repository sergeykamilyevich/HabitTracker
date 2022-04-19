package com.example.habittracker.domain.models

sealed class Either<out L, out R> {

    data class Failure<out L, out R>(val error: L) : Either<L, R>() {
        fun fetchError() = error
    }

    data class Success<out L, out R>(val result: R) : Either<L, R>() {
        fun fetchResult() = result
    }

}

fun <E> E.failure() = Either.Failure<E, Nothing>(this)

fun <T> T.success() = Either.Success<Nothing, T>(this)