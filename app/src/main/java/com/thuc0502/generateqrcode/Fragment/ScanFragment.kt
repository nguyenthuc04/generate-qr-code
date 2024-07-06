package com.thuc0502.generateqrcode.Fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.otaliastudios.cameraview.frame.Frame
import com.thuc0502.generateqrcode.R
import com.thuc0502.generateqrcode.databinding.FragmentScanBinding
import com.thuc0502.generateqrcode.replaceFragment
import kotlin.experimental.and


class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    private val reader = MultiFormatReader()
    private lateinit var scanArea: Rect

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScanBinding.bind(view)

        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)
        binding.cameraView.addFrameProcessor { frame ->
            if (frame.dataClass == ByteArray::class.java) {
                processFrame(frame)
            }
        }

        // Lấy tọa độ và kích thước của ô vuông để quét
        view.post {
            val overlay = binding.overlay
            val left = overlay.left + overlay.paddingLeft
            val top = overlay.top + overlay.paddingTop
            val right = overlay.right - overlay.paddingRight
            val bottom = overlay.bottom - overlay.paddingBottom
            scanArea = Rect(left, top, right, bottom)
        }
    }

    private fun processFrame(frame: Frame) {
        val data = frame.getData<ByteArray>()
        val width = frame.size.width
        val height = frame.size.height

        val luminanceSource = RGBLuminanceSource(width, height, toIntArray(data, width, height))
        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

        try {
            val result = reader.decode(binaryBitmap)
            Log.d("ScanFragment", "QR Code result: ${result.text}")
            val points = result.resultPoints


            // Kiểm tra xem mã QR có nằm trong vùng quét không
            val isInScanArea = points.all { point ->
                scanArea.contains(point.x.toInt(), point.y.toInt())
            }

            if (!isInScanArea) {
                vibratePhone()
                // Tạo một Bundle và đặt kết quả quét vào Bundle
                val bundle = Bundle().apply {
                    putString("qr_code_result", result.text)
                }
                // Tạo một thể hiện của ResultFragment và đặt Bundle vào Fragment
                val resultFragment = ResultFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.replaceFragment(R.id.frameLayout,resultFragment,"ResultFragment")
            }
        } catch (e: Exception) {
//            Log.e("ScanFragment", "Error decoding QR code", e)
        }
    }
    private fun vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }
    private fun toIntArray(data: ByteArray, width: Int, height: Int): IntArray {
        val intArray = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val yuvIndex = y * width + x
                val yValue = data[yuvIndex] and 0xff.toByte()
                intArray[yuvIndex] = 0xff000000.toInt() or (yValue.toInt() shl 16) or (yValue.toInt() shl 8) or yValue.toInt()
            }
        }
        return intArray
    }
}