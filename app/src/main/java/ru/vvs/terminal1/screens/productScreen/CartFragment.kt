package ru.vvs.terminal1.screens.productScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.databinding.FragmentCartBinding
import ru.vvs.terminal1.model.CartItem

class CartFragment : Fragment() {

    private var mBinding: FragmentCartBinding?= null
    private val binding get() = mBinding!!
    private var cartItem: MutableLiveData<CartItem?> = MutableLiveData()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCartBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mainActivity.actionBar.title = "Карточка товара"
        val barcode = arguments?.getString("barcode").orEmpty()
        val viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        recyclerView = binding.itemsCharacterLayout
        adapter = CartAdapter()
        recyclerView.adapter = adapter

        viewModel.myItemsList.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }

        cartItem.observe(viewLifecycleOwner) { cartItem ->
            if (cartItem != null) {
                viewModel.getItems(cartItem.Product)

                binding.russianName.text = cartItem.Product.substringBefore(",").trim()
                binding.latinName.text = cartItem.Product.substringAfter(",").trim()
                binding.container.text = cartItem.PotSize
                binding.height.text = cartItem.Height
                binding.remainder.text = cartItem.Quantity.toString()
                binding.price.text = "${cartItem.Price} руб."
                binding.barcode.text = cartItem.Barcode
                binding.history.text = cartItem.History
                binding.description.text = cartItem.Description
                //binding.progressBar.visibility = View.GONE
            } else {
                Toast.makeText(
                    mainActivity,
                    "Товар с таким штрихкодом не был обнаружен\n$barcode",
                    Toast.LENGTH_LONG
                ).show()
                mainActivity.navController.navigateUp()
            }
        }

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            cartItem.postValue(viewModel.repository.getCartByBarcode(barcode))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}