package ru.vvs.terminal1.screens.orderViewScreen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.R
import ru.vvs.terminal1.data.retrofit.RetrofitInstance
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.model.Order1C
import ru.vvs.terminal1.model.OrderItem
import ru.vvs.terminal1.model.OrderItemDao

class OrderViewViewModel(application: Application): AndroidViewModel(application) {

    private val orderItemDao: OrderItemDao

    val orderItemList: MutableLiveData<List<OrderItem>> = MutableLiveData();

    init {
        orderItemDao = CartsDatabase.getInstance(application).getOrderItemDao()
    }

    fun getOrderItems(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            orderItemList.postValue(orderItemDao.getById(orderId))
        }
    }

    fun incrementOrderItemAmount(barcode: String) {
        orderItemList.value!!.find { it.barcode == barcode  }?.let {
            it.amount++
            viewModelScope.launch(Dispatchers.IO) {
                orderItemDao.updateOrderItemAmount(it.id, it.barcode, it.amount)
            }
        }
    }

//    fun updateOrder(order: Order) {
//        viewModelScope.launch(Dispatchers.IO) {
//            orderItemDao.updateOrder(order)
//        }
//    }
//
//    private fun getProductByBarcode(barcode: String): ItemsOrder? {
//        return myItemsList.value!!.find { it.Barcode == barcode }
//    }
//
//    fun updateItem(barcode: String, orderId: Int) {
//        val item = getProductByBarcode(barcode)
//        if (item == null) {
//            viewModelScope.launch(Dispatchers.IO) {
//                productList.postValue(orderItemDao.newItem(barcode, orderId))
//                getOrderItems(orderId)
//                productList = MutableLiveData()
//            }
//        } else {
//            viewModelScope.launch(Dispatchers.IO) {
//                productList.postValue(orderItemDao.updateItem(barcode, orderId))
//                getOrderItems(orderId)
//                productList = MutableLiveData()
//            }
//        }
//    }
//
//    fun updateItemCount(itemsOrder: ItemsOrder, orderId: Int, counts: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            orderItemList.value!!.find{ it.Barcode == itemsOrder.Barcode }!!.amount = counts
//            orderItemDao.updateItemCount(itemsOrder, orderId)
//            getOrderItems(orderId)
//        }
//    }
//
    fun deleteItem(position: Int) {
        val orderItem = orderItemList.value!![position]
        orderItemList.postValue(orderItemList.value!!.toMutableList().apply { removeAt(position) })
        viewModelScope.launch(Dispatchers.IO) {
            orderItemDao.deleteOrderItemById(orderItem.id)
        }
    }

    fun createOrderIn1C() {
        if (!MainActivity.isOnline(mainActivity)) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.error_internet), Toast.LENGTH_LONG).show()
        } else {
            val order1C: MutableList<Order1C> = mutableListOf()

            orderItemList.value!!.forEach {
                order1C.add(0, Order1C(it.barcode, it.amount.toString()))
            }

            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val response = RetrofitInstance.api.postOrder(
                        "VVS",
                        "999",
                        order1C
                    ) //RetrofitClient.apiService.createUser(user)

                    if (response.isSuccessful) {
                        Log.d("createOrderIn1C", "Order created")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(mainActivity,
                                mainActivity.getString(R.string.message_order_to_1c), Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Log.e("createOrderIn1C", "Failed to create order.")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                mainActivity,
                                mainActivity.getString(R.string.message_order_not_send_to_1c),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("createOrderIn1C", "Error occurred: ${e.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            mainActivity,
                            "Error occurred: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}