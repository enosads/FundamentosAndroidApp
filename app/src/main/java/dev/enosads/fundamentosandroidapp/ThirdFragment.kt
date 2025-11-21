package dev.enosads.fundamentosandroidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dev.enosads.fundamentosandroidapp.databinding.FragmentThirdBinding
import kotlinx.coroutines.launch

class ThirdFragment : Fragment() {
    private val viewModel: DiceViewModel by activityViewModels()

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.rolledDice3ImageRes?.let { imgRes ->
                    binding.rvRolledDices.adapter = RolledDicesAdapter(uiState.rolledDicesList)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }
}