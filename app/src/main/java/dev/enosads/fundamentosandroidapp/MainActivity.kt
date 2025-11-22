package dev.enosads.fundamentosandroidapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import dev.enosads.fundamentosandroidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: DiceViewModel by viewModels()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvMainContainer) as? NavHostFragment
        navHostFragment?.navController
    }

    override fun onResume() {
        super.onResume()
        viewModel.uiStateLiveData.observe(this@MainActivity) { uiState ->
            uiState.rolledDice1ImageRes?.let { imgRes ->
                binding.ivRolledDice1.setImageResource(
                    imgRes
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { uiState ->
//                    uiState.rolledDice1ImageRes?.let { imgRes -> binding.ivRolledDice1.setImageResource(imgRes) }
//                }
//            }
//        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRollDice.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).setTitle("Rodar os dados")
                .setMessage("Deseja realmente jogar os dados?")
                .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("Sim") { _, _ ->
                    viewModel.rollDice()
                }.setPositiveButtonIcon(
                    AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_dice_unknown),
                ).setNegativeButton("Não") { _, _ ->

                }.setCancelable(false).create().show()
        }

        binding.btnNextFragment.setOnClickListener {
            navController?.currentDestination?.id.let {
                when (it) {
                    R.id.firstFragment -> {
                        navController?.navigate(
                            R.id.action_firstFragment_to_secondFragment,
                            bundleOf("firstArg" to arrayOf("1", "2", "3"))
                        )
                        binding.btnNextFragment.text =
                            getString(R.string.ir_para_a_lista_de_jogadas)
                    }

                    R.id.secondFragment -> {
                        navController?.navigate(R.id.action_secondFragment_to_thirdFragment)
                        binding.btnNextFragment.text =
                            getString(R.string.voltar_para_o_primeiro_fragment)
                    }

                    R.id.thirdFragment -> {
                        navController?.navigate(R.id.action_thirdFragment_to_firstFragment)
                        binding.btnNextFragment.text =
                            getString(R.string.ir_para_proxima_tela)
                    }
                }
            }
        }
    }
}