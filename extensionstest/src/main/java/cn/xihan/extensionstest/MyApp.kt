package cn.xihan.extensionstest

import android.app.Application
import com.pluto.Pluto
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.logger.PlutoLoggerPlugin
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin
import timber.log.Timber

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 2:02
 * @介绍 :
 */
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Pluto.Installer(this)
            .addPlugin(PlutoExceptionsPlugin("exceptions"))
            .addPlugin(PlutoLoggerPlugin("logger"))
            .addPlugin(PlutoRoomsDatabasePlugin("rooms-db"))
            .install()
    }

}