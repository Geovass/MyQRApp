package com.example.myqrapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myqrapp.databinding.ActivityQrscannerBinding
import java.net.MalformedURLException

class QRScanner : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbvScanner.decodeContinuous { result ->
            binding.cbvScanner.pause()
            try {
                if (result.text.startsWith("MATMSG:TO:")) {
                    val data = result.text.substringAfter("MATMSG:TO:").trim(';').split(";")
                    var to = ""
                    var subject = ""
                    var body = ""

                    for (item in data) {
                        val parts = item.split(":")
                        if (parts.size == 2) {
                            val key = parts[0]
                            val value = parts[1]
                            when (key) {
                                "" -> to = value
                                "SUB" -> subject = value
                                "BODY" -> body = value
                            }
                        }
                    }

                    val qremailIntent = Intent(Intent.ACTION_SENDTO)
                    qremailIntent.data = Uri.parse(getString(R.string.mailto, to))
                    qremailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                    qremailIntent.putExtra(Intent.EXTRA_TEXT, body)

                    startActivity(qremailIntent)
                }
            } catch (e: MalformedURLException) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.qr_no_valido))
                    .setNeutralButton(getString(R.string.aceptar)) { dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create()
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }
}