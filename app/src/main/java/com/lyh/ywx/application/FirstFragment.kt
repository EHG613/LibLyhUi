package com.lyh.ywx.application

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.lyh.ywx.application.databinding.FragmentFirstBinding
import net.liangyihui.android.ui.UpdateDialog

class FirstFragment : Fragment() {
    private var binding: FragmentFirstBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.buttonSecond.setOnClickListener {
            NavHostFragment.findNavController(this@FirstFragment)
                .navigate(R.id.action_FirstFragment_to_ThirdFragment)
        }
        binding!!.buttonFirst.setOnClickListener {

            NavHostFragment.findNavController(this@FirstFragment)
                .navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        context?.let {
//           PrivacyDialog().show(it){
//               titleText("隐私协议")
//               contentUrl("https://bosdev.liangyihui.net/pm-user-agreement.html")
//               cancelableOutside(false)
//               positiveButtonColorRes(R.color.button_positive_text_color)
//               negativeButtonColorRes(R.color.button_negative_text_color)
//               onPositive("同意"){
//                   Toast.makeText(it,"同意",LENGTH_SHORT).show()
//               }
//               onNegative("拒绝"){
//                   Toast.makeText(it,"拒绝",LENGTH_SHORT).show()
//               }
//           }
            binding!!.buttonFirst.postDelayed({
                showUpdateDialog(it)
            }, 5000)

        }
//        context?.let {
//            UpdateDialog().show(it) {
//                cancelableOutside(false)
//                positiveButtonColorRes(R.color.button_positive_text_color)
//                negativeButtonColorRes(R.color.button_negative_text_color)
//                onPositive("立即升级")
//                onNegative("取消")
//            }
//        }

    }

    private fun showUpdateDialog(it: Context) {
        UpdateDialog().show(it) {

            titleText("发现新版本")
            versionText("V8.9")
            contentText(
                "1、要么是态度问题，要么是能力问题，要么两者都有问题\n2、说出来的是你想的，做出来的是你说的，交出来的是你做的\n1、要么是态度问题，要么是能力问题，要么两者都有问题\n2、说出来的是你想的，做出来的是你说的，交出来的是你做的\n1、要么是态度问题，要么是能力问题，要么两者都有问题\n" +
                        "2、说出来的是你想的，做出来的是你说的，交出来的是你做的\n" +
                        "1、要么是态度问题，要么是能力问题，要么两者都有问题\n" +
                        "2、说出来的是你想的，做出来的是你说的，交出来的是你做的\n1、要么是态度问题，要么是能力问题，要么两者都有问题\n" +
                        "2、说出来的是你想的，做出来的是你说的，交出来的是你做的\n" +
                        "1、要么是态度问题，要么是能力问题，要么两者都有问题\n" +
                        "2、说出来的是你想的，做出来的是你说的，交出来的是你做的"
            )
            cancelableOutside(false)
            displayNegativeButton(true)
            positiveButtonColorRes(R.color.button_positive_text_color)
            negativeButtonColorRes(R.color.button_negative_text_color)
            onPositive("立即升级") {
                Toast.makeText(it, "升级", LENGTH_SHORT).show()
            }
            onNegative("取消") {
                Toast.makeText(it, "取消", LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}