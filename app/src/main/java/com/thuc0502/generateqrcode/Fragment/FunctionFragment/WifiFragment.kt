package com.thuc0502.generateqrcode.Fragment.FunctionFragment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.thuc0502.generateqrcode.generateRandomFileName
import com.thuc0502.generateqrcode.R
import com.thuc0502.generateqrcode.databinding.FragmentWifiBinding
import java.io.File
import java.io.FileOutputStream

class WifiFragment : Fragment() {
    private lateinit var binding: FragmentWifiBinding

    override fun onCreateView(
        inflater: LayoutInflater ,container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiBinding.inflate(inflater ,container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View ,savedInstanceState: Bundle?) {
        super.onViewCreated(view ,savedInstanceState)
        binding = FragmentWifiBinding.bind(view)
        val dialog = Dialog(requireContext()).apply {
            setContentView(R.layout.custom_progress_dialog)
            setCancelable(false)
            window?.setBackgroundDrawableResource(R.drawable.progress_dialog)
            window?.setLayout(580 ,900)
        }

        binding.btnCreateQR.setOnClickListener {
            val ssid = binding.edtWifiSSID.text.toString()
            val password = binding.edtPassword.text.toString()
            val wifiConfig = "WIFI:T:WPA;S:$ssid;P:$password;;"
            val bitmap = generateQRCode(wifiConfig)

            dialog.findViewById<Button>(R.id.btnCancel).apply { setOnClickListener {
                dialog.dismiss() } }
            val imgQRCode = dialog.findViewById<ImageView>(R.id.qrImageView)
            imgQRCode.setImageBitmap(bitmap)
            dialog.show()
            dialog.findViewById<Button>(R.id.btnDownload).apply {
                setOnClickListener {
                    showQRCodeImageDialog(imgQRCode,dialog)
                }
            }
        }

    }

    private fun showQRCodeImageDialog(imgQRCode: ImageView? ,dialog: Dialog) {
// Chuyển đổi ImageView thành Bitmap
        val bitmap = (imgQRCode?.drawable as BitmapDrawable).bitmap

        if (ContextCompat.checkSelfPermission(
                requireContext() ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Yêu cầu quyền ghi vào bộ nhớ ngoài nếu chưa có
            ActivityCompat.requestPermissions(
                requireActivity() ,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) ,
                0
            )
        } else {
            // Tạo thư mục "QRCode/Image" trong thư mục "Downloads" nếu nó chưa tồn tại
            val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/QRCode/Image"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Lưu Bitmap vào thư mục đã tạo
            try {
                val file = File(directory ,generateRandomFileName(".png"))
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG ,100 ,outputStream)
                outputStream.flush()
                outputStream.close()
                dialog.dismiss()
                parentFragmentManager.popBackStack()
                Toast.makeText(requireContext() ,"Lưu ảnh thành công" ,Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext() ,"Error saving image" ,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun generateQRCode(text: String): Bitmap? {
        val size = 300 // Kích thước mã QR
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix = qrCodeWriter.encode(text ,BarcodeFormat.QR_CODE ,size ,size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width ,height ,Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x ,
                        y ,
                        if (bitMatrix[x ,y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}