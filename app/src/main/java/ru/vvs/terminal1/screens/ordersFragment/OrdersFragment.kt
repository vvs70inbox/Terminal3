package ru.vvs.terminal1.screens.ordersFragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
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

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //TODO Вынести в утилиты
                val alertDialog = AlertDialog.Builder(MAIN)
                alertDialog.apply {
                    setIcon(R.drawable.baseline_delete_24)
                    setTitle("Удаление заказа")
                    setMessage("Вы уверены, что хотите удалить выбранный заказ?")
                    setCancelable(false)
                    setPositiveButton("ДА") { _, _ ->
                        //toast("clicked positive button")
                        viewModel.swipeItem(viewModel.myOrdersList.value!!.get(viewHolder.adapterPosition) )
                    }
                    setNegativeButton("НЕТ") { _, _ ->
                        //toast("clicked negative button")
                        viewModel.getOrders(false)
                    }
                    //setNeutralButton("Neutral") { _, _ ->
                    //    toast("clicked neutral button")
                    //}
                }.create().show()

                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                ////val deletedCourse: ItemsOrder =
                ////viewModel.myItemsList.value!!.get(viewHolder.adapterPosition)
                //-//courseList.get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                ////val position = viewHolder.adapterPosition


                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                ////courseList.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                ////adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                /*                Snackbar.make(recyclerView, "Deleted " + deletedCourse.Product, Snackbar.LENGTH_LONG)
                                    .setAction(
                                        "Undo",
                                        View.OnClickListener {
                                            // adding on click listener to our action of snack bar.
                                            // below line is to add our item to array list with a position.
                                            ////courseList.add(position, deletedCourse)

                                            // below line is to notify item is
                                            // added to our adapter class.
                                            //adapter.notifyItemInserted(position)
                                            Toast.makeText(MAIN, "Пробуем свайп", Toast.LENGTH_SHORT).show()
                                        }).show()*/
            }

        }).attachToRecyclerView(recyclerView)

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