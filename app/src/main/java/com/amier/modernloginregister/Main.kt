package com.amier.modernloginregister

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amier.modernloginregister.databinding.Main3Binding


class Main : AppCompatActivity() {


private lateinit var binding: Main3Binding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Main3Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}
