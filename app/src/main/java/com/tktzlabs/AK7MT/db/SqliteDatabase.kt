import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tktzlabs.AK7MT.db.Trades
import java.util.*


/*
* ID of record
* TYPE of trade SELL / BUY
* AMOUNT is default 1 BTC
* PRICE is current price from endpoint
* */

class SqliteDatabase internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTradesTable = ("CREATE TABLE "
                + TABLE_TRADES + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_AMOUNT + " TEXT,"
                + COLUMN_PRICE + " TEXT" + ")")
        db.execSQL(createTradesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRADES")
        onCreate(db)
    }

    fun resetDatabase() {
        val db = this.readableDatabase
        db.delete(TABLE_TRADES, null, null);
    }

    fun getTradesCount(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $TABLE_TRADES", null).toInt()
    }

    fun getBuyTradesCount(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $TABLE_TRADES WHERE $COLUMN_TYPE = 'BUY' ", null).toInt()
    }

    fun listTrades(): ArrayList<Trades> {
        val sql = "select * from $TABLE_TRADES"
        val db = this.readableDatabase
        val storeTrades = ArrayList<Trades>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0).toInt()
                val type = cursor.getString(1)
                val amount = cursor.getString(2)
                val price = cursor.getString(3)
                storeTrades.add(Trades(id, type, amount, price))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return storeTrades
    }

    fun addTrades(trades: Trades) {
        val values = ContentValues()
        values.put(COLUMN_TYPE, trades.type)
        values.put(COLUMN_AMOUNT, trades.amount)
        values.put(COLUMN_PRICE, trades.price)
        val db = this.writableDatabase
        db.insert(TABLE_TRADES, null, values)
    }

    fun updateTrades(trades: Trades) {
        val values = ContentValues()
        values.put(COLUMN_TYPE, trades.type)
        values.put(COLUMN_AMOUNT, trades.amount)
        values.put(COLUMN_PRICE, trades.price)
        val db = this.writableDatabase
        db.update(TABLE_TRADES, values, "$COLUMN_ID = ?", arrayOf(trades.id.toString()))
    }

    fun deleteTrade(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TRADES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteTrade(type: String) {
        val db = this.writableDatabase
        db.delete(TABLE_TRADES, "$COLUMN_TYPE = ?", arrayOf(type)) // TODO delete only one record - maybe limit
    }

    companion object {
        private const val DATABASE_VERSION = 6
        private const val DATABASE_NAME = "Trades"
        private const val TABLE_TRADES = "Trades"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TYPE = "tradeType"
        private const val COLUMN_AMOUNT = "tradeAmount"
        private const val COLUMN_PRICE = "tradePrice"
    }
}