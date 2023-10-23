package ru.vvs.terminal1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding?= null
    private val binding get() = mBinding!!
    lateinit var navController: NavController
    lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivity = this
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        actionBar = supportActionBar!!

        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()
        moduleInstall
            .installModules(moduleInstallRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()) {
                    // Modules are already installed when the request is sent.
                    Log.d("MLKit", "Module Loaded and Installed")
                }
            }
            .addOnFailureListener {
                // Handle failure…
                Log.d("MLKit", "Module Not Loaded")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}