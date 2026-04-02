package com.example.app_book_task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_book_task.databinding.FragmentRegisterBinding
import com.example.app_book_task.db.DBHelper
import com.example.app_book_task.models.User

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        binding.btnRegister.setOnClickListener {
            val username = binding.etRegUsername.text.toString()
            val password = binding.etRegPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val userId = dbHelper.registerUser(User(username = username, password = password))
                if (userId != -1L) {
                    val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                    prefs.edit().putInt("user_id", userId.toInt()).apply()
                    
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_tasksFragment)
                } else {
                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
