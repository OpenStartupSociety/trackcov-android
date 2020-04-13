package com.openstartupsociety.socialtrace.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.databinding.ActivityMainBinding
import com.openstartupsociety.socialtrace.ui.dailycheck.SymptomFragment
import com.openstartupsociety.socialtrace.ui.home.DashboardFragment
import com.openstartupsociety.socialtrace.ui.notifications.NotificationFragment
import com.openstartupsociety.socialtrace.ui.profile.ProfileFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    if (currentFragment !is DashboardFragment) {
                        loadFragment(DashboardFragment.newInstance())
                        return@OnNavigationItemSelectedListener true
                    } else return@OnNavigationItemSelectedListener false
                }
                R.id.nav_symptoms -> {
                    if (currentFragment !is SymptomFragment) {
                        loadFragment(SymptomFragment.newInstance())
                        return@OnNavigationItemSelectedListener true
                    } else return@OnNavigationItemSelectedListener false
                }
                R.id.nav_notifications -> {
                    if (currentFragment !is NotificationFragment) {
                        loadFragment(NotificationFragment.newInstance())
                        return@OnNavigationItemSelectedListener true
                    } else return@OnNavigationItemSelectedListener false
                }
                R.id.nav_profile -> {
                    if (currentFragment !is ProfileFragment) {
                        loadFragment(ProfileFragment.newInstance())
                        return@OnNavigationItemSelectedListener true
                    } else return@OnNavigationItemSelectedListener false
                }
                else -> return@OnNavigationItemSelectedListener false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = viewModel

        binding.navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            val menu = binding.navigation.menu
            when (fragment) {
                is DashboardFragment -> menu.getItem(0).isChecked = true
                is SymptomFragment -> menu.getItem(1).isChecked = true
                is NotificationFragment -> menu.getItem(2).isChecked = true
                is ProfileFragment -> menu.getItem(3).isChecked = true
            }
        }

        loadFragment(DashboardFragment.newInstance())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragment, fragment::class.java.simpleName)
            addToBackStack(fragment::class.java.simpleName)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!hasPermissions(PERMISSIONS) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(PERMISSIONS, REQ_CODE_PERMISSION)
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQ_CODE_PERMISSION) return

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Grant permissions", Toast.LENGTH_LONG).show()
                return
            }
        }
        recreate()
    }

    override fun onBackPressed() {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.simpleName)
        if (fragment != null && fragment.isVisible) finishAffinity()
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
        else loadFragment(DashboardFragment.newInstance())
    }

    override fun androidInjector() = dispatchingAndroidInjector

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        const val REQ_CODE_PERMISSION = 101
    }
}
