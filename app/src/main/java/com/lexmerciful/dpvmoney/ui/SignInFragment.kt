package com.lexmerciful.dpvmoney.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.Resource
import com.lexmerciful.dpvmoney.databinding.FragmentSignInBinding
import com.lexmerciful.dpvmoney.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupSignUpText()

        observeSignInLiveData()

        setupForm()
    }

    private fun setupSignUpText() {
        binding.signUpText.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun setupSignIn() {
        val email = binding.emailSignInEditText.text.toString()
        val password = binding.passwordSignInEditText.text.toString()
        authViewModel.signInUser(email, password)
    }

    private fun observeSignInLiveData() {
        authViewModel.loginLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                    showLoading("Loading, Please wait...")
                }
                is Resource.Success -> {
                    hideLoading()
                    //(activity as MainActivity).showSnackBar(binding, "Sign In Successful")
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
            input(binding.emailSignInEditText) {
                isNotEmpty()
                isEmail()
            }
            input(binding.passwordSignInEditText) {
                length().atLeast(6)
            }

            submitWith(binding.signInBtn) {
                setupSignIn()
            }
        }
    }
}