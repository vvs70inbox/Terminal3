package ru.vvs.terminal1.screens.salesFragment.saleFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentSaleBinding
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.model.Sale

class SaleFragment : Fragment() {

    private var mBinding: FragmentSaleBinding?= null
    private val binding get() = mBinding!!

    private lateinit var viewModel: SaleViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SaleAdapter

    lateinit var currentSale: Sale

    private var allowManualInput = false
    private var enableAutoZoom = false //true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSaleBinding.inflate(layoutInflater, container, false)

        currentSale = arguments?.getSerializable("order") as Sale

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mainActivity.actionBar.title = "Работа с отгрузкой"
        viewModel = ViewModelProvider(this)[SaleViewModel::class.java]

        recyclerView = binding.itemsSaleLayout
        adapter = SaleAdapter() {position -> onItemClick(position)}
        recyclerView.adapter = adapter

        binding.saleNumber.text = currentSale.number
        binding.saleDate.text = currentSale.date.substring(0..9)
        binding.saleNote.text = currentSale.name

        binding.salePositions.text = currentSale.positions.toString()
        binding.saleAmount.text = currentSale.amount.toString()
        binding.saleProducts.text = currentSale.products.toString()

        viewModel.getItems(currentSale.id)
        viewModel.myItemsList.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
/*            currentOrder.products = list.sumOf { it.counts }
            currentOrder.amount = list.sumOf { it.counts * it.Price }
            currentOrder.positions = list.count()*/
        }

        binding.fabSale.setOnClickListener {
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
                            viewModel.updateItem(barcode.rawValue!!, currentSale.id)
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


    private fun onItemClick(position: Int) {
        //toast(adapter.listMain[position].Product)
        val itemsOrder = adapter.listMain[position]
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = mainActivity.layoutInflater
        val oldCounts = itemsOrder.checks
        var newCounts = 0


        builder.setTitle("Укажите проверенное количество")
        val dialogLayout = inflater.inflate(R.layout.item_count_alert, null)
        dialogLayout.findViewById<TextView>(R.id.textViewAlert).text = itemsOrder.Product
        val editText  = dialogLayout.findViewById<EditText>(R.id.editTextAlert)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->

            if (editText.text.toString().dropWhile { it == ' ' }.isEmpty()) {
                newCounts = oldCounts}
            else {
                newCounts = editText.text.toString().toInt()
            }

            if (oldCounts != newCounts && newCounts != 0) {
                viewModel.updateItemCount(itemsOrder, currentSale.id, newCounts)
            }
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //viewModel.updateSale(currentSale)
        mBinding = null
    }
}