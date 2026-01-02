# Manual Técnico — Proyecto Vaqueras

## 1. Tecnologías y Arquitectura

### Backend (API REST)
* **Lenguaje:** Java 21
* **Servidor:** Jakarta Servlet (Apache Tomcat 10)
* **Gestión de dependencias:** Maven (empaquetado `.war`)
* **Base de Datos:** JDBC + MySQL Connector
* **Utilidades:**
    * **Gson:** Manejo de JSON + Adapter personalizado para `LocalDate`
    * **Seguridad:** Hash de contraseñas con SHA-256 (`PasswordUtil`)
* **Arquitectura por capas:**
    `Controller` → `Service` → `DAO` → `DB`

> *Nota: Aquí se incluyen los códigos de las clases ya creadas...*

### Frontend (SPA)
* **Framework:** Angular
* **Seguridad:** JWT + Guards + Interceptors
* **Comunicación:** Consumo de API REST mediante `HttpClient`
* **Gestión de Sesión:** Manejo de token/cookie
* **Roles:**
    * Admin
    * Empresa
    * Gamer

### Persistencia
* **Motor:** MySQL
* **Base de datos:** `db_vaqueras`
* **Esquema:** Tablas normalizadas según requisitos del sistema (usuarios, juegos, compras, grupos, etc.).