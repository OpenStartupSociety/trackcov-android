package com.openstartupsociety.socialtrace.di.modules

import com.openstartupsociety.socialtrace.services.FcmService
import com.openstartupsociety.socialtrace.services.NearbyService
import com.openstartupsociety.socialtrace.ui.MainActivity
import com.openstartupsociety.socialtrace.ui.login.LoginActivity
import com.openstartupsociety.socialtrace.ui.scancode.ScannerActivity
import com.openstartupsociety.socialtrace.ui.settings.SettingsActivity
import com.openstartupsociety.socialtrace.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun contributeScannerActivity(): ScannerActivity

    @ContributesAndroidInjector
    abstract fun contributeNearbyService(): NearbyService

    @ContributesAndroidInjector
    abstract fun contributeFcmService(): FcmService
}
