package de.othregensburg.healthnote.fragments.pictureDialog

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentAddBinding
import java.io.File

class AddPictureDialogFragment : DialogFragment() {
    private var _addBinding: FragmentAddBinding? = null
    private val addBinding get() = _addBinding!!
    private var imageSelect = addBinding.selectImage
    private var tempImageUri: Uri? = null
    private val selectedPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageSelect.setImageURI(it)
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageSelect.setImageURI(tempImageUri)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.choosePictureSource)
                .setPositiveButton(R.string.camera
                ) { _, _ ->
                    // Open Camera
                    tempImageUri = FileProvider.getUriForFile(requireContext(), "testProvider", createImageFile())
                }
                .setNegativeButton(R.string.gallery
                ) { _, _ ->
                    // Open Gallery
                    Toast.makeText(requireContext(), "Open gallery", Toast.LENGTH_SHORT).show()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createImageFile(): File {
        return File.createTempFile("temp_image", ".jpg", Environment.DIRECTORY_PICTURES)
    }


}