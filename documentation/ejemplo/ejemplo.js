console.log("Hola mundo");

function suma(a, b) {
    if(a >2 ){
        console.log("El primer número es mayor que 2");
    }
    return a + b;
}

console.log("La suma de 7 y 3 es:", suma(7, 3));

//crea una funcion que divida 2 numeros
function dividir(a, b) {
    if(b === 0){
        console.log("Error: No se puede dividir entre cero");
        return null;
    }
    return a / b;
}
console.log("La división de 10 entre 2 es:", dividir(10, 2));