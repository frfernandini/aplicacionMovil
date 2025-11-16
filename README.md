# Levelup - Aplicación de E-Commerce

Este repositorio contiene el código fuente de la aplicación Android para Levelup, una plataforma de e-commerce.

**Integrantes:**
*   Franco Fernandini
*   Johao Candia

---

## Funcionalidad

Levelup es una aplicación móvil nativa para Android que permite a los usuarios:

*   **Autenticación**: Registrar nuevas cuentas de usuario e iniciar sesión de forma segura.
*   **Catálogo de Productos**: Visualizar una lista completa de productos disponibles, filtrarlos por categoría y buscar.
*   **Carrito de Compras**: Añadir, quitar y modificar la cantidad de productos en el carrito.
*   **Proceso de Checkout**: Finalizar una compra, proporcionando una dirección de envío y creando un pedido en el sistema.
*   **Gestión de Perfil**: Ver la información del perfil y cerrar la sesión de forma segura.

---

## Endpoints Utilizados

La aplicación se comunica con un backend a través de una API REST. Los siguientes son los endpoints consumidos.

| Verbo  | Endpoint                                           | Descripción                                    |
| :----- | :------------------------------------------------- | :--------------------------------------------- |
| `POST` | `/api/auth/registro`                               | Registra un nuevo usuario.                     |
| `POST` | `/api/auth/login`                                  | Inicia sesión y obtiene un token de autenticación. |
| `GET`    | `/api/productos`                                   | Obtiene la lista completa de productos.        |
| `GET`    | `/api/carrito/{usuarioId}`                         | Obtiene los productos en el carrito de un usuario. |
| `POST`   | `/api/carrito/{usuarioId}/{productoId}`            | Agrega un producto al carrito.                 |
| `DELETE` | `/api/carrito/{usuarioId}/{productoId}`            | Quita un producto del carrito.                 |
| `POST`   | `/api/carrito/increase/{usuarioId}/{productoId}`   | Aumenta la cantidad de un producto en el carrito. |
| `POST`   | `/api/carrito/decrease/{usuarioId}/{productoId}`   | Disminuye la cantidad de un producto en el carrito.|
| `DELETE` | `/api/carritovacio/{usuarioId}`                    | Vacía completamente el carrito de un usuario.  |
| `POST`   | `/api/pedidos`                                     | Crea un nuevo pedido con los items del carrito.  |

---

## Pasos para Ejecutar el Proyecto

Para compilar y ejecutar la aplicación, sigue estos pasos:

1.  **Clonar el Repositorio**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```

2.  **Configurar el Backend**

    Esta aplicación requiere una instancia del backend de Levelup funcionando y accesible.

    *   Asegúrate de que el backend esté desplegado (por ejemplo, en AWS Elastic Beanstalk).
    *   Localiza el archivo `app/src/main/java/com/example/aplicacion/data/remote/RetrofitInstance.kt`.
    *   Modifica la constante `BASE_URL` para que apunte a la URL pública de tu backend.

        ```kotlin
        // Ejemplo:
        private const val BASE_URL = "http://levelup.us-east-1.elasticbeanstalk.com/"
        ```

3.  **Configurar la Seguridad de Red (si es necesario)**

    *   Si tu backend se ejecuta en `http` (no encriptado), asegúrate de que el dominio esté en la lista de permitidos.
    *   Ve a `app/src/main/res/xml/network_security_config.xml`.
    *   Añade el dominio de tu backend.

        ```xml
        <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">tu-dominio-de-backend.com</domain>
        </domain-config>
        ```
    *   **Nota**: Para un despliegue a producción, es altamente recomendable que el backend utilice `https`.

4.  **Compilar y Ejecutar**

    *   Abre el proyecto en Android Studio.
    *   Espera a que Gradle sincronice todas las dependencias.
    *   Selecciona un emulador o un dispositivo físico y presiona "Run".

