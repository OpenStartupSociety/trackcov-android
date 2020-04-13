package com.openstartupsociety.socialtrace.di.component

import com.openstartupsociety.socialtrace.App
import com.openstartupsociety.socialtrace.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        PersistenceModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        FragmentModule::class,
        FirebaseModule::class,
        AndroidInjectionModule::class,
        AppModule::class
    ]
)

interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
