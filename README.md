# Prueba Control de Fichajes

## Configuración del entorno

### Back
Para montar el back es necesario disponer de Java 8 instalado.

> El proyecto usa lombok por lo que si se quiere abrir en un IDE es necesario configurarlo para poder usar lombok. [Página Oficinal](https://projectlombok.org/)

### Front
Para montar el front es necesario disponer de npm y angular-cli instalado (o npx).

## Probar aplicación

Para probar la aplicación es necesario levantar primero el back

``` bash
cd api
./mvnw spring-boot:run
```

Después levantar el front

``` bash
ng serve --open
```

