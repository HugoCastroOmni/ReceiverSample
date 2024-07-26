package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.broadcastreceiver.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val TAG = "SecondFragment"

    private val ACTION_PERMISSIONS_NOT_GRANTED = "com.xata.ignition.broadcast.PERMISSIONS_NOT_GRANTED"
    private val KEY_PERMISSIONS_NOT_GRANTED = "PermissionsNotGranted"

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val permissionsListReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
                val bundle = intent?.extras
                val permissions = bundle?.getStringArrayList(KEY_PERMISSIONS_NOT_GRANTED)
                if (permissions != null) {
                    val sb = StringBuilder()
                    for (i in permissions.indices) {
                        sb.append(permissions[i])
                        if (i + 1 < permissions.size) {
                            sb.append(", \n")
                        }
                    }
                    val missingPermission = sb.toString()
                    binding.textviewSecond.text = missingPermission
                    Log.d(TAG, "Received permissions not granted: $missingPermission")
                } else {
                    Log.d(TAG, "Received broadcast but no permissions were sent")
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        val filter = IntentFilter().apply {
            addAction(ACTION_PERMISSIONS_NOT_GRANTED)
        }
        ContextCompat.registerReceiver(
            requireContext(),
            permissionsListReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "Receiver registered for ACTION_PERMISSIONS_NOT_GRANTED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireContext().unregisterReceiver(permissionsListReceiver)
        Log.d(TAG, "Receiver unregistered for ACTION_PERMISSIONS_NOT_GRANTED")
    }
}