package com.anshok.subzy.presentation.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private val binding: ActivityRootBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.calendarFragment -> {
                    // Установка цвета статус-бара для HomeFragment и CalendarFragment
                    setStatusBarColor(R.color.Gray_75)
                    binding.bottomNavigationView.isVisible = true
                    binding.fab.isVisible = true
                }

                R.id.spendFragment, R.id.settingsFragment -> {
                    // Установка цвета статус-бара для SpendFragment и WalletFragment
                    setStatusBarColor(R.color.Gray_80)
                    binding.bottomNavigationView.isVisible = true
                    binding.fab.isVisible = true
                }

                R.id.addSubSearchFragment -> {
                    // Установка цвета статус-бара для SettingsFragment и NewSubscriptionFragment
                    setStatusBarColor(R.color.Gray_80)
                    binding.bottomNavigationView.isVisible = false
                    binding.fab.isVisible = false
                }

                R.id.addSubSearchFragment -> {
                    // Установка цвета статус-бара для SettingsFragment и NewSubscriptionFragment
                    setStatusBarColor(R.color.Gray_75)
                    binding.bottomNavigationView.isVisible = false
                    binding.fab.isVisible = false
                }

                else -> {
                    // Установка стандартного цвета статус-бара
                    setStatusBarColor(R.color.Gray_80)
                    binding.bottomNavigationView.isVisible = false
                    binding.fab.isVisible = false
                }
            }
        }

        // Обработка нажатия на FloatingActionButton (FAB)
        binding.fab.setOnClickListener {
            navController.navigate(R.id.addSubSearchFragment)
        }

        // Обработка системных отступов для корректной работы с навигацией и FAB
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.bottomNavigationView.setPadding(0, 0, 0, systemBarsInsets.bottom)
            binding.fab.translationY = -systemBarsInsets.bottom.toFloat() / 2
            insets
        }
    }

    // Метод для установки цвета статус-бара
    private fun setStatusBarColor(colorResId: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResId)
    }
}