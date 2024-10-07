package com.example.checkoopencv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.checkoopencv.databinding.ActivityMainBinding
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : AppCompatActivity() {

     var isScannerInstalled = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()
      //  flip()

        installGoogleScanner()

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC)
            .build()

        binding.sampleText.setOnClickListener{
            val scanner = GmsBarcodeScanning.getClient(this , options)
            if (isScannerInstalled) {
                scanner.startScan().addOnSuccessListener {
                    if (it.rawValue != null)
                        Toast.makeText(this, it.rawValue, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "Scanner not installed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.zinxScan.setOnClickListener{
            scanViaZinx()
        }
    }

    val zinxQrReaderLauncher = registerForActivityResult(ScanContract()){result ->

        if(result!= null){
            Toast.makeText(this , result.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun scanViaZinx() {
        zinxQrReaderLauncher.launch(ScanOptions().setCameraId(1).setPrompt("Scan via Zinx"))
    }

    private fun installGoogleScanner() {
        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()

        moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener {
            isScannerInstalled = true
        }.addOnFailureListener {
            isScannerInstalled = false
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * A native method that is implemented by the 'checkoopencv' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun flip():Unit

    companion object {
        // Used to load the 'checkoopencv' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}