package com.tktzlabs.AK7MT

import SqliteDatabase
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tktzlabs.AK7MT.adapter.TradesAdapter
import com.tktzlabs.AK7MT.db.Trades
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class TradingActivity : AppCompatActivity() {

    private lateinit var dataBase: SqliteDatabase

    private var isActivityActive: Boolean = true
    private var lastPrice: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        setContentView(R.layout.activity_trading)

        val tradeView: RecyclerView = findViewById(R.id.myTradeList)
        val btnReset: Button = findViewById(R.id.btnReset)
        val btnBuy: Button = findViewById(R.id.btnBuy)
        val btnSell: Button = findViewById(R.id.btnSell)
        val twPrice: TextView = findViewById(R.id.textViewPrice)

        val linearLayoutManager = LinearLayoutManager(this)
        tradeView.layoutManager = linearLayoutManager
        tradeView.setHasFixedSize(true)

        dataBase = SqliteDatabase(this@TradingActivity)

        // PRICE RELOADER
        val handler = Handler()
        val r: Runnable = object : Runnable {
            override fun run() {
                handler.postDelayed(this, 3000)
                if (isActivityActive) {
                    val currentPrice: Float = getCurrentPrice().toFloat()
                    if (currentPrice > lastPrice.toFloat())
                        twPrice.setTextColor(Color.GREEN)
                    else
                        twPrice.setTextColor(Color.RED)
                    twPrice.text = "$currentPrice USDT"
                    lastPrice = currentPrice.toString()
                }
            }
        }
        handler.postDelayed(r, 0)

        // INITIAL CHECK
        if (dataBase.getTradesCount() > 0) {
            tradeView.visibility = View.VISIBLE
            val mAdapter = TradesAdapter(this, dataBase.listTrades())
            tradeView.adapter = mAdapter
            Log.d("TradingActivity", "We have some Trades in DB")
        } else {
            tradeView.visibility = View.GONE
            Toast.makeText(this, "There are no trades in the database. Start trading now", Toast.LENGTH_LONG).show()
            Log.d("TradingActivity", "We DON'T have any Trades in DB")
        }

        // RESET, BUY and SELL BUTTONs
        btnReset.setOnClickListener {
            resetButton()
        }

        btnBuy.setOnClickListener {
            buyButton()
        }

        btnSell.setOnClickListener {
            sellButton()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun resetButton() {
        dataBase.resetDatabase()
        reloadActivity()
    }

    private fun buyButton() {
        if (performBuy()) {
            Log.d("TradingActivity", "performBuy() is TRUE")
            Toast.makeText(this, "Bought 1 BTC", Toast.LENGTH_LONG).show()
            reloadActivity()
        } else {
            Log.d("TradingActivity", "performBuy() FALSE")
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun sellButton() {
        // check if we have any BUY trade in DB
        if (dataBase.getBuyTradesCount() > 0) {
            dataBase.deleteTrade("BUY")
            val price: String = getCurrentPrice()
            Toast.makeText(this, "Sold 1 BTC for $price", Toast.LENGTH_LONG).show()
            reloadActivity()
        } else {
            Toast.makeText(this, "NOTHING TO DO", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentPrice(): String {
        var price = ""

        val job = GlobalScope.launch {
            val currentPrice = RetrofitHelper.getBtcPrice()
            if (currentPrice != "") {
                Log.d("TradingActivity: ", "Current BTC price is $currentPrice")
                price = currentPrice
            }
        }
        runBlocking {
            job.join() // wait until child coroutine completes
        }

        return price
    }

    private fun performBuy(): Boolean {
        Log.d("TradingActivity", "Buy simulation")
        var response: Boolean = false
        val price: String = getCurrentPrice()
        if (price.isNotEmpty()) {
            dataBase.addTrades(Trades("BUY", "1", price))
            response = true
        }

        return response
    }

    private fun reloadActivity() {
        isActivityActive = false
        finish()
        startActivity(intent)
    }
}