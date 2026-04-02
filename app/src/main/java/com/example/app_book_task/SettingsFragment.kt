package com.example.app_book_task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_book_task.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        
        binding.switchDarkMode.isChecked = prefs.getBoolean("dark_mode", false)
        binding.switchSortDate.isChecked = prefs.getBoolean("sort_date", false)
        binding.seekbarFontSize.progress = prefs.getInt("font_size", 0)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.switchSortDate.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sort_date", isChecked).apply()
        }

        binding.seekbarFontSize.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                prefs.edit().putInt("font_size", progress).apply()
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        binding.btnAbout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.about)
                .setMessage(R.string.app_info)
                .setPositiveButton("OK", null)
                .show()
        }

        binding.btnLogout.setOnClickListener {
            val authPrefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            authPrefs.edit().clear().apply()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
