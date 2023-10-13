package ru.vvs.terminal1.screens.startFragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentMainBinding
import ru.vvs.terminal1.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var mBinding: FragmentStartBinding?= null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStartBinding.inflate(inflater, container, false)

        binding.buttonList.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_startFragment_to_mainFragment)
        }

        binding.buttonOrders.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_startFragment_to_ordersFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}