package ru.vvs.terminal1.screens.ordersFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.OrdersRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.Order

class OrdersViewModel(application: Application): AndroidViewModel(application) {

    private val repository: OrdersRepository //= RetrofitRepository()

    private var _myOrdersList: MutableLiveData<List<Order>> = MutableLiveData()
    val myOrdersList: LiveData<List<Order>> = _myOrdersList

    private val _isProgress = MutableLiveData<Boolean>(false)
    val isProgress: LiveData<Boolean> = _isProgress

    var order: MutableLiveData<Order> = MutableLiveData()

    init {
        val ordersDao = CartsDatabase.getInstance(application).getOrdersDao()
        repository = OrdersRepository(ordersDao)
    }

    fun getOrders(newList: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isProgress.postValue(true)
            _myOrdersList.postValue(repository.getOrders(newList))
            _isProgress.postValue(false)
        }
    }

    fun newOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            order.postValue(repository.newOrder())
        }
    }

}