package com.tktzlabs.AK7MT

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tktzlabs.AK7MT.PreferenceHelper.customPreference
import com.tktzlabs.AK7MT.PreferenceHelper.personName
import com.tktzlabs.AK7MT.PreferenceHelper.apiUrl
import com.tktzlabs.AK7MT.databinding.FragmentSettingsBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SettingsFragment : Fragment() {
    private val customPreference = "User_data"
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //var settings = this.activity!!.getSharedPreferences("pref", Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        val settings = customPreference(this.activity!!, customPreference)
        binding.editTextPersonName.setText(settings.personName.toString())
        binding.editTextApiUrl.setText(settings.apiUrl.toString())

        //
        binding.buttonSaveBack.setOnClickListener {
            // Save preferences
            settings.personName = getPersonName()
            settings.apiUrl = getApiUrl()

            // Show toast
            Toast.makeText(getActivity(), "Nastaveni bylo uloženo!", Toast.LENGTH_SHORT).show();
            findNavController().navigate(R.id.action_SettingsFragment_to_WelcomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPersonName(): String {
        return binding.editTextPersonName.text.toString()
    }

    private fun getApiUrl(): String {
        return binding.editTextApiUrl.text.toString()
    }

}


object PreferenceHelper {

    val API_URL = "API_URL"
    val PERSON_NAME = "PERSON_NAME"
    val USER_PASSWORD = "PASSWORD"

    fun defaultPreference(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.personName
        get() = getString(PERSON_NAME, "")
        set(value) {
            editMe {
                it.putString(PERSON_NAME, value)
            }
        }

    var SharedPreferences.apiUrl
        get() = getString(API_URL, "")
        set(value) {
            editMe {
                it.putString(API_URL, value)
            }
        }

    /*
        // INT
        var SharedPreferences.personName
        get() = getInt(PERSON_NAME, 0)
        set(value) {
            editMe {
                it.putInt(PERSON_NAME, value)
            }
        }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                it.putString(USER_PASSWORD, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }
    */
}
