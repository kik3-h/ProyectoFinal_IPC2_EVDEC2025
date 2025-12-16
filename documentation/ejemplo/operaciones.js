function sumando() {
    let resulado = 0;
    let a = parseFloat(document.getElementById("a").value);
    let b = parseFloat(document.getElementById("b").value);
    let resultado = a + b;
    document.getElementById("resultado").innerText = "El resultado de la suma es: " + resultado;
}
function resta() {
    let resulado = 0;
    let a = parseFloat(document.getElementById("a").value);
    let b = parseFloat(document.getElementById("b").value);
    let resultado = a - b;
    document.getElementById("resultado").innerText = "El resultado de la resta es: " + resultado;
}
function multiplicar() {
    let resulado = 0;
    let a = parseFloat(document.getElementById("a").value);
    let b = parseFloat(document.getElementById("b").value);
    let resultado = a * b;
    document.getElementById("resultado").innerText = "El resultado de la multiplicación es: " + resultado;
}
function division() {
    let resulado = 0;
    let a = parseFloat(document.getElementById("a").value);
    let b = parseFloat(document.getElementById("b").value);
    if(b === 0){
        document.getElementById("resultado").innerText = "Error: No se puede dividir entre cero";
        return;
    }
    let resultado = a / b;
    document.getElementById("resultado").innerText = "El resultado de la división es: " + resultado;
}
