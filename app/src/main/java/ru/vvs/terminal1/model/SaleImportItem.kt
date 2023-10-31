package ru.vvs.terminal1.model

data class SaleImportItem(
    val amoutSale: Int,
    val buyerSale: String,
    val commentSale: String,
    val dateSale: String,
    val managerSale: String,
    val numberSale: String
) {
    override fun toString(): String {
        return numberSale+"|"+dateSale+"|"+buyerSale+"|"+amoutSale.toString()
    }
}

/*ДанныеЗаказа.Вставить("dateSale", Выборка.ДатаЗаказа);
ДанныеЗаказа.Вставить("numberSale", Выборка.НомерЗаказа);
ДанныеЗаказа.Вставить("buyerSale", Выборка.Контрагент);
ДанныеЗаказа.Вставить("managerSale", Выборка.Ответственный);
ДанныеЗаказа.Вставить("commentSale", Выборка.Комментарий);
ДанныеЗаказа.Вставить("amoutSale", Выборка.СуммаДокумента);*/