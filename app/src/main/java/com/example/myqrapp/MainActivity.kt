package com.example.myqrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myqrapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var cameraPermissionGranted = false
    private var contactsPermissionGranted = false


    //CAMERA ACTIVITY
    private val cameraPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //El usuario concedió el permiso
            actionCameraPermissionGranted()
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso solamente para leer los códigos QR")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        updateOrRequestCameraPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Permiso negado permanentemente")
                    .setMessage("Para activar el permiso debe ir a la configuración de la aplicación y concederlo")
                    .setPositiveButton("Ir a configuración"){ _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .create()
                    .show()
            }
        }
    }

    // CONTACTS ACTIVITY
    private val contactsPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //El usuario concedió el permiso
            actionContactsPermissionGranted()
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso solamente para leer los contactos")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        updateOrRequestContactsPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Permiso negado permanentemente")
                    .setMessage("Para activar el permiso debe ir a la configuración de la aplicación y concederlo")
                    .setPositiveButton("Ir a configuración"){ _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .create()
                    .show()
            }
        }
    }

    // MAIN ACTIVITY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamara.setOnClickListener {
            //Mando el intent hacia el activity del lector de QR's
            //Hay que verificar si tenemos el permiso
            updateOrRequestCameraPermissions()
        }
        binding.bContacts.setOnClickListener {
            //Mando el intent hacia el activity del lector de contactos
            //Hay que verificar si tenemos el permiso
            updateOrRequestContactsPermissions()
        }
    }

    // CAMERA PERMISSIONS

    private fun updateOrRequestCameraPermissions() {
        cameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if(!cameraPermissionGranted){
            //Pido el permiso
            cameraPermissionsLauncher.launch(Manifest.permission.CAMERA)
        }else{
            //Tenemos el permiso
            actionCameraPermissionGranted()
        }
    }

    private fun actionCameraPermissionGranted(){
        startActivity(Intent(this, QRScanner::class.java))
    }

    // CONTACTS PERMISSIONS

    private fun updateOrRequestContactsPermissions() {
        contactsPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if(!contactsPermissionGranted){
            //Pido el permiso
            contactsPermissionsLauncher.launch(Manifest.permission.READ_CONTACTS)
        }else{
            //Tenemos el permiso
            actionContactsPermissionGranted()
        }
    }

    private fun actionContactsPermissionGranted(){
        startActivity(Intent(this, ContactsList::class.java))
    }
}