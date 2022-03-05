package com.dhandyjoe.stockku.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.dhandyjoe.stockku.ui.activity.AddItemActivity
import com.dhandyjoe.stockku.ui.activity.EditItemActivity
import com.dhandyjoe.stockku.adapter.ItemAdapter
import com.dhandyjoe.stockku.databinding.FragmentItemBinding
import com.dhandyjoe.stockku.model.Item
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentItemBinding
    private lateinit var thisContext: Context
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val listItemSearch = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        thisContext = container!!.context
        binding = FragmentItemBinding.inflate(inflater, container, false)

        binding.favAddTransaction.setOnClickListener {
            val intent = Intent(activity, AddItemActivity::class.java)
            startActivity(intent)
        }

        getBarangList()

        return binding.root
    }

    private fun getBarangList() {
        val doc = firebaseDB.collection("barang")
        doc.addSnapshotListener { snapshot, _ ->
            val user = ArrayList<Item>()

            for(docItem in snapshot!!) {
                user.add(docItem.toObject(Item::class.java))
            }

            if (user.size > 0) {
                showRecycleView(user)
            } else {
                binding.animationView.visibility = View.VISIBLE
                binding.rvListItem.visibility = View.GONE
            }

            searchItem(user)

        }
    }

    private fun showRecycleView(data: ArrayList<Item>) {
        binding.animationView.visibility = View.GONE
        binding.rvListItem.layoutManager = GridLayoutManager(context, 2)
        val data = ItemAdapter(data, thisContext)
        binding.rvListItem.adapter = data
        binding.rvListItem.visibility = View.VISIBLE

        data.setOnItemClickCallback(object : ItemAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Item) {
                val intent = Intent(activity, EditItemActivity::class.java)
                intent.putExtra(EditItemActivity.EXTRA_BARANG, data)
                startActivity(intent)
            }
        })
    }

    private fun searchItem(data: ArrayList<Item>) {
        binding.svItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                listItemSearch.clear()
                data.forEach {
                    if (it.name.lowercase().contains(query!!.lowercase())) {
                        listItemSearch.add(it)
                        showRecycleView(listItemSearch)
                    } else if (listItemSearch.isEmpty()) {
                        showRecycleView(listItemSearch)
                        binding.animationView.visibility = View.VISIBLE
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listItemSearch.clear()
                data.forEach {
                    if (it.name.lowercase().contains(newText!!.lowercase())) {
                        listItemSearch.add(it)
                        showRecycleView(listItemSearch)
                    } else if (listItemSearch.isEmpty()) {
                        showRecycleView(listItemSearch)
                        binding.animationView.visibility = View.VISIBLE
                    }
                }
                return false
            }
        })
    }
}