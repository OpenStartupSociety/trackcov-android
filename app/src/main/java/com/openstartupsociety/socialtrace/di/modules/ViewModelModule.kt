package com.openstartupsociety.socialtrace.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openstartupsociety.socialtrace.di.ViewModelFactory
import com.openstartupsociety.socialtrace.di.ViewModelKey
import com.openstartupsociety.socialtrace.ui.MainViewModel
import com.openstartupsociety.socialtrace.ui.dailycheck.DailyCheckViewModel
import com.openstartupsociety.socialtrace.ui.dailycheck.SymptomViewModel
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileDisplayViewModel
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileViewModel
import com.openstartupsociety.socialtrace.ui.home.DashboardViewModel
import com.openstartupsociety.socialtrace.ui.login.LoginViewModel
import com.openstartupsociety.socialtrace.ui.notifications.NotificationViewModel
import com.openstartupsociety.socialtrace.ui.profile.EditProfileViewModel
import com.openstartupsociety.socialtrace.ui.profile.ProfileViewModel
import com.openstartupsociety.socialtrace.ui.scancode.FullScreenCodeViewModel
import com.openstartupsociety.socialtrace.ui.scancode.ScannerViewModel
import com.openstartupsociety.socialtrace.ui.settings.FeedbackViewModel
import com.openstartupsociety.socialtrace.ui.settings.SettingsViewModel
import com.openstartupsociety.socialtrace.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindHomeViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullScreenCodeViewModel::class)
    abstract fun bindFullScreenCodeViewModel(viewModel: FullScreenCodeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DailyCheckViewModel::class)
    abstract fun bindDailyCheckViewModel(viewModel: DailyCheckViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HealthProfileViewModel::class)
    abstract fun bindHealthProfileViewModel(viewModel: HealthProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(viewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SymptomViewModel::class)
    abstract fun bindSymptomViewModel(viewModel: SymptomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScannerViewModel::class)
    abstract fun bindScannerViewModel(viewModel: ScannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HealthProfileDisplayViewModel::class)
    abstract fun bindHealthProfileDisplayViewModel(viewModel: HealthProfileDisplayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedbackViewModel::class)
    abstract fun bindFeedbackViewModel(viewModel: FeedbackViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
