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
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.broadcastreceiver.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val TAG = "FirstFragment"

    private val ACTION_PERMISSIONS_NOT_GRANTED = "com.xata.ignition.broadcast.PERMISSIONS_NOT_GRANTED"
    private val KEY_PERMISSIONS_NOT_GRANTED = "PermissionsNotGranted"


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val permissionsStringReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle = intent?.extras
            val permissions = bundle?.getString(KEY_PERMISSIONS_NOT_GRANTED)
            if (permissions != null) {
                binding.textviewFirst.text = permissions
                Log.d(TAG, "Received permissions not granted: $permissions")
            } else {
                Log.d(TAG, "Received broadcast but no permissions were sent")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        val filter = IntentFilter().apply {
            addAction(ACTION_PERMISSIONS_NOT_GRANTED)
        }
        registerReceiver(requireContext(), permissionsStringReceiver, filter, RECEIVER_EXPORTED)
        Log.d(TAG, "Receiver registered for ACTION_PERMISSIONS_NOT_GRANTED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireContext().unregisterReceiver(permissionsStringReceiver)
        Log.d(TAG, "Receiver unregistered for ACTION_PERMISSIONS_NOT_GRANTED")
    }
}