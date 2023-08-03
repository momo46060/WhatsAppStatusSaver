package com.bomb.app.whatsappstatussaver.ui

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.adapters.AdapterFunImp
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.databinding.FragmentShowVideoBinding
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.Segmenter
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions.SINGLE_IMAGE_MODE
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.nio.ByteBuffer
import javax.inject.Inject


@AndroidEntryPoint
class ShowVideoFragment : Fragment() {


    private lateinit var binding: FragmentShowVideoBinding
    private lateinit var path: String
    private lateinit var state: String
    private var boolean: Boolean = false

    @Inject
    lateinit var adsimpl: Adsimpl

    @Inject
    lateinit var funs: AdapterFunImp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_video, container, false)
        arguments?.let {
            path = it.getString("path", "")
            boolean = it.getBoolean("boolen")
        }
        binding.adView.loadAd(adsimpl.preperad())
        val activity = (requireActivity() as MainActivity)
        activity.binding.navView.visibility = GONE
        activity.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        if (path.last() == 'g') {
            state = "img"
            binding.play.visibility = GONE
            Glide.with(requireActivity()).load(path).into(binding.img)
        } else if (path.last() == '4') {
            state = "vid"
            Glide.with(requireActivity()).load(path).into(binding.img)
        }

        binding.play.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(path), "video/mp4")
            startActivity(intent)
        }
        binding.img.setOnClickListener {
            if (state == "vid") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(path), "video/mp4")
                startActivity(intent)
            }

        }

        return binding.root
    }

    private fun alertDialog() {
        AlertDialog.Builder(this.activity)
            .setMessage(resources.getString(R.string.delete_msg))
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
//                File(path).delete()
                val myFile = File(path)
                if (myFile.exists()) {
                    myFile.delete()
                    Log.d(TAG, "alertDialog: zamalekzamalek ${myFile.delete()}")

                }
                adsimpl.showad()
                requireActivity().onBackPressed();


            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(resources.getString(R.string.No)) { i, _ ->
                i.dismiss()
            }
            .show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_main, menu)
        if (boolean) {
            menu.removeItem(R.id.bu_save)
        } else {
            menu.removeItem(R.id.bu_delete)
            menu.removeItem(R.id.bu_share)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bu_save -> {
                if (state == "img") {
                    funs.saveImageInQ(binding.img.drawToBitmap())
                    adsimpl.showad()

                } else {
                    funs.saveVideo(path)
                    adsimpl.showad()
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.bu_share -> {
               funs.share(path)
            }
            R.id.bu_delete -> {
                alertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }



  private fun Perfoamauto(bitmapFromContentUri:Bitmap)
    {
        val client: Segmenter = Segmentation.getClient(SelfieSegmenterOptions.Builder().setDetectorMode(SINGLE_IMAGE_MODE).build())
        client.process(InputImage.fromBitmap(bitmapFromContentUri, 0))
            .addOnSuccessListener { segmentationMask ->
                val buffer: ByteBuffer = segmentationMask!!.buffer
                val width: Int = segmentationMask.width
                val height: Int = segmentationMask.height
                val createBitmap = Bitmap.createBitmap(
                    bitmapFromContentUri.width,
                    bitmapFromContentUri.height,
                    bitmapFromContentUri.config
                )
                for (i in 0 until height) {
                    for (i2 in 0 until width) {
                        val d = buffer.float.toDouble()
                        java.lang.Double.isNaN(d)
                        createBitmap.setPixel(
                            i2,
                            i,
                            Color.argb(((1.0 - d) * 255.0).toInt(), 0, 0, 0)
                        )
                    }
                }
                buffer.rewind()
                var autoeraseimage = overlay(bitmapFromContentUri, createBitmap)
                if (autoeraseimage != null) {
                    // Now set your auto eraseimagebitmap to your imageview
                    binding.img.setImageBitmap(autoeraseimage)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "resources.getString(R.string.please_try_again)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    fun overlay(bmp1: Bitmap, bmp2: Bitmap): Bitmap? {
        val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(bmp1, Matrix(), null)
        canvas.drawBitmap(bmp2, 0f, 0f, null)
        bmp1.recycle()
        bmp2.recycle()
        return bmOverlay
    }
}