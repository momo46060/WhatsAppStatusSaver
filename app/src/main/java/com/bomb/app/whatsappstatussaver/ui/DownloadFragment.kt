package com.bomb.app.whatsappstatussaver.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.adapters.WhatsAdapter
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.databinding.FragmentDownloadBinding
import com.google.android.gms.ads.AdRequest
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : Fragment() {
    lateinit var binding: FragmentDownloadBinding
    private lateinit var viewModel: GalleryViewModel
   @Inject lateinit var adpter: WhatsAdapter
   @Inject lateinit var adsimpl: Adsimpl
    private val SHARED_PREFS = "sharedPrefss"
    private val KEY: String = "myKey2"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        binding.adView.loadAd(adsimpl.preperad())
        viewModel= ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
        viewModel.whatsapp()
        binding.re.apply {
            hasFixedSize()
            layoutManager= StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL)
            adapter=adpter
        }
        setData()
        return binding.root
    }

 @RequiresApi(Build.VERSION_CODES.N)
 @SuppressLint("WrongConstant")
 fun x9(){
     try {
         val createOpenDocumentTreeIntent =
             if (Build.VERSION.SDK_INT >= 30) {
                 (requireActivity().getSystemService("storage") as StorageManager?)!!
                     .primaryStorageVolume.createOpenDocumentTreeIntent()
             } else {
                 TODO("VERSION.SDK_INT < Q")
             }
            createOpenDocumentTreeIntent.flags=FLAG_GRANT_READ_URI_PERMISSION
            createOpenDocumentTreeIntent.flags=FLAG_GRANT_WRITE_URI_PERMISSION
            createOpenDocumentTreeIntent.putExtra(
             "android.provider.extra.INITIAL_URI",
                 "/storage/emulated/0/Android/media/com.WhatsApp/WhatsApp/Media/.Statuses/".toUri())
         startActivityForResult(createOpenDocumentTreeIntent, 500)

     } catch (unused: Exception) { }
   /*   val SELECT_TREE_FOLDER: Int = 500
     val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
     intent.putExtra("android.provider.extra.INITIAL_URI",
                 "/storage/emulated/0/Android/media/com.WhatsApp/WhatsApp/Media/.Statuses/".toUri())
     startActivityForResult(intent, SELECT_TREE_FOLDER)*/
 }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 500) {
//            if (data != null) {
//                val Imagelist = ArrayList<String>()
//                val uriTree: Uri? = data.data
//                Log.d(TAG, "onActivityResult: $uriTree")
//                saveData(requireContext(),uriTree.toString())
//                val pickedDir = DocumentFile.fromTreeUri(requireContext(), uriTree!!)
//                for (file in pickedDir!!.listFiles()) {
//                    Imagelist.add(file.uri.toString())
//                }
//                adpter.setlist(Imagelist)
//
//            }
            val treeUri = data?.data ?: return
            if (DocumentFile.fromTreeUri(requireContext(), treeUri)?.canWrite() == true) {
                var takeFlags = data.flags
                takeFlags =
                    takeFlags and (FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                requireContext().contentResolver.takePersistableUriPermission(treeUri, takeFlags)
                saveData(requireContext(),treeUri.toString())
                setData()
            }
        }else if (requestCode==1001){
            Toast.makeText(requireContext(), "cbkv bfbviu", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveData(context: Context, text:String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY, text)
        editor.apply()
    }

    private fun loadData(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY, "")
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private  fun setData(){
        if (loadData(requireContext()).isNullOrBlank()){
            x9()
        }else{
            val Imagelist = ArrayList<String>()
            val uriTree=loadData(requireContext())
            val uri = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(KEY,"")!!.toUri()
            val xx= DocumentFile.fromTreeUri(requireContext(), uri)?.canWrite()
            val pickedDir = DocumentFile.fromTreeUri(requireContext(), uriTree!!.toUri())
            for (file in pickedDir!!.listFiles()) {
                Imagelist.add(file.uri.toString())
            }
            if (Imagelist.isEmpty()){
                Toast.makeText(requireContext(), "No status", Toast.LENGTH_SHORT).show()
            }else{
                adpter.setlist(Imagelist)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.navView.visibility = View.VISIBLE

    }
}