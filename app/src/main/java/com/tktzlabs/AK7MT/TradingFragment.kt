package com.tktzlabs.AK7MT

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tktzlabs.AK7MT.databinding.FragmentTradingBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TradingFragment : Fragment() {

    private var _binding: FragmentTradingBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_TradingFragment_to_WelcomeFragment)
        }
*/


        // BUY BUTTON
        val buttonBuy = view.findViewById<Button>(R.id.button_buy)
        buttonBuy.setOnClickListener {
            val price = getCurrentPrice()
            if (performBuy(price = price) && price.isNotEmpty()) {
                Log.d("TradingFragment: onViewCreated", "performBuy() is TRUE")
                Snackbar.make(view, "Bought 1 BTC @ $price USD", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                Log.d("TradingFragment: onViewCreated", "performBuy() FALSE")
                Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            // TODO always refresh listview
        }

        // SELL BUTTON
        val buttonSell = view.findViewById<Button>(R.id.button_sell)
        buttonSell.setOnClickListener {
            val price = getCurrentPrice()
            if (performSell(price = price) && price.isNotEmpty()) {
                Log.d("TradingFragment: onViewCreated", "performSell() is TRUE")
                Snackbar.make(view, "Sold 1 BTC @ $price USD", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                Log.d("TradingFragment: onViewCreated", "performSell() FALSE")
                Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            // TODO always refresh listview
        }


    }


    private fun getCurrentPrice(): String {
        var price = ""

        GlobalScope.launch {
            val currentPrice = RetrofitHelper.getBtcPrice()
            if (currentPrice != "") {
                Log.d("TradingFragment: getCurrentPrice: ", "Current BTC price is $currentPrice")
                price = currentPrice
            }
        }

        return price
    }

    private fun performBuy(price: String): Boolean {
        Log.d("TradingFragment: performBuy", "Buy simulation")
        //return db?.buyTrade("BUY", price)!!
        return true
    }

    private fun performSell(price: String): Boolean {
        Log.d("TradingFragment: performSell", "Sell simulation")
        //return db?.sellTrade("SELL", price)!!
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}






