package r.evgenymotorin.recipes.di.activity

import android.content.Context
import dagger.Module
import dagger.Provides
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

@Module
class MainModule(val context: Context){

    @Provides
    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}