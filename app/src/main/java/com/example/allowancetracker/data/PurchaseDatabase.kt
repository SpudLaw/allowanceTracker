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

@Database(entities = [Purchase::class], version = 4)
@TypeConverters(Converters::class)
abstract class PurchaseDatabase : RoomDatabase() {

    abstract fun purchaseDao(): PurchaseDao

    companion object {
        @Volatile
        private var INSTANCE: PurchaseDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            //Adds type field to Purchase object
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE purchase_table ADD COLUMN type INTEGER DEFAULT 0 NOT NULL")

                //Fill the new 'type' column based on the name of the purchase
                database.execSQL(
                    "UPDATE purchase_table " +
                            "SET type=${TransactionType.Deposit.ordinal} " +
                            "WHERE description LIKE '-- Increase Balance --'"
                )
                database
                    .execSQL(
                        "UPDATE purchase_table " +
                                "SET type=${TransactionType.Purchase.ordinal} " +
                                "WHERE description NOT LIKE '-- Increase Balance --'"
                    )
                //Add an initial deposit based on the old initial deposit of $400
                database
                    .execSQL(
                        "INSERT INTO purchase_table (cost, date, description, type) " +
                                "VALUES (400.00, 1636511460000, 'Initial Deposit', ${TransactionType.InitialDeposit.ordinal})"
                    )

            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            // Infers purchase dates for missing purchases
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE purchase_table SET date= 1636511460000 + id WHERE type != ${TransactionType.InitialDeposit.ordinal}")
                database.execSQL("UPDATE purchase_table SET date= 1636511460000 WHERE type == ${TransactionType.InitialDeposit.ordinal}")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            // Updates TransactionType enum from int to string
            // Also makes 'date' non-null
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `purchase_table_new` (`cost` REAL NOT NULL, `date` NOT NULL, `description` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transaction_type` TEXT NOT NULL)")

                database.execSQL("INSERT INTO purchase_table_new (cost, date, description, id, transaction_type) SELECT cost, date, description, id, '${TransactionType.InitialDeposit.serializedName}' FROM purchase_table WHERE purchase_table.type = 0")
                database.execSQL("INSERT INTO purchase_table_new (cost, date, description, id, transaction_type) SELECT cost, date, description, id, '${TransactionType.Purchase.serializedName}' FROM purchase_table WHERE purchase_table.type = 1")
                database.execSQL("INSERT INTO purchase_table_new (cost, date, description, id, transaction_type) SELECT cost, date, description, id, '${TransactionType.Deposit.serializedName}' FROM purchase_table WHERE purchase_table.type = 2")

                database.execSQL("DROP TABLE purchase_table");
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE purchase_table_new RENAME TO purchase_table");
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
                    .addMigrations(MIGRATION_3_4)
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