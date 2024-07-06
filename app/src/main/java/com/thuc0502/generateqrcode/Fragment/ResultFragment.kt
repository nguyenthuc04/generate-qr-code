package com.thuc0502.generateqrcode.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thuc0502.generateqrcode.R
import com.thuc0502.generateqrcode.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater ,container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater ,container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View ,savedInstanceState: Bundle?) {
        super.onViewCreated(view ,savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        val qrCodeResult = arguments?.getString("qr_code_result")
        binding.txtResul.text = qrCodeResult
    }


}