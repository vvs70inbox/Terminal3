package ru.vvs.terminal1.screens.salesFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.OrdersRepository
import ru.vvs.terminal1.data.SalesRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.Sale

class SalesViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SalesRepository //= RetrofitRepository()

    private var _mySalesList: MutableLiveData<List<Sale>> = MutableLiveData()
    val mySalesList: LiveData<List<Sale>> = _mySalesList

    private val _isProgress = MutableLiveData<Boolean>(false)
    val isProgress: LiveData<Boolean> = _isProgress

    init {
        val salesDao = CartsDatabase.getInstance(application).getSalesDao()
        repository = SalesRepository(salesDao)
    }

    fun getSales(newList: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isProgress.postValue(true)
            _mySalesList.postValue(repository.getSales(newList))
            _isProgress.postValue(false)
        }
    }

}