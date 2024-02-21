package ru.vvs.terminal1.screens.orderViewScreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentOrderBinding
import ru.vvs.terminal1.model.Order

class OrderViewFragment : Fragment() {

    private var mBinding: FragmentOrderBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: OrderViewViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderViewAdapter

    private lateinit var menuHost: MenuHost
    private lateinit var provider: MenuProvider

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
    private fun toast(text: String) = Toast.makeText(mainActivity, text, Toast.LENGTH_SHORT).show()
    private fun init() {
        mainActivity.actionBar.title = "Работа с заказом"
        viewModel = ViewModelProvider(this)[OrderViewViewModel::class.java]

        recyclerView = binding.itemsOrderLayout
        adapter = OrderViewAdapter({ pos -> onItemClick(pos)}, { pos -> onItemDelete(pos)})
        recyclerView.adapter = adapter

        binding.orderNumber.text = currentOrder.id.toString()
        binding.orderDate.text = currentOrder.createdAt.toString()
        binding.orderNote.text = currentOrder.name

        binding.orderPositions.text = currentOrder.positions.toString()
        binding.orderAmount.text = currentOrder.amount.toString()
        binding.orderProducts.text = currentOrder.products.toString()

        viewModel.getOrderItems(currentOrder.id)
        viewModel.orderItemList.observe(viewLifecycleOwner) { list ->
            adapter.setOrderItemList(list)
            currentOrder.products = list.sumOf { it.amount }
            currentOrder.amount = list.sumOf { it.amount * it.product.price }
            currentOrder.positions = list.count()
        }

        menuHost = requireActivity()
        provider = object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_unload, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.actionUnload -> {
                        viewModel.createOrderIn1C()
                    }
                }
                return true
            }
        }
        menuHost.addMenuProvider(provider)


        binding.fabOrder.setOnClickListener {
            val optionsBuilder = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13)
            if (allowManualInput) {
                optionsBuilder.allowManualInput()
            }
            if (enableAutoZoom) {
                optionsBuilder.enableAutoZoom()
            }
            val gmsBarcodeScanner = GmsBarcodeScanning.getClient(mainActivity, optionsBuilder.build())
            gmsBarcodeScanner
                .startScan()
                .addOnSuccessListener { barcode: Barcode ->
                    when (barcode.rawValue!!.substring(0, 2)) {
                        "27" -> // изменяем кол-во
                        {
                            viewModel.incrementOrderItemAmount(barcode.rawValue!!)
                            //if (cart.Barcode == barcode.rawValue) Toast.makeText(MAIN, "ВСЁ ОК!!!!!", Toast.LENGTH_LONG).show()
                        }
                        else -> Toast.makeText(mainActivity, "Штрихкод начинается не на 27!", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e: Exception -> getErrorMessage(e) } //barcodeResultView!!.text = getErrorMessage(e) }
                .addOnCanceledListener {
                    //barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
                    Toast.makeText(mainActivity, getString(R.string.error_scanner_cancelled), Toast.LENGTH_SHORT).show()
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
        viewModel.updateOrder(currentOrder)
        mBinding = null
        menuHost.removeMenuProvider(provider)
    }

    private fun onItemDelete(position: Int) {
        viewModel.deleteItem(position, currentOrder.id)
    }

    private fun onItemClick(position: Int) {
        //toast(adapter.listMain[position].Product)
        val itemsOrder = adapter.listMain[position]
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = mainActivity.layoutInflater
        val oldCounts = itemsOrder.counts
        var newCounts: Int = 0


        builder.setTitle("Укажите нужное количество")
        val dialogLayout = inflater.inflate(R.layout.item_count_alert, null)
        dialogLayout.findViewById<TextView>(R.id.textViewAlert).text = itemsOrder.Product
        val editText  = dialogLayout.findViewById<EditText>(R.id.editTextAlert)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { _, _ ->

            newCounts = if (editText.text.toString().dropWhile { it == ' ' }.isEmpty()) {
                oldCounts
            } else {
                editText.text.toString().toInt()
            }

            if (oldCounts != newCounts && newCounts != 0) {
                viewModel.updateItemCount(itemsOrder, currentOrder.id, newCounts)
            }
        }
        builder.show()
    }
}