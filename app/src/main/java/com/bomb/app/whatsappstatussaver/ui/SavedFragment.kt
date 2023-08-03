package com.bomb.app.whatsappstatussaver.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.adapters.WhatsAdapter
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.databinding.FragmentSavedBinding
import com.google.android.gms.ads.AdRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedFragment : Fragment() {
    private lateinit var viewModel: GalleryViewModel
    @Inject
    lateinit var adpter: WhatsAdapter
    @Inject
    lateinit var adsimpl: Adsimpl
    lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false)
        binding.adView.loadAd(adsimpl.preperad())

        viewModel= ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
        viewModel.saved_whatsapp()
        binding.rv.apply {
            hasFixedSize()
            layoutManager= StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL)
            adapter=adpter
        }
        viewModel.whatsapp.observe(requireActivity()) {
            adpter.setlist(it)
        }
        return binding.root
    }




    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.navView.visibility=View.VISIBLE

    }
}