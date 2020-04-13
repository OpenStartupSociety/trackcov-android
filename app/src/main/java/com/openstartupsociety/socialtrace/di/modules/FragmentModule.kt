package com.openstartupsociety.socialtrace.di.modules

import com.openstartupsociety.socialtrace.ui.dailycheck.DailyCheckFragment
import com.openstartupsociety.socialtrace.ui.dailycheck.SymptomDisplayFragment
import com.openstartupsociety.socialtrace.ui.dailycheck.SymptomFragment
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileDisplayFragment
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileFragment
import com.openstartupsociety.socialtrace.ui.home.DashboardFragment
import com.openstartupsociety.socialtrace.ui.login.LoginFragment
import com.openstartupsociety.socialtrace.ui.login.VerifyOtpFragment
import com.openstartupsociety.socialtrace.ui.login.WelcomeFragment
import com.openstartupsociety.socialtrace.ui.notifications.NotificationFragment
import com.openstartupsociety.socialtrace.ui.profile.EditProfileFragment
import com.openstartupsociety.socialtrace.ui.profile.ProfileFragment
import com.openstartupsociety.socialtrace.ui.scancode.FullScreenCodeFragment
import com.openstartupsociety.socialtrace.ui.settings.AboutTeamFragment
import com.openstartupsociety.socialtrace.ui.settings.FeedbackFragment
import com.openstartupsociety.socialtrace.ui.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeVerifyOtpFragment(): VerifyOtpFragment

    @ContributesAndroidInjector
    abstract fun contributeWelcomeFragment(): WelcomeFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeEditProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsActivity.SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeFullScreenCodeFragment(): FullScreenCodeFragment

    @ContributesAndroidInjector
    abstract fun contributeDailyCheckFragment(): DailyCheckFragment

    @ContributesAndroidInjector
    abstract fun contributeHealthProfileFragment(): HealthProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributeSymptomFragment(): SymptomFragment

    @ContributesAndroidInjector
    abstract fun contributeHealthProfileDisplayFragment(): HealthProfileDisplayFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutTeamFragment(): AboutTeamFragment

    @ContributesAndroidInjector
    abstract fun contributeFeedbackFragment(): FeedbackFragment

    @ContributesAndroidInjector
    abstract fun contributeSymptomDisplayFragment(): SymptomDisplayFragment
}
