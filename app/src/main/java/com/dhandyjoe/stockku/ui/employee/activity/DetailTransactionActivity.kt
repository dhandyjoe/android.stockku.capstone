package com.dhandyjoe.stockku.ui.employee.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhandyjoe.stockku.adapter.CartAdapter
import com.dhandyjoe.stockku.databinding.ActivityDetailTransactionBinding
import com.dhandyjoe.stockku.model.Transaction
import com.dhandyjoe.stockku.model.Item
import com.dhandyjoe.stockku.utils.COLLECTION_TRANSACTION
import com.google.firebase.firestore.FirebaseFirestore

class DetailTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionBinding
    private val firebaseDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Detail transaksi"

        val data = intent.getParcelableExtra<Transaction>(EXTRA_DATA)

        if (data != null) {
            getItemTransactionList(data.id)
        }
    }

    private fun getItemTransactionList(transaksiId: String) {
        val doc = firebaseDB.collection(COLLECTION_TRANSACTION).document(transaksiId).collection("itemTransaksi")
        doc.get()
            .addOnSuccessListener {
                val user = ArrayList<Item>()
                for(docItem in it) {
                    user.add(docItem.toObject(Item::class.java))
                }
                val data = CartAdapter(user, this)
                showRecycleView(data)
            }
            .addOnFailureListener {

            }
    }


    private fun showRecycleView(adapter: CartAdapter) {
        binding.rvDetailTransaction.layoutManager = LinearLayoutManager(this)
        binding.rvDetailTransaction.adapter = adapter
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}