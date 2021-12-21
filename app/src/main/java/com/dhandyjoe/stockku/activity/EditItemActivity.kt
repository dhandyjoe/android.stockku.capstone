package com.dhandyjoe.stockku.activity

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dhandyjoe.stockku.R
import com.dhandyjoe.stockku.databinding.ActivityEditItemBinding
import com.dhandyjoe.stockku.model.Item
import com.dhandyjoe.stockku.util.DATA_IMAGES
import com.dhandyjoe.stockku.util.Database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class EditItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditItemBinding
    private var firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Edit barang"

        val data = intent.getParcelableExtra<Item>(EXTRA_BARANG)


        if (data?.imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(R.drawable.empty_image)
                .into(binding.ivAddImageItem)
        } else {
            Glide.with(this)
                .load(data?.imageUrl)
                .into(binding.ivAddImageItem)
        }

        binding.etEditNameItem.setText(data?.name)
        binding.etEditSizeItem.setText(data?.size)
        binding.etEditPriceItem.setText(data?.price)

        binding.btnUpdate.setOnClickListener {
            updateItem(data!!)
            finish()
        }

        binding.btnDelete.setOnClickListener {
            deleteItem(data!!)
        }
    }

//    private fun userInfo(item: Item) {
//        firebaseDB.collection("barang")
//            .document(item.id)
//            .get()
//            .addOnSuccessListener {
//                val user = it.toObject(User::class.java)
//                imageUrl = user?.imageUrl
//                binding.etNameProfile.setText(user?.name)
//                binding.etEmailProfile.setText(user?.email)
//                binding.etPhoneProfile.setText(user?.phone)
//                if (imageUrl != null) {
//                    userImage(this, user?.imageUrl!!, binding.ivPhotoProfile)
//                }
//            }
//    }

    fun updateItem(item: Item) {
        val nameItem = binding.etEditNameItem.text.toString()
        val sizeItem = binding.etEditSizeItem.text.toString()
        val priceItem = binding.etEditPriceItem.text.toString()
        val addStockItem = binding.etEditStockItem.text.toString()

        database.editItem(item.id, nameItem, priceItem, sizeItem, addStockItem)
    }

    fun deleteItem(item: Item) {
        AlertDialog.Builder(this)
            .setTitle("Hapus barang")
            .setMessage("Ini akan menghapus barang anda. Apakah anda yakin?")
            .setPositiveButton("Ya") { dialog, which ->
                Toast.makeText(this, "Barang dihapus", Toast.LENGTH_SHORT).show()
                database.deleteItem(item.id)
                finish()
            }
            .setNegativeButton("Tidak") {dialog, which -> }
            .show()
    }

    companion object {
        const val EXTRA_BARANG = "extra_barang"
    }
}