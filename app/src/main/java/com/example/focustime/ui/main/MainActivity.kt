package com.example.focustime.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.focustime.R
import com.example.focustime.data.PreferencesManager
import com.example.focustime.databinding.ActivityMainBinding
import com.example.focustime.ui.tasklist.TaskListFragment
import com.example.focustime.ui.timer.TimerFragment
import com.example.focustime.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector
    private lateinit var preferencesManager: PreferencesManager
    private var currentTab = R.id.nav_tasks
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Initialize preferences and apply theme
            preferencesManager = PreferencesManager(this)
            applyTheme(preferencesManager.isDarkMode)
            
            enableEdgeToEdge()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            
            setupGestureDetector()
            setupBottomNavigation()
            
            // Add TaskListFragment as default
            if (savedInstanceState == null) {
                loadFragment(TaskListFragment(), false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val swipeThreshold = 100
                val velocityThreshold = 1000
                
                if (Math.abs(velocityX) > velocityThreshold) {
                    if (velocityX > 0) {
                        // Swipe right - go to previous tab
                        when (currentTab) {
                            R.id.nav_timer -> switchToTab(R.id.nav_tasks)
                            R.id.nav_settings -> switchToTab(R.id.nav_timer)
                        }
                        return true
                    } else {
                        // Swipe left - go to next tab
                        when (currentTab) {
                            R.id.nav_tasks -> switchToTab(R.id.nav_timer)
                            R.id.nav_timer -> switchToTab(R.id.nav_settings)
                        }
                        return true
                    }
                }
                return false
            }
        })
        
        binding.fragmentContainer.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            switchToTab(item.itemId)
            true
        }
    }
    
    private fun switchToTab(tabId: Int) {
        if (currentTab == tabId) return
        
        currentTab = tabId
        binding.bottomNavigation.selectedItemId = tabId
        
        val fragment = when (tabId) {
            R.id.nav_tasks -> TaskListFragment()
            R.id.nav_timer -> TimerFragment()
            R.id.nav_settings -> SettingsFragment()
            else -> return
        }
        
        loadFragment(fragment, true)
        animateTabSwitch()
    }
    
    private fun loadFragment(fragment: Fragment, animate: Boolean) {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            
            if (animate) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
            
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun animateTabSwitch() {
        // Animate the selected tab icon
        val selectedView = binding.bottomNavigation.findViewById<View>(currentTab)
        selectedView?.let { view ->
            val scaleUp = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f)
            val scaleDown = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f)
            
            scaleUp.duration = 150
            scaleDown.duration = 150
            scaleUp.interpolator = OvershootInterpolator()
            scaleDown.interpolator = DecelerateInterpolator()
            
            scaleUp.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    scaleDown.start()
                }
            })
            
            scaleUp.start()
        }
    }
    
    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
