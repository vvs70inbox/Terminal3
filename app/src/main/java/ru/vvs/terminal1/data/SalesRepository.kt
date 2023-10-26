package ru.vvs.terminal1.data

import android.icu.util.Calendar
import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import ru.vvs.terminal1.data.room.dao.OrdersDao
import ru.vvs.terminal1.data.room.dao.SalesDao
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.Sale
import java.time.LocalDate

class SalesRepository(private val salesDao: SalesDao) {

    suspend fun getSales(newList: Boolean): List<Sale> {
        return salesDao.getAllSales()
    }

    suspend fun newSale() : Sale {
        val count = salesDao.getCount()+1
        val sale = Sale(count, "Продажа", count.toString(), LocalDate.now().toString(), 0, 0, 0)

        salesDao.insert(sale)
        return sale
    }

    suspend fun deleteSale(sale: Sale) {
        salesDao.delete(sale)
    }
}