package de.othregensburg.healthnote.fragments.login

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.navigation.fragment.findNavController
    import de.othregensburg.healthnote.MainActivity
    import de.othregensburg.healthnote.R
    import de.othregensburg.healthnote.databinding.FragmentBioAuthenticationBinding
    import de.othregensburg.healthnote.viewmodel.SettingsViewModel
    import java.util.concurrent.Executor

class BioAuthFragment : Fragment() {

        private var _binding: FragmentBioAuthenticationBinding? = null
        private val binding get() = _binding!!
        private lateinit var svmodel: SettingsViewModel
        private lateinit var executor: Executor
        private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
        private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentBioAuthenticationBinding.inflate(inflater, container, false)

            svmodel = ViewModelProvider(this)[SettingsViewModel::class.java]
            svmodel.readAllData.observe(viewLifecycleOwner) { settings ->
                val mainAct = activity as MainActivity
                mainAct.prepareSettings(settings, svmodel)
                if (!settings[0].useBiometrics) {
                    findNavController().navigate(R.id.action_bioAuthFragment_to_pinFragment)
                }
                else{
                    if(!settings[0].usePIN) {
                        binding.authUsePinBtn.visibility = View.INVISIBLE
                    }
                    else{
                        binding.authUsePinBtn.visibility = View.VISIBLE
                    }

                    binding.authBtn.setOnClickListener {
                        biometricPrompt.authenticate(promptInfo)
                    }

                    binding.authUsePinBtn.setOnClickListener {
                        findNavController().navigate(R.id.action_bioAuthFragment_to_pinFragment)
                    }

                }
            }

            executor = ContextCompat.getMainExecutor(requireContext())
            biometricPrompt = androidx.biometric.BiometricPrompt(this@BioAuthFragment, executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(context,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        findNavController().navigate(R.id.action_bioAuthFragment_to_listFragment)
                        Toast.makeText(context,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(context, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })


            promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()

            val mainAct = activity as MainActivity
            mainAct.supportActionBar?.setDisplayShowHomeEnabled(false)
            mainAct.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mainAct.supportActionBar?.setHomeButtonEnabled(false)


            return binding.root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    }