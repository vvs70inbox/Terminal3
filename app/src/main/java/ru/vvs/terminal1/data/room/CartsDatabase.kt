package ru.vvs.terminal1.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.OrderDao
import ru.vvs.terminal1.model.OrderItem
import ru.vvs.terminal1.model.OrderItemDao
import ru.vvs.terminal1.model.Product
import ru.vvs.terminal1.model.ProductDao
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.TimeZone

@Database(entities = [Product::class, Order::class, OrderItem::class], version = 8)
abstract class CartsDatabase: RoomDatabase() {

    abstract fun getProductDao(): ProductDao
    abstract fun getOrderDao(): OrderDao
    abstract fun getOrderItemDao(): OrderItemDao

    companion object {
        @Volatile
        private var database: CartsDatabase ?= null

        fun getInstance(context: Context): CartsDatabase {
            return  if (database == null) {
                database = Room
                    .databaseBuilder(context, CartsDatabase::class.java, "db_terminal")
                    .addTypeConverter(Converters::class.java)
                    .fallbackToDestructiveMigration()
                    .build()
                database as CartsDatabase
            } else {
                database as CartsDatabase
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochSecond(value), TimeZone.getDefault().toZoneId()) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}