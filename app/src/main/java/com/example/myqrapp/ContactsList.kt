package com.example.myqrapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myqrapp.databinding.ActivityContactsListBinding

class ContactsList : AppCompatActivity() {

    private lateinit var binding: ActivityContactsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}






