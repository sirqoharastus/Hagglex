package com.abdulqohar.hagglex.ui.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulqohar.hagglex.R
import com.abdulqohar.hagglex.apollo.apolloClient
import com.abdulqohar.hagglex.databinding.FragmentCreateAccountBinding
import com.abdulqohar.hagglex.graphql.RegisterMutation
import com.abdulqohar.hagglex.graphql.type.CreateUserInput
import com.abdulqohar.hagglex.utils.SharedPreferenceManager
import com.abdulqohar.hagglex.utils.makeSnackbar
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.launch


class CreateAccountFragment : Fragment() {
    //Binding variables set up
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Implementation to set back navigation icon and behavior
        binding.createAccountFragmentToolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        //Implementation to create instance of create user model
        binding.createAccountFragmentSignupButton.setOnClickListener {
            val email = binding.createAccountFragmentEmailEdittext.text.toString()
            val password = binding.createAccountFragmentPasswordEditText.text.toString()
            val countrycode = binding.createAccountFragmentCountryPicker.selectedCountryCode
            val username = binding.createAccountFragmentUsernameEditText.text.toString()
            val phoneNumber =
                "$countrycode+${binding.createAccountFragmentCountryPickerEditText.toString()}"
            val referralCode = binding.createAccountFragmentReferralCodeEditText.text.toString()
            val country = binding.createAccountFragmentCountryPicker.selectedCountryName
            createUser(email, password, username, phoneNumber, referralCode, country)
        }
    }

    // Function to create user or handle exceptions appropriately
    private fun createUser(
        email: String,
        password: String,
        username: String,
        phoneNumber: String,
        referralCode: String? = null,
        country: String
    ) {
        if (validateUser(email, password)) {
            binding.createAccountFragmentProgressbar.visibility = View.VISIBLE
            val user = CreateUserInput(
                email = email,
                username = username,
                password = password,
                phonenumber = phoneNumber,
                referralCode = Input.fromNullable(referralCode),
                country = "Nigeria",
                currency = "Naira"
            )
            lifecycleScope.launch {
                val response = try {
                    apolloClient.mutate(RegisterMutation(user = Input.fromNullable(user))).await()
                } catch (e: Exception) {
                    throw java.lang.Exception(e)
                }

                val register = response.data?.register

                if (register == null || response.hasErrors()) {
                    makeSnackbar("${response.errors?.get(0)?.message}")
                    binding.createAccountFragmentProgressbar.visibility = View.GONE
                } else {
                    binding.createAccountFragmentProgressbar.visibility = View.GONE
                    makeSnackbar("Registration Successful")
                    SharedPreferenceManager(requireContext()).saveToken(register.token)
                    val destination =
                        CreateAccountFragmentDirections.actionCreateAccountFragmentToVerifyAccountFragment(
                            email
                        )
                    findNavController().navigate(destination)
                }
            }
        }
    }

    // Function to validate user input
    private fun validateUser(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            makeSnackbar("Please input a valid email")
            return false
        }
        if (password.length < 7) {
            makeSnackbar("Password lengths must be greater than 6")
            return false
        }
        return true
    }

}