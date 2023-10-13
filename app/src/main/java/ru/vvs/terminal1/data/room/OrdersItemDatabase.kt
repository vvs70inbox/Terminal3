package ru.vvs.terminal1.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vvs.terminal1.data.room.dao.OrdersItemDao
import ru.vvs.terminal1.model.OrderItem

@Database(entities = [OrderItem::class], version = 1)
abstract class OrdersItemDatabase: RoomDatabase() {

    abstract fun getAllOrdersItem(): OrdersItemDao

    companion object {
        @Volatile
        private var database: OrdersItemDatabase?= null

        fun getInstance(context: Context): OrdersItemDatabase {
            return  if (database == null) {
                database = Room
                    .databaseBuilder(context, OrdersItemDatabase::class.java, "db")
                    .build()
                database as OrdersItemDatabase
            } else {
                database as OrdersItemDatabase
            }
        }
    }
}