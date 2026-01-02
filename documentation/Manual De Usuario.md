# Manual de Usuario — Plataforma Vaqueras

Este documento describe cómo utilizar la plataforma de videojuegos **Vaqueras**, detallando las funcionalidades disponibles para cada tipo de usuario.

---

## 1. Requisitos para usar la aplicación
Para garantizar el correcto funcionamiento del sistema, asegúrese de cumplir con lo siguiente:

* **Navegador recomendado:** Google Chrome o Microsoft Edge (versiones recientes).
* **Conexión:** Internet estable.
* **Acceso:** Disponer de la URL del Frontend (Angular) proporcionada por el administrador del sistema.

---

## 2. Roles y funcionalidades
El sistema cuenta con tres perfiles de usuario, cada uno con permisos específicos:

### 🎮 GAMER (Usuario común)
* Navegar en la tienda, buscar y filtrar videojuegos.
* Recargar saldo en la sección **“Mi Cartera”**.
* Comprar juegos (con validación automática de edad y saldo).
* Gestionar su biblioteca: instalar/desinstalar juegos (cambio de estado lógico).
* Comentar y calificar juegos (solo si los posee en su biblioteca).
* Crear grupos familiares y prestar juegos con restricciones de uso simultáneo.

### 🏢 EMPRESA (Desarrolladora)
* **Gestión de Catálogo:** Publicar, editar y suspender la venta de videojuegos.
* **Multimedia:** Gestionar imágenes y recursos de sus juegos.
* **Moderación:** Puede ocultar el texto de comentarios ofensivos en sus juegos (la calificación en estrellas se mantiene por transparencia).

### 🛠️ ADMIN (Administrador)
* Administrar categorías de juegos.
* Gestionar comisiones y banners publicitarios.
* Moderación global y generación de reportes.

> **Nota técnica:** El sistema separa los permisos por rutas (`/api/admin`, `/api/empresa`, `/api/gamer`). Si intenta acceder a una pantalla que no corresponde a su rol, el sistema bloqueará el acceso automáticamente.

---

## 3. Registro e inicio de sesión

### 3.1 Registrarse (Solo GAMER)
1.  En la pantalla inicial, seleccione la opción **“Registrarse como Gamer”**.
2.  Complete el formulario con sus datos:
    * Nickname y Correo electrónico.
    * Contraseña segura.
    * Fecha de nacimiento (importante para la validación de edad).
    * Teléfono y País.
3.  Guarde el registro. Ahora podrá iniciar sesión.

### 3.2 Iniciar sesión (Todos los roles)
1.  Presione el botón **“Ingresar”**.
2.  Escriba su correo (o nickname) y contraseña.
3.  El sistema validará sus credenciales y lo redirigirá a su panel correspondiente:
    * **ADMIN** → Panel de administración.
    * **EMPRESA** → Panel de empresa.
    * **GAMER** → Tienda / Inicio.

### 3.3 Cerrar sesión
Utilice el botón **“Cerrar sesión”** (Logout) en la barra de navegación.

> **Advertencia:** Si cierra sesión e intenta volver a una pantalla anterior usando el navegador, verá un mensaje de error tipo "Sesión cerrada / Token revocado".

---

## 4. Uso como GAMER

### 4.1 Navegar la tienda
Al ingresar, verá la pantalla de **Inicio / Tienda**:
* **Banner principal:** Muestra el contenido destacado por los administradores.
* **Sección de Destacados:** Muestra juegos seleccionados por el algoritmo de “Mejor Balance” (equilibrio entre ventas y calidad).

### 4.2 Buscar y filtrar juegos
Puede utilizar la barra de búsqueda o los filtros laterales para encontrar juegos por:
* Título
* Categoría
* Precio
* Empresa desarrolladora
* *Haga clic en un juego para ver su detalle completo.*

### 4.3 Recargar saldo (Mi Cartera)
1.  Entre a la sección **“Mi Cartera”**.
2.  Verifique su saldo actual.
3.  Ingrese el monto a recargar (ej. `50.00`) y confirme.
4.  El sistema registrará la transacción y actualizará su saldo inmediatamente.

### 4.4 Comprar un videojuego
1.  En el detalle del juego, presione **“Comprar”**.
2.  El sistema pedirá una fecha de compra (para fines de simulación) y validará:
    * **Edad:** Si su edad es menor a la clasificación del juego (E, T, M), la compra se bloqueará.
    * **Saldo:** Debe tener saldo suficiente (`Saldo ≥ Precio`).
3.  **Resultado:**
    * ✅ *Éxito:* Se descuenta el saldo, se calculan las comisiones y el juego aparece en su biblioteca.
    * ❌ *Error:* Si no tiene saldo, el sistema le sugerirá ir a recargar.

### 4.5 Mi Biblioteca
Aquí aparecen todos sus juegos adquiridos. Puede cambiar el estado del juego:
* **Instalar / Desinstalar:** Esto es un estado lógico en el sistema, útil para gestionar el espacio o las reglas del grupo familiar.

### 4.6 Comentar y calificar
Vaya al detalle de un juego que ya posea.
1.  Seleccione la calificación (1 a 5 estrellas).
2.  Escriba su reseña.
3.  El sistema recalculará el promedio de calificación del juego.

### 4.7 Grupos familiares (Biblioteca compartida)
**Crear grupo:**
Vaya a **Grupos / Biblioteca familiar**. Cree un grupo e invite miembros (Máximo 6 personas: Usted + 5 invitados).

**Reglas de Préstamo:**
Todos los juegos de los miembros son visibles para el grupo.
> **⚠️ Restricción Importante:** Solo puede tener **1 juego prestado en estado INSTALADO** a la vez.
> Si desea instalar otro juego prestado, primero debe "Desinstalar" el anterior. Los juegos comprados por usted no cuentan para este límite.

---

## 5. Uso como EMPRESA

### 5.1 Publicar videojuego
1.  Ingrese al **Panel Empresa** → **Mis Videojuegos**.
2.  Presione **Crear/Agregar videojuego**.
3.  Complete la ficha técnica (título, precio, clasificación, descripción, etc.) y guarde.
4.  Agregue la multimedia (imágenes de portada y capturas).

### 5.2 Editar o suspender venta
Desde su lista de juegos puede:
* **Editar:** Modificar información descriptiva o precios.
* **Suspender/Reactivar:** Puede detener la venta de un juego. *Nota: Esto no elimina el juego de las bibliotecas de los usuarios que ya lo compraron.*

### 5.3 Moderación
Si detecta comentarios ofensivos en sus juegos, puede **ocultar el texto**. La calificación de estrellas permanecerá visible para mantener la transparencia de la reputación.

---

## 6. Uso como ADMIN

### 6.1 Categorías
En **Panel Admin → Categorías**, puede crear, editar o eliminar las categorías que organizan la tienda (ej. Acción, RPG, Deportes).

### 6.2 Apariencia
En **Apariencia / Banner**, seleccione qué contenido promocional aparecerá en el carrusel de inicio de la tienda.

### 6.3 Finanzas
El administrador define las comisiones globales o específicas y puede generar reportes de ventas (exportables a PDF).

---

## 7. Solución de problemas frecuentes

| Mensaje de Error | Causa Probable | Solución |
| :--- | :--- | :--- |
| **"Token requerido / inválido"** | Su sesión ha caducado o no ha iniciado sesión. | Vuelva a la pantalla de Login e ingrese sus credenciales nuevamente. |
| **"Acceso denegado: Rol insuficiente"** | Está intentando entrar a una zona restringida (ej. un Gamer intentando entrar a Admin). | Navegue solo por los menús autorizados para su tipo de usuario. |
| **"Saldo insuficiente"** | El precio del juego es mayor a su saldo actual. | Vaya a "Mi Cartera" y realice una recarga. |
| **"Bloqueo por edad"** | El juego tiene clasificación **M** (Maduro) y su fecha de nacimiento indica que es menor de edad. | No podrá comprar este contenido por políticas de protección al menor. |