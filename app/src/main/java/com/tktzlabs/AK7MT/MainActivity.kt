package com.tktzlabs.AK7MT

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.tktzlabs.AK7MT.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * zdrojové kódy přístupné v nějakém verzovacím systému (GIT)
 * vlastní ikona aplikace, vlastní splashscreen
 * více propojených obrazovek, funkční navigační stack (přechody mezi obrazovkami)
 * ukládání dat do perzistentní paměti zařízení
 * vstup od uživatele (zadávání dat uživatelem)
 * komunikace s libovolným REST API
 * https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT
 * https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT
 * https://api.binance.com/api/v3/ticker/price?symbol=STGUSDT
 * v krajním případě (odůvodněno například praxí - tím, že danou technologii, jazyk, FW používáte v práci) lze zvolit libovolnou platformu (Android, iOS), programovací jazyk či vývojový framework - jinak je preferováno řešení v Kotlinu pro Android s využitím doporučených návrhových vzorů
 */

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Extending class for context to be available in ApiService
        MainActivity.appContext = applicationContext

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        /*
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        */

        /*
        * REST API test
        * */
        // launching a new coroutine
        GlobalScope.launch {
            val ping = RetrofitHelper.getPing()
            if (ping != "")
                Log.d("REST API PING TEST: ", ping)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            findNavController(R.id.SettingsFragment) // tohle nefunguje
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}