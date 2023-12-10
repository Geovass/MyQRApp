package com.example.myqrapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
                // Verificar si el resultado es del formato MATMSG:TO: email@ejemplo.com;SUB:Asunto del email;BODY:Texto del email;
                if (result.text.startsWith("MATMSG:TO:")) {
                    // Extraer la información del correo electrónico
                    val data = result.text.substringAfter("MATMSG:TO:").trim(';').split(";")
                    var to = ""
                    var subject = ""
                    var body = ""

                    for (item in data) {
                        val parts = item.split(":")
                        if (parts.size == 2) {
                            val key = parts[0].trim()
                            val value = parts[1].trim()
                            when (key) {
                                "" -> to = value
                                "SUB" -> subject = value
                                "BODY" -> body = value
                            }
                        }
                    }

                    // Crear un intent para enviar un correo electrónico
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto:$to")
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                    emailIntent.putExtra(Intent.EXTRA_TEXT, body)

                    startActivity(emailIntent)
                }
            } catch (e: MalformedURLException) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("El código QR no es válido para la aplicación")
                    .setNeutralButton("Aceptar") { dialog, _ ->
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


/*class QRScanner : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbvScanner.decodeContinuous { result ->
            //Toast.makeText(this, "Resultado: $result", Toast.LENGTH_LONG).show()

            binding.cbvScanner.pause()

            try{
                val url = URL(result.text)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(result.text)
                startActivity(i)
                finish()
            }catch(e: MalformedURLException){
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("El código QR no es válido para la aplicación")
                    .setNeutralButton("Aceptar"){ dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
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


}*/

