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

Para compilar y ejecutar la aplicación, sigue estos pasos

1.  **Configurar el Backend**

    Esta aplicación esta conectada con un backend desplegado en beanstalk, en caso de que se quiera iniciar en local habria que configurar el archivo RetrofitInstance.kt


2.  **Compilar y Ejecutar**

    apk firmada disponible en el mismo repositorio, simplemente descargar y instalar en un dispositivo movil y funcionara sin problemas
    

