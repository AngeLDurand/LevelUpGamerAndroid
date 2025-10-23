package com.example.levelupgamer.repository

import com.example.levelupgamer.data.local.CartLocal
import com.example.levelupgamer.data.local.CompraDao
import com.example.levelupgamer.data.local.CompraEntity
import com.example.levelupgamer.data.local.CompraLineaEntity
import com.example.levelupgamer.data.local.ProductDao
import com.example.levelupgamer.model.CartItemUi
import com.example.levelupgamer.model.CartLinePersist
import com.example.levelupgamer.model.CartUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class CartRepository(
    private val cartLocal: CartLocal,
    private val productDao: ProductDao,
    private val compraDao: CompraDao
) {

    fun flow(): Flow<CartUiState> = cartLocal.flow().map { lines ->
        if (lines.isEmpty()) return@map CartUiState()

        val ids = lines.map { it.productId }.distinct()
        val products = productDao.getByIds(ids).associateBy { it.id }

        val items = lines.mapNotNull { line ->
            val p = products[line.productId] ?: return@mapNotNull null
            CartItemUi(
                id = p.id,
                title = p.titulo,
                priceClp = p.precioClp,
                image = p.thumbUrl.ifBlank { p.imagenUrl },
                qty = line.qty,
                lineTotalClp = p.precioClp * line.qty
            )
        }
        val totalQty = items.sumOf { it.qty }
        val totalClp = items.sumOf { it.lineTotalClp }
        CartUiState(items, totalQty, totalClp, items.isEmpty())
    }

    /* ====================== Mutaciones básicas ====================== */

    suspend fun add(productId: Int, delta: Int = 1) {
        require(delta > 0)
        val current = cartLocal.flow().first()

        val updated = current
            .toMutableList()
            .also { list ->
                val idx = list.indexOfFirst { it.productId == productId }
                if (idx >= 0) {
                    list[idx] = list[idx].copy(qty = (list[idx].qty + delta).coerceAtMost(99))
                } else {
                    list += CartLinePersist(productId, 1)
                }
            }
        cartLocal.save(updated)
    }

    suspend fun setQty(productId: Int, qty: Int) {
        if (qty <= 0) return remove(productId)
        val current = cartLocal.flow().first()
        val updated = current.map {
            if (it.productId == productId) it.copy(qty = qty.coerceIn(1, 99)) else it
        }
        cartLocal.save(updated)
    }

    suspend fun remove(productId: Int) {
        val current = cartLocal.flow().first()
        cartLocal.save(current.filterNot { it.productId == productId })
    }

    suspend fun clear() = cartLocal.clear()

    /* ====================== Checkout ====================== */


    suspend fun checkout(email: String?): Int {
        val lines = cartLocal.flow().first()
        if (lines.isEmpty()) return -1

        val ids = lines.map { it.productId }.distinct()
        val products = productDao.getByIds(ids).associateBy { it.id }

        val resolved = lines.mapNotNull { l ->
            val p = products[l.productId] ?: return@mapNotNull null
            Triple(l, p.titulo, p.precioClp)
        }
        if (resolved.isEmpty()) return -1

        val total = resolved.sumOf { (l, _, price) -> l.qty * price }

        val compra = CompraEntity(
            fechaMillis = System.currentTimeMillis(),
            email = email,
            totalClp = total
        )


        val lineas = resolved.map { (l, title, price) ->
            CompraLineaEntity(
                purchaseId = 0,
                productId = l.productId,
                title = title,
                priceClp = price,
                qty = l.qty,
                lineTotalClp = l.qty * price
            )
        }


        val purchaseId = compraDao.insertConLineas(compra, lineas)

        if (purchaseId > 0) cartLocal.clear()

        return purchaseId
    }
    }