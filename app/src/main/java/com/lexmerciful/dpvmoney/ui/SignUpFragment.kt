package com.lexmerciful.dpvmoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.Resource
import com.lexmerciful.dpvmoney.databinding.FragmentSignUpBinding
import com.lexmerciful.dpvmoney.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupSignInText()

        observeSignUpLiveData()

        setupForm()
    }

    private fun setupSignInText() {
        binding.signInTextView.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment) }
    }

    private fun setupSignUp() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditSignUp.text.toString()
        val password = binding.passEditSignUp.text.toString()
        authViewModel.signUpUser(name, email, password)
    }

    private fun observeSignUpLiveData() {
        authViewModel.signupLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                    showLoading("Loading, Please wait...")
                }
                is Resource.Success -> {
                    hideLoading()
                    (activity as MainActivity).showSnackBar(binding, "Sign Up Successful")
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                is Resource.Failure -> {
                    hideLoading()
                    resource.exception.let { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage.message, Toast.LENGTH_LONG).show()
                    }
                }
                null -> {

                }
            }
        }
    }

    private fun showLoading(message: String?) {
        // hideBaseToolbar()
        binding.progressInclude.progress.visibility = View.VISIBLE
        if (message !== null) {
            binding.progressInclude.progressTextView.text = message
        } else {
            binding.progressInclude.progressTextView.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.progressInclude.progress.visibility = View.GONE
    }

    private fun setupForm() {
        form {
            useRealTimeValidation(disableSubmit = true)
            input(binding.nameEditText) {
                isNotEmpty()
                length().atLeast(3)
            }
            input(binding.emailEditSignUp) {
                isNotEmpty()
                isEmail()
            }
            input(binding.passEditSignUp) {
                length().atLeast(6)
            }

            submitWith(binding.signUpBtn) {
                setupSignUp()
            }
        }
    }

}