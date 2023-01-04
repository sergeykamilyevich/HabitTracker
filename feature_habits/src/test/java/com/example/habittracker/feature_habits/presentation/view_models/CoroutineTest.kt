package com.example.habittracker.feature_habits.presentation.view_models

import kotlinx.coroutines.*
import org.junit.Test

class CoroutineTest {

    @Test
    fun test() = runBlocking(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        println("before ${Thread.currentThread()}")
        launch {
//            for (i in 1..100000000) {
//                val a = log(sqrt(i.toDouble()),10.8)
//            }
            delay(1000)
            println("${Thread.currentThread()} launch 1 time ${System.currentTimeMillis() - start}")
        }
        launch {
//            for (i in 1..200000000) {
//                val a = log(sqrt(i.toDouble()),10.8)
//            }
            delay(2000)
            println("${Thread.currentThread()} launch 2 time ${System.currentTimeMillis() - start}")
        }
        val a: Deferred<Unit> = async {
//            for (i in 1..100000000) {
//                val a = log(sqrt(i.toDouble()),10.8)
//            }
            delay(3000)
            println("${Thread.currentThread()} async a time ${System.currentTimeMillis() - start}")
        }
        val b = async {
            println("${Thread.currentThread()} async b0 time ${System.currentTimeMillis() - start}")
//            for (i in 1..300000000) {
//                val a = log(sqrt(i.toDouble()),10.8)
//            }
            delay(4000)
            println("${Thread.currentThread()} async b1 time ${System.currentTimeMillis() - start}")
        }
        println("${Thread.currentThread()} after all time ${System.currentTimeMillis() - start}")
    }

}