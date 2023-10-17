package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentOrderBinding
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.screens.mainFragment.MainFragment
import ru.vvs.terminal1.screens.ordersFragment.OrdersAdapter

class OrderFragment : Fragment() {

    private var mBinding: FragmentOrderBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter

    lateinit var currentOrder: Order

    private var allowManualInput = false
    private var enableAutoZoom = true
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
                    viewModel.itemOrder.observe(viewLifecycleOwner) { cart ->
                        if (cart !=null) {
                            when (cart.Barcode.substring(0, 2)) {
                                "27" -> if (cart.Barcode == barcode.rawValue) Toast.makeText(MAIN,"ВСЁ ОК!!!!!", Toast.LENGTH_LONG).show()
                                else -> Toast.makeText(
                                    MAIN,
                                    "Штрихкод начинается не на 27!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        else {
                            when (barcode.rawValue!!.substring(0, 2)) {
                                "27" -> // Новая запись о товаре
                                 {
                                     Toast.makeText(MAIN,"Надо заводить запись!!!", Toast.LENGTH_LONG).show()
                                     viewModel.newItem(barcode.rawValue!!, currentOrder.id)
                                 }
                                else -> Toast.makeText(
                                    MAIN,
                                    "Штрихкод не обнаружен - товара нет!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    //поиск по barcode, возврат Item во ViewMidel
                    viewModel.getItemByBarcode(barcode.rawValue!!)
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