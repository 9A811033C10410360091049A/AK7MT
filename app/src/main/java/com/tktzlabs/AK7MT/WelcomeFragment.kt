package com.tktzlabs.AK7MT

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tktzlabs.AK7MT.PreferenceHelper.apiUrl
import com.tktzlabs.AK7MT.databinding.FragmentWelcomeBinding


/*
* TODO
* refaktoruj jmena fraagmentu - welcomefragment z firstfragmetn, tradingfragment ze secondfragment
* do trading fragmentu zobrazit cenu BTC a dole dve tlacitko pod tim pro buy and sell
* pod tim adapter s obchody
*   - jednoducha logika ... nemuze prodat kdyz nic nemas, kupovat muzes neomezene, prodat pak muzes jen celkove mnozsti .... tzn udelat asi sqlite adapter
*
* */

class WelcomeFragment : Fragment() {
    private val customPreference = "User_data"
    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check if API URL is set
        val settings = PreferenceHelper.customPreference(this.activity!!, customPreference)
        val apiUrl: String = settings.apiUrl.toString()

        if (apiUrl.isEmpty()) {
            view.findViewById<TextView>(R.id.textview_first).text = getString(R.string.welcome_settings)
            view.findViewById<Button>(R.id.button_trading).visibility = View.GONE
            view.findViewById<Button>(R.id.button_settings).visibility = View.VISIBLE
        } else {
            view.findViewById<TextView>(R.id.textview_first).text = getString(R.string.welcome_settings_set)
            view.findViewById<Button>(R.id.button_trading).visibility = View.VISIBLE
            view.findViewById<Button>(R.id.button_settings).visibility = View.GONE
        }

        binding.buttonTrading.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeFragment_to_TradingFragment)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeFragment_to_SettingsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}