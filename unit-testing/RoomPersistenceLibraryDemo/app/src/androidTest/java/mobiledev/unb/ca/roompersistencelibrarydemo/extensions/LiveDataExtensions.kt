package mobiledev.unb.ca.roompersistencelibrarydemo.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val observer = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(observer)
    try {
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    return value
}
