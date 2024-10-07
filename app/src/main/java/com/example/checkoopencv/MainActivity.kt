package com.example.checkoopencv

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private var isScannerInstalled = false
    private lateinit var binding: ActivityMainBinding

    private val zinxQrReaderLauncher = registerForActivityResult(ScanContract()) { result ->

        if (result != null) {
            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()
        flip(copyAssetToInternalStorage(applicationContext , "1.png"))

        installGoogleScanner()
        handleClickListeners()

    }

    private fun handleClickListeners() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC)
            .build()

        binding.sampleText.setOnClickListener {
            val scanner = GmsBarcodeScanning.getClient(this, options)
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
            } else {
                Toast.makeText(this, "Scanner not installed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.zinxScan.setOnClickListener {
            scanViaZinx()
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
    external fun flip( name:String): Unit


    fun copyAssetToInternalStorage(context: Context, fileName: String): String {
        // Get the AssetManager
        val assetManager = context.assets

        // Open the asset as an input stream
        val inputStream = assetManager.open(fileName)
        Log.d("Neeraj" , inputStream.readBytes().size.toString())
        inputStream.reset()

        // Create a temporary file in the app's internal storage
        val outFile = File(context.externalMediaDirs[0], fileName)
        if(!outFile.parentFile.exists()){
            outFile.parentFile.mkdirs()
        }
        if(!outFile.exists()){
            outFile.createNewFile()
        }
        val outputStream = FileOutputStream(outFile)

        // Copy the content from the asset to the file
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        Log.d("Neeraj" , outFile.absolutePath)
        // Return the file path of the copied file
        return outFile.absolutePath
    }



    companion object {
        // Used to load the 'checkoopencv' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}