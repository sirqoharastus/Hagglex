package com.abdulqohar.hagglex.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdulqohar.hagglex.R
import com.abdulqohar.hagglex.apollo.apolloClient
import com.abdulqohar.hagglex.databinding.FragmentVerifyAccountBinding
import com.abdulqohar.hagglex.graphql.ResendVerificationCodeQuery
import com.abdulqohar.hagglex.graphql.VerifyUserMutation
import com.abdulqohar.hagglex.graphql.type.EmailInput
import com.abdulqohar.hagglex.graphql.type.VerifyUserInput
import com.abdulqohar.hagglex.utils.makeSnackbar
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.launch


class VerifyAccountFragment : Fragment() {
    //Binding variables and nav args set up
    private var _binding: FragmentVerifyAccountBinding? = null
    private val binding get() = _binding!!
    private val navArgs by navArgs<VerifyAccountFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.verifyUserFragmentToolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        binding.verifyUserFragmentVerifyMeButton.setOnClickListener {
            verifyUser(Integer.parseInt(binding.verifyUserFragmentVerificationCodeEditText.text.toString()))
        }

        binding.verifyUserFragmentResendCodeTextView.setOnClickListener {
            resendVerificationCode(navArgs.email)
        }
    }
    // Function to resend verification code
    private fun resendVerificationCode(email: String) {
        binding.verifyUserFragmentResendCodeProgressbar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val response = try {
                apolloClient(requireContext()).query(ResendVerificationCodeQuery(email = Input.fromNullable(EmailInput(email)))).await()
            }
            catch (e:java.lang.Exception){
                null
            }

            val result = response?.data

            if (result == null || response.hasErrors()){
                binding.verifyUserFragmentResendCodeProgressbar.visibility = View.GONE
                makeSnackbar("${response?.errors?.get(0)?.message}")
            }
            else {
                binding.verifyUserFragmentResendCodeProgressbar.visibility = View.GONE
                makeSnackbar("Verification code sent")
            }
        }
    }
    // Function to verify user
    private fun verifyUser(verificationPin: Int) {
        if (binding.verifyUserFragmentVerificationCodeEditText.text.isEmpty()){
            makeSnackbar("Fields cannot be empty")
            return
        }
        binding.verifyUserFragmentProgressbar.visibility = View.VISIBLE
        val pin = VerifyUserInput(verificationPin)
        lifecycleScope.launch {
            val response = try {
                apolloClient(requireContext()).mutate(VerifyUserMutation(Input.fromNullable(pin))).await()
            }
            catch (e: Exception){
                null
            }
            val result = response?.data?.verifyUser

            if (result == null || response.hasErrors()){
                binding.verifyUserFragmentProgressbar.visibility = View.GONE
                response?.errors?.get(0)?.message?.let { makeSnackbar(it) }
            }
            else{
                binding.verifyUserFragmentProgressbar.visibility = View.GONE
                makeSnackbar("Account Verified")
                findNavController().navigate(R.id.action_verifyAccountFragment_to_setupCompleteFragment)
            }
        }
    }
}