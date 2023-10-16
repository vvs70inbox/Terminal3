package ru.vvs.terminal1.screens.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentOrdersBinding
import ru.vvs.terminal1.model.CartItem
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.screens.mainFragment.MainAdapter
import ru.vvs.terminal1.screens.mainFragment.MainFragment
import ru.vvs.terminal1.screens.mainFragment.MainViewModel

class OrdersFragment : Fragment() {

    private var mBinding: FragmentOrdersBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: OrdersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        MAIN.actionBar.title = "Работа с заказами"
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        recyclerView = binding.ordersFragment
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter


        viewModel.order.observe(viewLifecycleOwner) { } // снимаем наблюдение
        viewModel.order = MutableLiveData()

        viewModel.isProgress.observe(viewLifecycleOwner) { bool ->
            when(bool) {
                true -> {
                    binding.progressBarOrders.visibility = View.VISIBLE
                    binding.fabOrders.visibility = View.INVISIBLE

                }
                else -> {
                    binding.progressBarOrders.visibility = View.GONE
                    binding.fabOrders.visibility = View.VISIBLE
                }
            }
        }

        if (viewModel.myOrdersList.value == null) {
            viewModel.getOrders(false)
        }

        viewModel.myOrdersList.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }

        binding.fabOrders.setOnClickListener {
            viewModel.newOrder()
            viewModel.order.observe(viewLifecycleOwner) {order ->
                clickOrder(order)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    companion object {
        fun clickOrder(order: Order) {
            val bundle = Bundle()
            bundle.putSerializable("order", order)
            MAIN.navController.navigate(R.id.action_ordersFragment_to_orderFragment, bundle)
        }
    }

}