package ru.vvs.terminal1.model

import java.text.DecimalFormat

data class SaleImportItem(
    val amoutSale: Int,
    val buyerSale: String,
    val commentSale: String,
    val dateSale: String,
    val managerSale: String,
    val numberSale: String
) {
    override fun toString(): String {
        val df = DecimalFormat("###,###")
        return numberSale+" - "+dateSale.substring(0..9)+"\n"+buyerSale+"\nСумма - "+df.format(amoutSale)+"\n"
    }
}

/*ДанныеЗаказа.Вставить("dateSale", Выборка.ДатаЗаказа);
ДанныеЗаказа.Вставить("numberSale", Выборка.НомерЗаказа);
ДанныеЗаказа.Вставить("buyerSale", Выборка.Контрагент);
ДанныеЗаказа.Вставить("managerSale", Выборка.Ответственный);
ДанныеЗаказа.Вставить("commentSale", Выборка.Комментарий);
ДанныеЗаказа.Вставить("amoutSale", Выборка.СуммаДокумента);*/