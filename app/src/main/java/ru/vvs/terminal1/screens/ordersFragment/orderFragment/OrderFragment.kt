package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentOrderBinding
import ru.vvs.terminal1.model.Order

class OrderFragment : Fragment() {

    private var mBinding: FragmentOrderBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter

    lateinit var currentOrder: Order

    private var allowManualInput = false
    private var enableAutoZoom = false //true
    private var barcodeResultView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        currentOrder = arguments?.getSerializable("order") as Order

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun toast(text: String) = Toast.makeText(MAIN, text, Toast.LENGTH_SHORT).show()
    private fun init() {
        MAIN.actionBar.title = "Работа с заказом"
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        recyclerView = binding.itemsOrderLayout
        adapter = OrderAdapter()
        recyclerView.adapter = adapter

        binding.orderNumber.text = currentOrder.number
        binding.orderDate.text = currentOrder.date
        binding.orderNote.text = currentOrder.name

        viewModel.itemOrder.observe(viewLifecycleOwner) { } // снимаем наблюдение
        viewModel.itemOrder = MutableLiveData()

        viewModel.getItems(currentOrder.id)
        viewModel.myItemsList.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                val alertDialog = AlertDialog.Builder(MAIN)

                alertDialog.apply {
                    setIcon(R.drawable.baseline_delete_24)
                    setTitle("Удаление товара")
                    setMessage("Вы уверены, что хотите удалить выбранный товар?")
                    setCancelable(false)
                    setPositiveButton("ДА") { _, _ ->
                        //toast("clicked positive button")
                        viewModel.swipeItem(viewHolder.adapterPosition, currentOrder.id)
                    }
                    setNegativeButton("НЕТ") { _, _ ->
                        //toast("clicked negative button")
                        viewModel.getItems(currentOrder.id)
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

        binding.fabOrder.setOnClickListener {
            val optionsBuilder = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13)
            if (allowManualInput) {
                optionsBuilder.allowManualInput()
            }
            if (enableAutoZoom) {
                optionsBuilder.enableAutoZoom()
            }
            val gmsBarcodeScanner = GmsBarcodeScanning.getClient(requireContext(), optionsBuilder.build())
            gmsBarcodeScanner
                .startScan()
                .addOnSuccessListener { barcode: Barcode ->
                    when (barcode.rawValue!!.substring(0, 2)) {
                        "27" -> // изменяем кол-во
                        {
                            viewModel.updateItem(barcode.rawValue!!, currentOrder.id)
                            //if (cart.Barcode == barcode.rawValue) Toast.makeText(MAIN, "ВСЁ ОК!!!!!", Toast.LENGTH_LONG).show()
                        }
                        else -> Toast.makeText(MAIN, "Штрихкод начинается не на 27!", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e: Exception -> barcodeResultView!!.text = getErrorMessage(e) }
                .addOnCanceledListener {
                    //barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
                    Toast.makeText(MAIN, getString(R.string.error_scanner_cancelled), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getErrorMessage(e: Exception): String? {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    getString(R.string.error_camera_permission_not_granted)
                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    getString(R.string.error_app_name_unavailable)
                else -> getString(R.string.error_default_message, e)
            }
        } else {
            e.message
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}