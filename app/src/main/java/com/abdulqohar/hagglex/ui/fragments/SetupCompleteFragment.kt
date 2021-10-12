package com.abdulqohar.hagglex.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdulqohar.hagglex.R
import com.abdulqohar.hagglex.databinding.FragmentSetupCompleteBinding
import com.abdulqohar.hagglex.ui.activities.DashbordActivity


class SetupCompleteFragment : Fragment() {
    // Binding variables set up
    private var _binding: FragmentSetupCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetupCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setting start exploring button on click listener to start dashboard activity
        binding.setupCompleteFragmentStartExploringButton.setOnClickListener {
            val intent = Intent(requireContext(), DashbordActivity::class.java)
            startActivity(intent)
            this.activity?.finish()
        }
    }
}