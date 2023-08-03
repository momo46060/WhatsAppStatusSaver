package com.bomb.app.whatsappstatussaver.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.databinding.FragmentIntroBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : Fragment() {
    private var REQUEST_PERMISSION = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    lateinit var dialog: BottomSheetDialog
    lateinit var binding: FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_intro, container, false)



        return binding.root
    }

    private fun bottomSheet() {
        val vie: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(vie)
        dialog.setCancelable(false)
        val b = vie.findViewById<Button>(R.id.i)
        b.setOnClickListener {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_PERMISSION)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.navView.visibility = View.GONE
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            bottomSheet()
        } else {
            Handler().postDelayed({
                (activity as MainActivity).binding.navView.selectedItemId = R.id.downloadFragment
            }, 1500)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    (activity as MainActivity).binding.navView.selectedItemId = R.id.downloadFragment
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    onStart()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }


}