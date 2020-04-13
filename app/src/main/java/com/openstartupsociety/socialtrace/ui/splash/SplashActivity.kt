package com.openstartupsociety.socialtrace.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.ui.MainActivity
import com.openstartupsociety.socialtrace.ui.login.LoginActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.destination.observe(this) {
            val intent = if (it == SplashDestination.LOGIN)
                Intent(this, LoginActivity::class.java)
            else Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
