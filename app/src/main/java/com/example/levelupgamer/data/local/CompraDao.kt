package com.example.levelupgamer.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CompraDao {
    @Insert
    suspend fun insertCompra(compra: CompraEntity): Long

    @Insert
    suspend fun insertLineas(lineas: List<CompraLineaEntity>)

    @Transaction
    suspend fun insertConLineas(compra: CompraEntity, lineas: List<CompraLineaEntity>): Int {
        val id = insertCompra(compra).toInt()
        val ajustadas = lineas.map { it.copy(purchaseId = id) }
        insertLineas(ajustadas)
        return id
    }

    @Query("SELECT * FROM compras WHERE email = :email ORDER BY fechaMillis DESC")
    suspend fun getComprasByEmail(email: String): List<CompraEntity>

    @Query("SELECT * FROM compra_lineas WHERE purchaseId = :purchaseId")
    suspend fun getLineasByCompra(purchaseId: Int): List<CompraLineaEntity>
}