package com.example.allowancetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Purchase::class], version = 3)
@TypeConverters(Converters::class)
abstract class PurchaseDatabase : RoomDatabase() {

    abstract fun purchaseDao(): PurchaseDao

    companion object {
        @Volatile
        private var INSTANCE: PurchaseDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE purchase_table ADD COLUMN type INTEGER DEFAULT 0 NOT NULL")

                //Fill the new 'type' column based on the name of the purchase
                database.execSQL(
                    "UPDATE purchase_table " +
                        "SET type=${PurchaseType.Deposit.ordinal} " +
                        "WHERE description LIKE '-- Increase Balance --'")
                database
                    .execSQL(
                        "UPDATE purchase_table " +
                            "SET type=${PurchaseType.Purchase.ordinal} " +
                            "WHERE description NOT LIKE '-- Increase Balance --'")
                //Add an initial deposit based on the old initial deposit of $400
                database
                    .execSQL(
                        "INSERT INTO purchase_table (cost, date, description, type) " +
                            "VALUES (400.00, 1636511460000, 'Initial Deposit', ${PurchaseType.InitialDeposit.ordinal})")

            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE purchase_table SET date= 1636511460000 + id WHERE type != ${PurchaseType.InitialDeposit.ordinal}")
                database.execSQL("UPDATE purchase_table SET date= 1636511460000 WHERE type == ${PurchaseType.InitialDeposit.ordinal}")
            }
        }
        fun getDatabase(context: Context, scope: CoroutineScope): PurchaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PurchaseDatabase::class.java,
                    "purchase-database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addCallback(PurchaseDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class PurchaseDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.purchaseDao())
                }
            }
        }

        suspend fun populateDatabase(purchaseDao: PurchaseDao) {
            purchaseDao.deleteAll()

//            val purchase = Purchase(0, 25.0, null, "video game")
//            purchaseDao.add(purchase)
        }
    }
}