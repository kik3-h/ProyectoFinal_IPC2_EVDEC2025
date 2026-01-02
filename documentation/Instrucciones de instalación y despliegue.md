## 5. Instrucciones de instalación y despliegue

### 5.1 Requisitos previos
Para ejecutar el proyecto localmente, asegúrate de tener instalado el siguiente software:

* **Java JDK:** Versión 21
* **Gestor de dependencias:** Maven 3.9+
* **Servidor de aplicaciones:** Apache Tomcat 10.x (Compatible con Jakarta Servlet 6)
* **Base de datos:** MySQL 8.x
* **Frontend:** Node.js 18+ y Angular CLI

---

### 5.2 Base de datos (MySQL)

1.  **Crear la base de datos:**
    Ejecuta el siguiente comando en tu cliente SQL:
    ```sql
    CREATE DATABASE db_vaqueras CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
    ```

2.  **Importar tablas y datos:**
    * **Opción A (MySQL Workbench):** Ve a `Server` → `Data Import` → `Import from Self-Contained File`.
    * **Opción B (Consola):**
        ```bash
        mysql -u root -p db_vaqueras < DB_Vaqueras.sql
        ```

3.  **Verificar conexión desde el backend:**
    Revisa que tu clase `DatabaseConfig` apunte correctamente:
    * **URL:** `jdbc:mysql://localhost:3306/db_vaqueras?...`
    * **Usuario:** `root`
    * **Password:** *(Configurada en tu archivo local)*

    > **Recomendación técnica:** Se sugiere mover las credenciales a variables de entorno o un archivo `.properties` externo para evitar dejar contraseñas en el código fuente (hardcoding).

---

### 5.3 Backend (Java WAR en Tomcat)

#### Opción A — Despliegue clásico (Copiando WAR)
1.  **Compilar el proyecto:**
    ```bash
    mvn clean package
    ```
2.  **Ubicar el archivo generado:**
    Se encontrará en: `target/vaqueras-backend.war`
3.  **Desplegar en Tomcat:**
    Copia el archivo `.war` a la carpeta: `TOMCAT_HOME/webapps/`
4.  **Iniciar el servidor:**
    * **Windows:** `bin/startup.bat`
    * **Linux/Mac:** `bin/startup.sh`

#### Opción B — Despliegue desde IDE
* Configurar Tomcat en IntelliJ, NetBeans o Eclipse.
* Agregar el artefacto **WAR Exploded** o **WAR** al servidor.
* Ejecutar en modo **Run** o **Debug**.

#### Prueba de Endpoint (Ejemplo)
Para verificar que el backend responde, realiza una petición `POST`:

* **URL:** `http://localhost:8080/vaqueras-backend/api/usuarios`
* **Body (JSON):**
    ```json
    {
      "nickname": "kike01",
      "email": "kike01@mail.com",
      "password": "1234",
      "telefono": "55555555",
      "fechaNacimiento": "2000-01-01",
      "pais": "Guatemala",
      "rol": "GAMER"
    }
    ```

---

### 5.4 Frontend (Angular)

1.  **Instalar dependencias:**
    Dentro de la carpeta del frontend:
    ```bash
    npm install
    ```

2.  **Configurar conexión al Backend:**
    Edita el archivo `src/environments/environment.ts`:
    ```typescript
    export const environment = {
      production: false,
      apiBaseUrl: 'http://localhost:8080/vaqueras-backend/api'
    };
    ```

3.  **Ejecutar en desarrollo:**
    ```bash
    ng serve -o
    ```
    La aplicación se abrirá en: [http://localhost:4200](http://localhost:4200)

4.  **Configuración de CORS y Credenciales:**
    * **Frontend (Angular):** Asegurar `withCredentials: true` en los interceptors o peticiones HTTP si usas cookies/sesión.
    * **Backend (Java):** Habilitar CORS para el origen `http://localhost:4200` y permitir credenciales.

---

### 5.5 Despliegue “tipo producción”

#### Frontend (Build estático)
Genera los archivos optimizados para producción:
```bash
ng build --configuration production