# 🎮 LevelUp Gamer

Aplicación móvil desarrollada en **Kotlin + Jetpack Compose**, que permite explorar productos tecnológicos, agregarlos al carrito y registrar compras localmente mediante **Room (SQLite)**.  
El objetivo es simular una experiencia completa de compra dentro de una app nativa Android.

---

## 👩‍💻 Integrantes

| Nombre | Rol | Sección |
|---------|------|----------|
| Angel Durand | Desarrollador  | DSY1105 |

---

## 🧭 Descripción general

**LevelUp Gamer** es una aplicación enfocada en el comercio electrónico de productos gamer.  
Incluye funcionalidades completas: autenticación, exploración por categorías, carrito persistente, registro de compras y vista de historial.

Diseñada con **Material 3**, siguiendo los principios de arquitectura limpia **(MVVM + Repository Pattern)** y persistencia local con **Room**.

---

## ⚙️ Funcionalidades implementadas

✅ **Autenticación de usuarios**
- Registro y login locales con validación de campos.  
- Mantenimiento de sesión con `SessionPrefs`.

✅ **Exploración de productos**
- Vista por categorías.  
- Sección “Destacados” con productos deslizables.  
- Detalle visual con imagen, precio formateado (CLP) y descripción.

✅ **Carrito de compras**
- Agregar, eliminar y modificar cantidades.  
- Cálculo automático del total (en CLP).  
- Persistencia local de ítems.

✅ **Finalización de compra**
- Guardado en base de datos local (Room) en las tablas `Compra` y `CompraLinea`.  
- Limpieza del carrito posterior a la compra.

✅ **Historial de compras**
- Visualización de todas las compras registradas en la sección “Mi cuenta”.  
- Actualización automática al volver desde la pantalla de compra.

✅ **Diseño visual**
- Theming oscuro con fondo negro y componentes `SurfaceDark`.  
- Estilo coherente entre pantallas (cards, botones, tipografía).  
- Componentes adaptados de Material 3 (`Card`, `LazyColumn`, `Button`, `OutlinedIconButton`).

---

## 🗃️ Arquitectura del proyecto

El proyecto implementa una arquitectura **MVVM** 




---

## 🧱 Tecnologías principales

- **Kotlin** (1.9+)
- **Jetpack Compose** (Material 3)
- **Room (SQLite)** — persistencia local
- **ViewModel + Flow**
- **Coil** — carga de imágenes
- **Android Studio Iguana / Koala**
- **Gradle 8.0+**

---

## 🪜 Instalación y ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu_usuario/levelup-gamer.git
   cd levelup-gamer
   
2. Abrir en Android Studio.

3. Esperar la sincronización de dependencias (Gradle).

4. Ejecutar en un emulador o dispositivo físico Android 10 o superior.

