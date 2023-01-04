package com.example.habittracker.feature_habits.presentation.view_models

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * JUnit5 extension to be able to test LiveData objects
 */
class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    /**
     * sets a delegate before each test that updates LiveData immediately on the calling thread
     */
//    @SuppressLint("RestrictedApi")
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
    }

    /**
     * Remove delegate after each test, to avoid influencing other tests
     */
//    @SuppressLint("RestrictedApi")
    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

}

class Test<out T>() {

    private fun foo(): T {
        val a = Any()
        return a as T
    }

    fun bar() {
        val a = Test<Int>()
        val b = a.foo()
        val c: Test<Number> = a
    }
}

class Test2<T : Int?, S>(val id: T, val a: S) {

    fun foo(): T {
        return id
    }

    fun bar() {
        val a = Test2(6, null)
        val b = a.foo()
        val c = listOf<Int>()
    }
}