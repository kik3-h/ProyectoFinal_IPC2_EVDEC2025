DROP DATABASE IF EXISTS EjemploOrdenArticulos; -- esto para las pruebas constantes borro la anterior yc
CREATE DATABASE EjemploOrdenArticulos;
USE EjemploOrdenArticulos;

CREATE TABLE Clientes (
    ids_clientes INT PRIMARY KEY ,
    nombres_clientes VARCHAR(50) NOT NULL,
    ciudades VARCHAR(50) NOT NULL
);

CREATE TABLE Ordenes (
    ids_ordenes INT PRIMARY KEY,
    fechas DATE NOT NULL,
    ids_clientes INT NOT NULL,
    FOREIGN KEY (ids_clientes) REFERENCES Clientes(ids_clientes)
);

CREATE TABLE Telefonos (
    ids_telefonos INT PRIMARY KEY,
    numeros VARCHAR(20) NOT NULL
);

CREATE TABLE OrdenesTelefonos (
    ids_ordenes_telefonos INT PRIMARY KEY, 
    ids_ordenes INT NOT NULL,
    ids_telefonos INT NOT NULL,
    FOREIGN KEY (ids_ordenes) REFERENCES Ordenes(ids_ordenes),
    FOREIGN KEY (ids_telefonos) REFERENCES Telefonos(ids_telefonos)
);

CREATE TABLE Articulos (
    ids_articulos INT PRIMARY KEY,
    nombres_articulos VARCHAR(50) NOT NULL,
    precios DECIMAL(10, 2) NOT NULL
);

CREATE TABLE OrdenesArticulos (
    ids_ordenes INT NOT NULL,
    ids_articulos INT NOT NULL,
    cantidades INT NOT NULL,
    PRIMARY KEY (ids_ordenes, ids_articulos),
    FOREIGN KEY (ids_ordenes) REFERENCES Ordenes(ids_ordenes),
    FOREIGN KEY (ids_articulos) REFERENCES Articulos(ids_articulos)
);

INSERT INTO Clientes (ids_clientes, nombres_clientes, ciudades) VALUES
(101, 'Luis', 'Quetzaltenango'),
(107, 'Brisa', 'Huehuetenango'),
(110, 'Liseth', 'Reu');

INSERT INTO Ordenes (ids_ordenes, fechas, ids_clientes) VALUES
(2301, '2011-02-23', 101),
(2302, '2011-02-23', 107),
(2303, '2011-02-27', 110),
(2304, '2011-03-31', 110);

INSERT INTO Telefonos (ids_telefonos, numeros) VALUES
(1, '5565794'),
(2, '4896825'),
(3, '9898751'),
(4, '7749852'),
(5, '4784525'),
(6, '7745168');

INSERT INTO OrdenesTelefonos (ids_ordenes_telefonos, ids_telefonos, ids_ordenes) VALUES
(101, 1, 2301),
(102, 2, 2302), 
(103, 3, 2303),
(104, 4, 2301),
(105, 5, 2302),
(106, 6, 2303);

INSERT INTO Articulos (ids_articulos, nombres_articulos, precios) VALUES
(3786, 'Lapiz', 35.00),
(4011, 'Lapicero', 4.75),
(9132, 'Borr-1', 5.00),
(5794, 'Borr-2', 65.00),
(3141, 'Funda', 15.00); 

INSERT INTO OrdenesArticulos (ids_ordenes, ids_articulos, cantidades) VALUES
(2301, 3786, 3), 
(2301, 4011, 6), 
(2301, 9132, 8), 
(2302, 5794, 4), 
(2303, 4011, 2), 
(2303, 3141, 2),
(2304,3141,3); 
-- el inner join
SELECT 
    C.nombres_clientes,
    O.ids_ordenes,
    O.fechas,
    A.nombres_articulos,
    A.precios AS precio_unitario,
    OA.cantidades,
    (A.precios * OA.cantidades) AS total_linea
FROM 
    Clientes C
INNER JOIN 
    Ordenes O ON C.ids_clientes = O.ids_clientes
INNER JOIN 
    OrdenesArticulos OA ON O.ids_ordenes = OA.ids_ordenes
INNER JOIN 
    Articulos A ON OA.ids_articulos = A.ids_articulos
WHERE 
    C.ids_clientes = 101;