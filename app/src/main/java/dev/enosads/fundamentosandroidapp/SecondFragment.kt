package dev.enosads.fundamentosandroidapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dev.enosads.fundamentosandroidapp.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    private val viewModel: DiceViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstArgument = arguments?.getStringArray("first_arg") ?: arrayOf()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.tvSecondFragment.text = viewModel.uiState.value.rolledDiceValue?.toString()
            }
        }

        Log.d("SecondFragment", "Argument: ${firstArgument.joinToString() }")

    }
}