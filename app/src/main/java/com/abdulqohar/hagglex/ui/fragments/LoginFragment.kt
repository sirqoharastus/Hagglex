package com.abdulqohar.hagglex.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulqohar.hagglex.R
import com.abdulqohar.hagglex.apollo.apolloClient
import com.abdulqohar.hagglex.databinding.FragmentLoginBinding
import com.abdulqohar.hagglex.graphql.LoginMutation
import com.abdulqohar.hagglex.graphql.type.LoginInput
import com.abdulqohar.hagglex.ui.activities.DashbordActivity
import com.abdulqohar.hagglex.utils.makeSnackbar
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.launch
import java.lang.Exception


class LoginFragment : Fragment() {
    // Binding variables set up
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setting click listener on create account button
        binding.loginFragmentCreateNewAcountTextview.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        // Implementation to create instance of login model from user input
        binding.loginFragmentLogInButton.setOnClickListener {
            val email = binding.loginFragmentEmailEditText.text.toString()
            val password = binding.loginFragmentPasswordEditText.text.toString()
            loginUser(email, password)
        }
    }

    // Function to login user and handle errors appropriately
    private fun loginUser(email: String, password: String) {
        if (binding.loginFragmentEmailEditText.text.isEmpty() || binding.loginFragmentPasswordEditText.text.isEmpty()){
            makeSnackbar("Fields cannot be empty")
            return
        }
        binding.loginFragmentProgressbar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val response = try {
                apolloClient.mutate(LoginMutation(data = LoginInput(email, password))).await()
            }
            catch (e: Exception){
                null
            }

            val result = response?.data?.login

            if (result == null || response.hasErrors() ){
                makeSnackbar("${response?.errors?.get(0)?.message}")
                binding.loginFragmentProgressbar.visibility = View.GONE
            }
            else {
                binding.loginFragmentProgressbar.visibility = View.GONE
                makeSnackbar("Login Successful")
                val intent = Intent(requireContext(), DashbordActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }
    }
}