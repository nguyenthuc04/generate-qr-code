package com.thuc0502.generateqrcode.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thuc0502.generateqrcode.Fragment.FunctionFragment.WifiFragment
import com.thuc0502.generateqrcode.R
import com.thuc0502.generateqrcode.databinding.FragmentCreateBinding
import com.thuc0502.generateqrcode.replaceFragment


class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    override fun onCreateView(
        inflater: LayoutInflater ,container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater ,container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View ,savedInstanceState: Bundle?) {
        super.onViewCreated(view ,savedInstanceState)
        binding = FragmentCreateBinding.bind(view)

        binding.btnWifi.setOnClickListener {
            // Handle click event
            val newFragment = WifiFragment()
            parentFragmentManager.replaceFragment(R.id.frameLayout, newFragment, "WifiFragment")
        }
    }
}