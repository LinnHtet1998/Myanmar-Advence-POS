package com.myanmaradvancedpos.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val customCode: String = "",
    val category: String = "အထွေထွေ",
    val unit: String = "ခု",
    val costPrice: Double = 0.0,
    val sellPrice: Double = 0.0,
    val stockQty: Double = 0.0,
    val photoUri: String? = null
)

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val invoiceNo: String,
    val createdAt: Long = System.currentTimeMillis(),
    val customerName: String = "လက်ငင်းဝယ်သူ",
    val subtotal: Double,
    val discount: Double = 0.0,
    val total: Double,
    val paymentMethod: String = "ငွေသား",
    val paid: Double = 0.0
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "အထွေထွေ",
    val amount: Double,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val receiptPhotoUri: String? = null
)

@Dao
interface PosDao {
    @Query("SELECT * FROM items ORDER BY name") fun observeItems(): kotlinx.coroutines.flow.Flow<List<ItemEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun saveItem(item: ItemEntity): Long
    @Query("SELECT * FROM invoices ORDER BY createdAt DESC") suspend fun allInvoices(): List<InvoiceEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun saveInvoice(invoice: InvoiceEntity): Long
    @Query("SELECT * FROM expenses ORDER BY createdAt DESC") suspend fun allExpenses(): List<ExpenseEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun saveExpense(expense: ExpenseEntity): Long
    @Query("SELECT * FROM items") suspend fun allItems(): List<ItemEntity>
    @Query("DELETE FROM items") suspend fun clearItems()
    @Query("DELETE FROM invoices") suspend fun clearInvoices()
    @Query("DELETE FROM expenses") suspend fun clearExpenses()
}

@Database(entities = [ItemEntity::class, InvoiceEntity::class, ExpenseEntity::class], version = 1, exportSchema = true)
abstract class PosDatabase : RoomDatabase() {
    abstract fun posDao(): PosDao
    companion object {
        @Volatile private var INSTANCE: PosDatabase? = null
        fun get(context: Context): PosDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, PosDatabase::class.java, "myanmar_pos.db").build().also { INSTANCE = it }
        }
    }
}
