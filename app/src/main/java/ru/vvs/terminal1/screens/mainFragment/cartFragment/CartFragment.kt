package ru.vvs.terminal1.screens.mainFragment.cartFragment

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
import ru.vvs.terminal1.screens.ordersFragment.orderFragment.OrderAdapter

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

                binding.cartGroup.text = cartItem.GroupString.substringBeforeLast("/").substringAfterLast("/")
                binding.cartName.text = cartItem.Product.substringBefore(",")
                binding.cartNameEnglish.text = cartItem.Product.substringAfter(",").trim()
                binding.cartCharacter.text = cartItem.Character
                binding.cartBarcode.text = cartItem.Barcode
                binding.cartQuantity.text = cartItem.Quantity.toString()
                binding.cartProduction.text = cartItem.Production.toString()
                binding.cartReserve.text = cartItem.Reserve.toString()
                binding.cartPrice.text = cartItem.Price.toString()
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