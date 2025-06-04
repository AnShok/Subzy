package com.anshok.subzy.presentation.settings

import android.annotation.SuppressLint
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
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.databinding.FragmentSettingsBinding
import com.anshok.subzy.domain.settings.model.AppIconStyle
import com.anshok.subzy.domain.settings.model.AppTheme
import com.anshok.subzy.presentation.common.CurrencyPickerBottomSheet
//import com.anshok.subzy.presentation.common.PermissionDialogFragment
import com.anshok.subzy.presentation.settings.bottomsheet.AppIconBottomSheet
import com.anshok.subzy.presentation.settings.bottomsheet.EditNameBottomSheet
import com.anshok.subzy.presentation.settings.bottomsheet.HelpBottomSheet
import com.anshok.subzy.presentation.settings.bottomsheet.RateBottomSheet
import com.anshok.subzy.presentation.settings.bottomsheet.ThemeBottomSheet
import com.anshok.subzy.presentation.settings.bottomsheet.TimeNotifBottomSheet
import com.anshok.subzy.presentation.settings.viewmodel.AppIconViewModel
import com.anshok.subzy.presentation.settings.viewmodel.CurrencyViewModel
//import com.anshok.subzy.presentation.settings.viewmodel.NotificationSettingsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.SettingsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.ThemeViewModel
import com.anshok.subzy.util.animation.animateAppear
import com.anshok.subzy.util.notification.PermissionRequestHelper
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val binding: FragmentSettingsBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: SettingsViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by viewModel()
    private val appIconViewModel: AppIconViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()
    //private val notifViewModel: NotificationSettingsViewModel by viewModel()


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

        setupInitialAnimations()

        // ⛔️ Проверка разрешений
//        if (!PermissionRequestHelper.hasAllRequiredPermissions(requireContext())) {
//            notifViewModel.setNotificationsEnabled(false, requireContext())
//        }

        observeViewModels()
        setupClickListeners()
        //notifViewModel.loadNotificationSettings()
        initSettings()

    }

    private fun setupInitialAnimations() {
        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(250).start()

        val animatedSettings = listOf(
            binding.currencySelector,
            binding.appIconSetting,
            binding.theme,
            //binding.notifications,
            //binding.notificationsTime,
            binding.tgNotices,
            binding.aboutUs,
            binding.rateUs,
            binding.tellFriends,
            binding.help
        )

        animatedSettings.forEachIndexed { index, view ->
            view.animateAppear(delay = 30L * (index + 1))
        }
    }

    @SuppressLint("DefaultLocale")
    private fun observeViewModels() {
        viewModel.profileImage.observe(viewLifecycleOwner) { uri ->
            binding.profileImage.setImageURI(uri)
            binding.profileImage.animateAppear()
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.userName.text = it
            binding.userName.animateAppear()
            binding.profileNameEditor.animateAppear(delay = 100)
        }

        currencyViewModel.selectedCurrencyCode.observe(viewLifecycleOwner) {
            binding.selectedCurrencyCode.text = it
        }

        appIconViewModel.selectedStyle.observe(viewLifecycleOwner) {
            binding.appIconValue.text = it.label
        }

        themeViewModel.selectedTheme.observe(viewLifecycleOwner) {
            binding.themeValue.text = it.label
        }

//        notifViewModel.notificationsEnabled.observe(viewLifecycleOwner) { isEnabled ->
//            binding.notificationsSwitch.setOnCheckedChangeListener(null) // отключаем временно
//            binding.notificationsSwitch.isChecked = isEnabled
//            binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//                handleNotificationToggle(isChecked)
//            }
//            setNotificationsTimeEnabled(isEnabled)
//        }
//
//
//
//
//
//        notifViewModel.notificationTime.observe(viewLifecycleOwner) { (hour, minute) ->
//            binding.notificationsTimeValue.text = String.format("%02d:%02d", hour, minute)
//        }

    }

    private fun setupClickListeners() {
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

        binding.profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.userName.safeDelayedClick { showEditNameBottomSheet() }
        binding.profileNameEditor.safeDelayedClick { showEditNameBottomSheet() }

        binding.currencySelector.safeDelayedClick { showCurrencyBottomSheet() }
        binding.appIconSetting.safeDelayedClick { showAppIconBottomSheet() }
        binding.theme.safeDelayedClick { showThemeBottomSheet() }

        binding.rateUs.safeDelayedClick {
            RateBottomSheet().show(parentFragmentManager, "RateBottomSheet")
        }

        binding.tellFriends.safeDelayedClick {
            shareApp()
        }

        binding.help.safeDelayedClick {
            HelpBottomSheet().show(parentFragmentManager, "HelpBottomSheet")
        }
//        binding.notifications.safeDelayedClick {
//            val isCurrentlyEnabled = binding.notificationsSwitch.isChecked
//            handleNotificationToggle(!isCurrentlyEnabled)
//        }



//        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            // чтобы не срабатывало от toggle()
//            if (PermissionRequestHelper.isPermissionGranted(requireContext())) {
//                notifViewModel.setNotificationsEnabled(isChecked, requireContext())
//            } else if (isChecked) {
//                binding.notificationsSwitch.isChecked = false
//                PermissionDialogFragment {
//                    notifViewModel.setNotificationsEnabled(true, requireContext())
//                }.show(parentFragmentManager, "PermissionDialog")
//            }
//        }




//        binding.notificationsTime.safeDelayedClick {
//            TimeNotifBottomSheet(
//                onTimeSaved = { hour, minute ->
//                    notifViewModel.setNotificationTime(hour, minute, requireContext())
//                },
//                initialTime = notifViewModel.notificationTime.value ?: (9 to 0)
//            ).show(parentFragmentManager, "TimeNotifBottomSheet")
//        }



    }

    private fun initSettings() {
        viewModel.loadImage()
        viewModel.loadUserName()
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

//    private fun setNotificationsTimeEnabled(enabled: Boolean) {
//        binding.notificationsTime.isEnabled = enabled
//        binding.notificationsTime.alpha = if (enabled) 1f else 0.5f
//    }

//    private fun handleNotificationToggle(isChecked: Boolean) {
//        if (PermissionRequestHelper.hasAllRequiredPermissions(requireContext())) {
//            notifViewModel.setNotificationsEnabled(isChecked, requireContext())
//        } else if (isChecked) {
//            // Откатываем переключение
//            binding.notificationsSwitch.isChecked = false
//
//            PermissionDialogFragment {
//                if (PermissionRequestHelper.hasAllRequiredPermissions(requireContext())) {
//                    notifViewModel.setNotificationsEnabled(true, requireContext())
//                    binding.notificationsSwitch.isChecked = true
//                }
//            }.show(parentFragmentManager, "PermissionDialog")
//        } else {
//            notifViewModel.setNotificationsEnabled(false, requireContext())
//        }
//    }



}