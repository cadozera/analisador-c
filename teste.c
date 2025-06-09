#include <stdio.h>
#include <math.h>

int potencia(int base, int expoente) {
    int resultado = 1;
    while (expoente > 0) {
        resultado *= base;
        expoente--;
    }
    return resultado;
}

int main() {
    int a = 2;
    int b = 5;
    int c = potencia(a, b);

    float raiz = sqrt((float)c);

    if (c > 10) {
        printf("Resultado: %d\n", c);
    } else {
        printf("Muito pequeno\n");
    }

    raiz = (raiz > 10.0f) ? raiz : 10.0f;

    printf("Raiz ajustada: %.2f\n", raiz);

    return 0;
}
