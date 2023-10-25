package ru.vvs.terminal1.screens.mainFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentMainBinding
import ru.vvs.terminal1.model.CartItem

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    private lateinit var menuHost: MenuHost
    private lateinit var provider: MenuProvider

    private var mBinding: FragmentMainBinding ?= null
    private val binding get() = mBinding!!

    private var allowManualInput = false
    private var enableAutoZoom = true
    //private var barcodeResultView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mainActivity.actionBar.title = "Работа с картотекой"
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        recyclerView = binding.mainFragment
        adapter = MainAdapter()
        recyclerView.adapter = adapter

        viewModel.isProgress.observe(viewLifecycleOwner) { bool ->
            when(bool) {
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.INVISIBLE

                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cartItem.observe(viewLifecycleOwner) { } // снимаем наблюдение
        viewModel.cartItem = MutableLiveData()

        if (viewModel.myCartList.value == null) {
            viewModel.getCarts(false)
        }

        viewModel.myCartList.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }

        binding.fab.setOnClickListener {
            val optionsBuilder = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13)
            if (allowManualInput) {
                optionsBuilder.allowManualInput()
            }
            if (enableAutoZoom) {
                optionsBuilder.enableAutoZoom()
            }
            val gmsBarcodeScanner = GmsBarcodeScanning.getClient(requireContext(), optionsBuilder.build()) //
            gmsBarcodeScanner
                .startScan()
                .addOnSuccessListener { barcode: Barcode ->
                    viewModel.cartItem.observe(viewLifecycleOwner) { cart ->
                        if (cart !=null) {
                            when (cart.Barcode.substring(0, 2)) {
                                "27" -> if (cart.Barcode == barcode.rawValue) clickMovie(cart)
                                else -> Toast.makeText(
                                    mainActivity,
                                    "Штрихкод начинается не на 27!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        else
                        {Toast.makeText(
                            mainActivity,
                            "Штрихкод не обнаружен - товара нет!",
                            Toast.LENGTH_LONG
                        ).show()}
                    }
                    //поиск по barcode, возврат CartItem во ViewMidel
                    viewModel.getCartByBarcode(barcode.rawValue!!)
                }
                .addOnFailureListener { e: Exception -> Log.d("MLKit",getErrorMessage(e)!!) }//barcodeResultView!!.text = getErrorMessage(e)
                .addOnCanceledListener {
                    //barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
                    Toast.makeText(mainActivity, getString(R.string.error_scanner_cancelled), Toast.LENGTH_SHORT).show()
                }
        }

        menuHost = requireActivity()
        provider = object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.actionSearch -> {
                        val searchView: SearchView = menuItem.actionView as SearchView
                        // below line is to call set on query text listener method.
                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(msg: String): Boolean {
                                // inside on query text change method we are
                                // calling a method to filter our recycler view.
                                filter(msg)
                                return false
                            }
                        })
                        // обрабатываем кнопку возврата из поиска / processing the return button from the search
                        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                                return true
                            }
                            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                                adapter.filterRemove()
                                return true
                            }
                        })
                    }
                    R.id.actionUpdate -> {
                        if (MainActivity.isOnline(mainActivity)) {
                            viewModel.getCarts(true)
                        } else {
                            Toast.makeText(mainActivity, "Отсутствует интернет!!!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                return true
            }
        }
        menuHost.addMenuProvider(provider)
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<CartItem> = ArrayList()

        // running a for loop to compare elements.
        for (item in adapter.listMain) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.Product.lowercase().contains(text.lowercase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(mainActivity, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
        menuHost.removeMenuProvider(provider)
    }

/*    private fun getSuccessfulMessage(barcode: Barcode): String {
        val barcodeValue =
            String.format(
                Locale.US,
                "Display Value: %s\nRaw Value: %s\nFormat: %s\nValue Type: %s",
                barcode.displayValue,
                barcode.rawValue,
                barcode.format,
                barcode.valueType
            )
        return getString(R.string.barcode_result, barcodeValue)
    }*/

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

    companion object {
        fun clickMovie(cart: CartItem) {
            val bundle = Bundle()
            bundle.putSerializable("cart", cart)
            mainActivity.navController.navigate(R.id.action_mainFragment_to_cartFragment, bundle)
        }
    }

}