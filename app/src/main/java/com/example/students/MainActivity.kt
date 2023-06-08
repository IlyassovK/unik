package com.example.students

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.students.databinding.ActivityMainBinding
import com.example.students.features.LanguageDialog
import com.example.students.utils.LocaleManager


//todo
// 1. Комментарии в ленте
// 2. Форма UI UX

class MainActivity : AppCompatActivity(), LanguageDialog.LangDialogListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.feedFragment || destination.id == R.id.chatFragment
                || destination.id == R.id.mapFragment || destination.id == R.id.contactsFragment || destination.id == R.id.profileFragment
            ) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }
        }

        LocaleManager.overrideLocale(this)

    }

    override fun attachBaseContext(newBase: Context?) {
        val newContext = LocaleManager.wrapContext(newBase)
        super.attachBaseContext(newContext)
    }

    fun switchLanguage(language: LocaleManager.SupportedLanguages) {
        LocaleManager.switchLanguage(this, language)
        restart()
    }

    private fun restart() {
        recreate()
        finish()
        startActivity(intent)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onLanguageSelected(lang: LocaleManager.SupportedLanguages) {
        switchLanguage(lang)
    }
}