package com.example.levelupgamer.data.local

import android.util.Log

suspend fun seed(catDao: CategoryDao, prodDao: ProductDao) {
    // Inserta TODAS las categorías si está vacío
    if (catDao.count() == 0) {
        catDao.insertAll(
            listOf(
                CategoryEntity("computacion", "Computación"),
                CategoryEntity("gaming", "Gaming y Streaming"),
                CategoryEntity("componentes", "Componentes"),
                CategoryEntity("redes", "Conectividad y Redes")
            )
        )
    }

    // Inserta productos si no hay ninguno aún
    if (prodDao.count() == 0) {
        val productos = listOf(
            // --- Computación ---
            ProductEntity(
                titulo = "Notebook HP 15-FD0010LA, Intel Core i3-N305, Ram 8GB, LED 15.6\", SSD 512GB, W11 Home",
                precioClp = 854_510,
                imagenUrl = "notebook_hp",
                thumbUrl  = "notebook_hp",
                categoriaSlug = "computacion"
            ),
            ProductEntity(
                titulo = "Notebook HP ProBook 440 G11, Intel Core Ultra 7 155U, 14.0\" WUXGA, Ram 16GB, SSD 512GB, W11Pro",
                precioClp = 944_070,
                imagenUrl = "probook_440",
                thumbUrl  = "probook_440",
                categoriaSlug = "computacion"
            ),

            // --- Gaming y Streaming ---
            ProductEntity(
                titulo = "PC Gamer ARGB 1, Ryzen 3 5300G, 16GB RGB, 512GB NVMe, WiFi, ARGB, FreeDOS (Sin SO)",
                precioClp = 457_480,
                imagenUrl = "pc_gamer_argb1",
                thumbUrl  = "pc_gamer_argb1",
                categoriaSlug = "gaming"
            ),

            // --- Componentes ---
            ProductEntity(
                titulo = "Procesador Intel Core i5-10400 6-Core 2.9 GHz (12M Cache, up to 4.30 GHz) LGA1200 65W",
                precioClp = 225_790,
                imagenUrl = "intel_i5_10400",
                thumbUrl  = "intel_i5_10400",
                categoriaSlug = "componentes"
            ),

            // --- Conectividad y Redes ---
            ProductEntity(
                titulo = "Gateway Multi-Servicio, 8x PoE+, 16 Gbps, Huawei S380-S8P2T eKitEngine - 300 usuarios",
                precioClp = 369_680,
                imagenUrl = "huawei_gateway",
                thumbUrl  = "huawei_gateway",
                categoriaSlug = "redes"
            )
        )

        prodDao.insertAll(productos)
        Log.d("DB_SEED","cat=${catDao.count()} prod=${prodDao.count()}")

    }
}
