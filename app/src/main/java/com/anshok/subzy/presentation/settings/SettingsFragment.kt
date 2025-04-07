package com.anshok.subzy.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentSettingsBinding
import com.anshok.subzy.domain.settings.model.AppIconStyle
import com.anshok.subzy.domain.settings.model.AppTheme
import com.anshok.subzy.presentation.common.CurrencyPickerBottomSheet
import com.anshok.subzy.presentation.settings.bottomSheet.AppIconBottomSheet
import com.anshok.subzy.presentation.settings.bottomSheet.EditNameBottomSheet
import com.anshok.subzy.presentation.settings.bottomSheet.HelpBottomSheet
import com.anshok.subzy.presentation.settings.bottomSheet.RateBottomSheet
import com.anshok.subzy.presentation.settings.bottomSheet.ThemeBottomSheet
import com.anshok.subzy.presentation.settings.viewmodel.AppIconViewModel
import com.anshok.subzy.presentation.settings.viewmodel.CurrencyViewModel
import com.anshok.subzy.presentation.settings.viewmodel.SettingsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.ThemeViewModel
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val binding: FragmentSettingsBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: SettingsViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by viewModel()
    private val appIconViewModel: AppIconViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()

    // Выбор изображения из галереи
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.saveImage(it) }
        }

    private val shareLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK || result.resultCode == AppCompatActivity.RESULT_CANCELED) {
                Snackbar.make(binding.root, getString(R.string.thanks_for_sharing), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.Accent_P_100))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val radius = 20f
            val blurEffect = android.graphics.RenderEffect.createBlurEffect(
                radius, radius,
                android.graphics.Shader.TileMode.CLAMP
            )
            binding.tgNotices.setRenderEffect(blurEffect)
        }
        binding.tgNotices.safeDelayedClick {
            Snackbar.make(binding.root, "Soon to be available", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.Accent_P_100))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .show()

        }


        binding.aboutUs.safeDelayedClick {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutUsFragment)
        }


        // Загрузка изображения при старте
        viewModel.profileImage.observe(viewLifecycleOwner) { uri ->
            binding.profileImage.setImageURI(uri)
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.userName.text = it
        }

        currencyViewModel.selectedCurrencyCode.observe(viewLifecycleOwner) { code ->
            binding.selectedCurrencyCode.text = code
        }


        appIconViewModel.selectedStyle.observe(viewLifecycleOwner) {
            binding.appIconValue.text = it.label
        }

        themeViewModel.selectedTheme.observe(viewLifecycleOwner) {
            binding.themeValue.text = it.label
        }

        viewModel.loadImage()
        viewModel.loadUserName()

        // Обработка нажатия на фото
        binding.profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.userName.safeDelayedClick { showEditNameBottomSheet() }
        binding.profileNameEditor.safeDelayedClick { showEditNameBottomSheet() }

        // Выбор валюты по умолчанию
        binding.currencySelector.safeDelayedClick { showCurrencyBottomSheet() }

        // Выбор иконки приложения
        binding.appIconSetting.safeDelayedClick { showAppIconBottomSheet() }


        // Тема
        binding.theme.safeDelayedClick { showThemeBottomSheet() }

        binding.rateUs.safeDelayedClick {
            RateBottomSheet().show(parentFragmentManager, "RateBottomSheet")
        }

        // Поделиться
        binding.tellFriends.safeDelayedClick {
            shareApp()
        }

        // Помощь
        binding.help.safeDelayedClick {
            HelpBottomSheet().show(parentFragmentManager, "HelpBottomSheet")
        }


    }

    // Редактирование имени (нажатие на текст или иконку)
    private val showEditNameBottomSheet = {
        EditNameBottomSheet(
            currentName = viewModel.userName.value ?: "",
            onNameSaved = { viewModel.saveUserName(it) }
        ).show(parentFragmentManager, "EditNameBottomSheet")
    }

    // Выбор валюты по умолчанию
    private val showCurrencyBottomSheet = {
        CurrencyPickerBottomSheet(
            currencies = currencyViewModel.currencies.value.orEmpty(),
            currentCode = currencyViewModel.selectedCurrencyCode.value ?: "USD",
            onCurrencySelected = { selectedCode ->
                currencyViewModel.setCurrencyCode(selectedCode)
            }
        ).show(parentFragmentManager, "CurrencyPicker")
    }


    // Выбор иконки приложения
    private val showAppIconBottomSheet = {
        AppIconBottomSheet(
            selectedStyle = appIconViewModel.selectedStyle.value ?: AppIconStyle.CLASSIC,
            onIconSelected = { appIconViewModel.selectStyle(it) }
        ).show(parentFragmentManager, "AppIconBottomSheet")
    }

    // Выбор темы
    private val showThemeBottomSheet = {
        ThemeBottomSheet(
            selectedTheme = themeViewModel.selectedTheme.value ?: AppTheme.SYSTEM,
            onThemeSelected = { themeViewModel.setTheme(it) }
        ).show(parentFragmentManager, "ThemeBottomSheet")
    }

    // Поделиться
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, viewModel.getShareText())
        }

        val chooser = Intent.createChooser(shareIntent, getString(R.string.share_with_friends))
        shareLauncher.launch(chooser)
    }
}