package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentOrderBinding
import ru.vvs.terminal1.databinding.FragmentOrdersBinding
import ru.vvs.terminal1.screens.ordersFragment.OrdersAdapter
import ru.vvs.terminal1.screens.ordersFragment.OrdersViewModel

class OrderFragment : Fragment() {

    private var mBinding: FragmentOrderBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        MAIN.actionBar.title = "Работа с заказом"
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}