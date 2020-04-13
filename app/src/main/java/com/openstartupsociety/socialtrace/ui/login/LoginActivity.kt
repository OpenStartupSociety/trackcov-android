package com.openstartupsociety.socialtrace.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.databinding.ActivityLoginBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            val fragment = LoginFragment.newInstance()
            replace(R.id.container, fragment, fragment::class.java.simpleName)
            addToBackStack(null)
        }
    }

    override fun onBackPressed() {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)
        if (fragment != null && fragment.isVisible) finishAffinity()
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
        else super.onBackPressed()
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
