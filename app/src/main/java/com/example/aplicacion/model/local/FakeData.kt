package com.example.aplicacion.model.local

import com.example.aplicacion.R

val listaProductosEstaticos = mutableListOf(
    ProductoEntity(
        nombre = "Catan",
        descripcion = "Un clásico juego de estrategia donde los jugadores compiten por colonizar y"+
                " expandirse en la isla de Catan.",
        precio = 29990.0,
        imagen = R.drawable.catan,
        categoria = "Juegos de Mesa"
    ),
    ProductoEntity(
        nombre = "Carcassonne",
        descripcion = "Un juego de colocación de fichas donde los jugadores construyen el paisaje"+
                " alrededor de la fortaleza medieval de Carcassonne.",
        precio = 24990.0,
        imagen = R.drawable.carcassonne,
        categoria = "Juegos de Mesa "
    ),
    ProductoEntity(
        nombre = "Controlador Inalámbrico Xbox Series X",
        descripcion = " Ofrece una experiencia de juego cómoda con"+
                " botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.",
        precio = 59990.0,
        imagen = R.drawable.controller,
        categoria = "Accesorios"
    ),
    ProductoEntity(
        nombre = "Auriculares Gamer HyperX Cloud II",
        descripcion = "Proporcionan un sonido envolvente de calidad con un"+
                " micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad",
        precio = 79990.0,
        imagen = R.drawable.hyperx_cloud,
        categoria = "Accesorios"
    ),
    ProductoEntity(
        nombre = "PlayStation 5",
        descripcion = "La consola de última generación de Sony, que ofrece gráficos"+
                " impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.",
        precio = 549990.0,
        imagen = R.drawable.ps5,
        categoria = "Consolas"
    ),
    ProductoEntity(
        nombre = "PC Gamer ASUS ROG Strix",
        descripcion = "Un potente equipo diseñado para los gamers más exigentes,"+
                " equipado con los últimos componentes para ofrecer un rendimiento excepcional",
        precio = 1299990.0,
        imagen = R.drawable.pc_gamer,
        categoria = "Computadores Gamers"
    ),
    ProductoEntity(
        nombre = "Silla Gamer Secretlab Titan",
        descripcion = "Diseñada para el máximo confort, esta silla ofrece un soporte"+
                " ergonómico y personalización ajustable para sesiones de juego prolongadas.",
        precio = 349990.0,
        imagen = R.drawable.silla_gamer,
        categoria = "Sillas Gamers"
    ),
    ProductoEntity(
        nombre = "Mouse Gamer Logitech G502 HERO",
        descripcion = "Con sensor de alta precisión y botones"+
                " personalizables, este mouse es ideal para gamers que buscan un control preciso y"+
                " personalización.",
        precio = 49990.0,
        imagen = R.drawable.mouse,
        categoria = "Mouse"
    ),
    ProductoEntity(
        nombre = "Mousepad Razer Goliathus Extended Chroma",
        descripcion = " Ofrece un área de juego amplia con"+
                " iluminación RGB personalizable, asegurando una superficie suave y uniforme para el"+
                " movimiento del mouse.",
        precio = 29990.0,
        imagen = R.drawable.mousepad,
        categoria = "Mousepad"
    ),
    ProductoEntity(
        nombre = "Polera Gamer Personalizada 'Level-Up'",
        descripcion = " Una camiseta cómoda y estilizada, con la"+
                " posibilidad de personalizarla con tu gamer tag o diseño favorito.",
        precio = 14990.0,
        imagen = R.drawable.polera_gamer_life,
        categoria = "Poleras Personalizadas"
    )
)