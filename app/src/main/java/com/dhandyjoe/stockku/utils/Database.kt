package com.dhandyjoe.stockku.utils

import com.dhandyjoe.stockku.model.Item
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Database {
    private val firebaseDB = FirebaseFirestore.getInstance()

    // add item to firebase
    fun addItem(userId: String, nameItem: String, sizeItem: String, priceItem: Int, imageUrl: String, stockItem: Int) {
        val docBarang = firebaseDB.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_ITEM).document()
        val item = Item(docBarang.id,"",nameItem, sizeItem, priceItem, imageUrl, stockItem)

        docBarang.set(item)
    }

    // edit item firebase
    fun editItem(userId: String, itemId: String, nameItem: String, priceItem: Int, sizeItem: String, stockItem: Int) {
        val map = HashMap<String, Any>()
        map["name"] = nameItem
        map["price"] = priceItem
        map["size"] = sizeItem
        map["stock"] = FieldValue.increment(stockItem.toDouble())

        firebaseDB.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_ITEM)
            .document(itemId)
            .update(map)
    }

    // delete item firebase
    fun deleteItem(userId: String, itemId: String) {
        firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_ITEM).document(itemId)
            .delete()
    }

    // update stock item after transaction
    fun updateStockItem(userId: String, item: Item) {
        firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_ITEM).document(item.id)
            .update("stock", item.stock - item.totalTransaction)
    }

    // add transaction item to firebase
    fun saveTransactionItem(userId: String, data: Item, docTransaction: String) {
        firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_TRANSACTION).document(docTransaction)
            .collection(COLLECTION_TRANSACTION_ITEM).document()
            .set(data)
    }

    // add item to cart
    fun addItemCart(userId: String, data: Item, totalTransaction: Int) {
        val docItemCart = firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_CART).document()
        val item = Item(data.id, docItemCart.id, data.name, data.size, data.price, data.imageUrl, data.stock, totalTransaction)
        docItemCart.set(item)
    }

    // update totalTransaction item in cart
    fun updateItemCart(userId: String, data: Item, statusIndicator: Int) {
        firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_CART).document(data.idCart)
            .update("totalTransaction", data.totalTransaction + statusIndicator)
    }

    // delete item in cart
    fun deleteItemCart(userId: String, data: Item) {
        firebaseDB.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_CART).document(data.idCart)
            .delete()
    }
}