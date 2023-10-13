package ru.vvs.terminal1.screens.cartFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.vvs.terminal1.MAIN
import ru.vvs.terminal1.R
import ru.vvs.terminal1.databinding.FragmentCartBinding
import ru.vvs.terminal1.databinding.FragmentMainBinding
import ru.vvs.terminal1.model.CartItem

class CartFragment : Fragment() {

    private var mBinding: FragmentCartBinding?= null
    private val binding get() = mBinding!!
    lateinit var currentCart: CartItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCartBinding.inflate(layoutInflater, container, false)

        currentCart = arguments?.getSerializable("cart") as CartItem

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        MAIN.actionBar.title = "Карточка товара"

        val viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        binding.cartGroup.text = currentCart.GroupString.substringBeforeLast("/").substringAfterLast("/")
        binding.cartName.text = currentCart.Product
        binding.cartCharacter.text = currentCart.Character
        binding.cartBarcode.text = currentCart.Barcode
        binding.cartQuantity.text = currentCart.Quantity.toString()
        binding.cartProduction.text = currentCart.Production.toString()
        binding.cartReserve.text = currentCart.Reserve.toString()
        binding.cartPrice.text = currentCart.Price.toString()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}