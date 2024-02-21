package ru.vvs.terminal1.screens.productListScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.Product
import ru.vvs.terminal1.model.ProductDao

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao: ProductDao

    val productList: MutableLiveData<List<Product>> = MutableLiveData()
    val isProgress: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var selectedProduct: MutableLiveData<Product?> = MutableLiveData()

    init {
        productDao = CartsDatabase.getInstance(application).getProductDao()
    }

    fun getProducts() {
            viewModelScope.launch(Dispatchers.IO) {
                isProgress.postValue(true)
                productList.postValue(Product.getProducts(productDao))
                isProgress.postValue(false)
            }
    }

    fun updateProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            isProgress.postValue(true)
            productList.postValue(Product.updateProducts(productDao))
            isProgress.postValue(false)
        }
    }

    fun getProductByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedProduct.postValue(productDao.getByBarcode(barcode))
        }

    }
}