
package com.mycompany.biblioteca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.DrbgParameters;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Biblioteca_kv {

    public static int espaciosLlenos; //Esta variable me puede servir para tener controlado la cantidad de espacios ocupados.

    public static void main(String[] args) {
        int cantLibro = 0; //Lo utilizare para controlar la cantidad de los libros 

        Scanner sc = new Scanner(System.in);

        //Mi array de biblioteca
        String[][] biblioteca = new String[50][5]; //mi biblioteca tiene como limite 50

        String[] partes = datosLibros().split(","); //carga los libros dividiendo los datos por ","
        int numLibros = (partes.length - 3) / 3;  // Descontamos los encabezados

        int index = 3;  // Empezamos después de los encabezados

        //Los libros del string se cargan a mi biblioteca.
        for (int i = 0; i < numLibros; i++) {
            biblioteca[i][0] = partes[index++]; // Título
            biblioteca[i][1] = partes[index++].replace("\"", ""); // Autor (remover comillas)
            biblioteca[i][2] = partes[index++]; // Año de publicación
            biblioteca[i][3] = generateRandomID();//ingresa el numero random id
            biblioteca[i][4] = generarCantidadLibros();//se inserta el numero de libros del 1 al 15
        }

        
        espaciosLlenos = contarEspaciosLlenos(biblioteca); //toma la cantidad de espacios ocupados
        System.out.println("Espacios llenos: " + espaciosLlenos); //para saber cuantos espacios ocupados quedan

        boolean llave = true; //La utilizo para mantener abierto el bucle

        do {
            //Inicia el menu:
            System.out.println(""
                    + "\n************MENU*************\n"
                    + "1.Sacar libro.\n"
                    + "2.Devolver libro.\n"
                    + "3.Disponibilidad (Buscar).\n"
                    + "4.Ordenar.\n"
                    + "5.Listado.\n"
                    + "6.Añadir.\n"
                    + "7.Salir.\n");

            String respuesta = sc.nextLine();
            switch (respuesta) {
                case "1": //LISTO
                    //ESTA FORMA DE AGREGAR VALORES A UN LIBRO DEBO VOLVERLA UNA FUNCION
                    System.out.println("*****************Sacar libro.*****************");

                    System.out.println("Codigo del libro: ");
                    String codigo_sacar = sc.nextLine();

                    //comprobamos si existe
                    String[] resulArray = existeLibro(biblioteca, codigo_sacar, 3);
                    if (resulArray != null) {

                        for (int i = 0; i < espaciosLlenos; i++) {

                            //necesito comprobar que no este la cantidad igual a 0:
                            if (biblioteca[i][3].equals(resulArray[3])) {
                                if (string_a_int(biblioteca[i][4]) == 0) {
                                    System.out.println("Lo sentimos, al parecer hemos prestado todos los ejemplares,"
                                            + "\npero si vuelves otro dia quizas ya tengamos alguno devuelto!");
                                } else {
                                    System.out.println("LIBRO DISPONIBLE PARA PRESTAMO!\n");

                                    //  biblioteca[i][4] es donde esta la cantidad de libros 
                                    cantLibro = string_a_int(biblioteca[i][4]) - 1; //combierte la cantidad de libros en int, le resta uno y 
                                    biblioteca[i][4] = int_a_string(cantLibro); //modificamos el libro con el nuevo valor.

                                    //SOLO PARA MOSTRAR EL RESULTADO:
                                    // mostrarLibros(biblioteca);
                                    System.out.println("*Datos de prestamo:*"
                                            + "\nLibro: " + biblioteca[i][0] + ""
                                            + "\nAutor : " + biblioteca[i][1] + ""
                                            + "\nCantidad restante: " + biblioteca[i][4] + ""
                                            + "\n Vuelva pronto!");
                                    // System.out.println("Cantidad restante: " + cantLibro);
                                    System.out.println("***********************");
                                }
                            }

                            cantLibro = 0; //vuelve a 0 para que no guarde el ultimo dato

                        }

                    } else {
                        System.out.println("No tenemos ese ejemplar!");
                    }
                    break;

                case "2": //LISTO
                    System.out.println("Devolver libro.");

                    System.out.println("Codigo del libro: ");
                    String codigo_devolver = sc.nextLine();

                    //comprobamos si existe
                    String[] resulArray2 = existeLibro(biblioteca, codigo_devolver, 3);
                    if (resulArray2 != null) {

                        for (int i = 0; i < espaciosLlenos; i++) {
                            if (biblioteca[i][3].equals(resulArray2[3])) {
                                System.out.println("LIBRO DISPONIBLE PARA PRESTAMO!\n");

                                //  biblioteca[i][4] es donde esta la cantidad de libros 
                                cantLibro = string_a_int(biblioteca[i][4]) + 1; //combierte la cantidad de libros en int, le suma uno y 
                                biblioteca[i][4] = int_a_string(cantLibro); //modificamos el libro con el nuevo valor.

                                //SOLO PARA MOSTRAR EL RESULTADO:
                                // mostrarLibros(biblioteca);
                                System.out.println("*Datos de prestamo:*"
                                        + "\nLibro: " + biblioteca[i][0] + ""
                                        + "\nAutor : " + biblioteca[i][1] + ""
                                        + "\nCantidad: " + biblioteca[i][4] + ""
                                        + "\n Vuelva pronto!");
                                // System.out.println("Cantidad restante: " + cantLibro);
                                System.out.println("***********************");
                            }
                        }
                        cantLibro = 0; //vuelve a 0 para que no guarde el ultimo dato
                    } else {
                        System.out.println("El libro que intentas ingresar al parecer no es nuestro.");
                    }
                    break;

                case "3": //LISTO
                    System.out.println("Disponibilidad / Buscar.");
                    disponibilidad(biblioteca, sc);
                    break;

                case "4":
                    /*
                    -Alfabetico autor
                    -Antiguedad (mas antiguio primero)
                    -Antiguedad (mas nuevo primero)
                     */
                    System.out.println("Ordenar por:"
                            + "\nA) Antiguedad (mas antiguo primero)"
                            + "\nB) Antiguedad (mas reciente primero)"
                            + "\nC) Alfabetico (autor)");
                    String orden = sc.nextLine();
                    if (orden.equalsIgnoreCase("A")) {
                        String[][] ordenDescendente_biblio = ordenarPorAñoDescendente(biblioteca);
                        mostrarLibros(ordenDescendente_biblio);
                    } else if (orden.equalsIgnoreCase("B")) {
                       
                        String[][] orden_ascenden_biblio = ordenarPorAñoAscendente(biblioteca);
                        mostrarLibros(orden_ascenden_biblio);
                    } else if (orden.equalsIgnoreCase("C")) {
                        String[][] orden_autor_biblio = ordenarPorAutor(biblioteca);
                        mostrarLibros(orden_autor_biblio);
                    } else {
                        System.out.println("Tu respuesta no esta en el menu...");
                    }

                    break;

                case "5": //LISTO
                    System.out.println("Listado.");
                    mostrarLibros(biblioteca); //Llama la funcion que muestra los libros guardados
                    break;

                case "6"://LISTO
                    System.out.println("\n****Añadir******");
                    // Llenar el array tridimensional
                    System.out.println("titulo: ");
                    String titulo = sc.nextLine();
                    System.out.println("Autor: ");
                    String autor = sc.nextLine();
                    System.out.println("Año: ");
                    String anio = sc.nextLine();

                    //el libro se agregara despues del ultmo espacio ocupado.
                    biblioteca[espaciosLlenos + 1][0] = titulo; // Título
                    biblioteca[espaciosLlenos + 1][1] = autor; // Autor (remover comillas)
                    biblioteca[espaciosLlenos + 1][2] = anio; // Año de publicación
                    biblioteca[espaciosLlenos + 1][3] = generateRandomID();//ingresa el numero random id
                    biblioteca[espaciosLlenos + 1][4] = generarCantidadLibros();//se inserta el numero de libros del 1 al 15
                    mostrarLibros(biblioteca); //Los lista para comprobar que se haya ingresado

                    espaciosLlenos++; //para saber que el espacio ocupado es +1
                    break;

                case "7"://LISTO
                    System.out.println("Salir.");
                    llave = false;
                    break;
                default:// LISTO
                    System.out.println("Respuesta fuera del menu.");
                    break;
            }
        } while (llave);

        sc.close();

    }

    /**
     * pasa un int a string
     *
     * @param numero
     * @return numero en modo string
     */
    public static String int_a_string(int numero) {
        return String.valueOf(numero);
    }

    /**
     * Pasa un numero de string a int
     *
     * @param numeroComoTexto
     * @return string como int
     */
    public static int string_a_int(String numeroComoTexto) {
        // Convertir el string a int
        return Integer.parseInt(numeroComoTexto);
    }

    /**
     * Funcion para saber cuantos espacios llenos tengo
     *
     * @param biblioteca
     * @return espacios llenos
     */
    public static int contarEspaciosLlenos(String[][] biblioteca) {
        int contador = 0;
        for (int i = 0; i < biblioteca.length; i++) {
            if (biblioteca[i][0] != null) {  // verificamos a partir del 0 cuantos espacios hay disponible, para cada primer valor
                contador++;
            }
        }
        return contador;//devuelve el resuldo cuando sea igual a null.
    }

    /**
     * String con libros con los que inicia la biblioteca
     *
     * @return string de libros
     */
    public static String datosLibros() {
        String datosLibros = "Titulo,Autor,Año de publicacion,"
                + "El Señor de los Anillos: La Comunidad del Anillo,\"J.R.R. Tolkien\",1954,"
                + "Harry Potter y la piedra filosofal,\"J.K. Rowling\",1997,"
                + "El nombre del viento,\"Patrick Rothfuss\",2007,"
                + "El bosque oscuro,\"C.S. Lewis\",1950,"
                + "La princesa prometida,\"William Goldman\",1973,"
                + "La materia oscura,\"Philip Pullman\",1995,"
                + "La Torre Oscura: El Pistolero,\"Stephen King\",1982,"
                + "El juego de Ender,\"Orson Scott Card\",1985,"
                + "La espada de la verdad,\"Terry Goodkind\",1994,"
                + "Las crónicas de Narnia: El león - la bruja y el ropero,\"C.S. Lewis\",1950,"
                + "American Gods,\"Neil Gaiman\",2001,"
                + "Un mago de Terramar,\"Ursula K. Le Guin\",1968,"
                + "El ciclo de Terramar,\"Ursula K. Le Guin\",1972,"
                + "El último unicornio,\"Peter S. Beagle\",1968,"
                + "La Rueda del Tiempo: El Ojo del Mundo,\"Robert Jordan\",1990,"
                + "El archivo de las tormentas,\"Brandon Sanderson\",2010,"
                + "Nunca donde,\"Neil Gaiman\",1996,"
                + "El último deseo,\"Andrzej Sapkowski\",1993,"
                + "Crónicas marcianas,\"Ray Bradbury\",1950,"
                + "Fahrenheit 451,\"Ray Bradbury\",1953";
        return datosLibros;
    }

    /**
     * Muestra los libros guardados en la biblioteca
     *
     * @param biblioteca
     */
    public static void mostrarLibros(String[][] biblioteca) {
        for (int i = 0; i < biblioteca.length; i++) {
            if (biblioteca[i][0] != null) {  // Verifica que haya datos en la posición
                System.out.println("Libro #" + (i + 1)); //Para numerar los libros
                System.out.println("Título: " + biblioteca[i][0]); //Posicion del titulo
                System.out.println("Autor: " + biblioteca[i][1]);//Posicion del titulo
                System.out.println("Año de Publicación: " + biblioteca[i][2]);//Posicion del titulo
                System.out.println("ID: " + biblioteca[i][3]);//Posicion del titulo
                System.out.println("Cantidad de Libros: " + biblioteca[i][4]);//Posicion del titulo
                System.out.println("-----------------------------");
            }
        }

    }

    /**
     * Genera numero del 1 al 10 para darle la cantidad de libros
     *
     * @return numero random
     */
    public static String generarCantidadLibros() {
        Random random = new Random();
        return String.valueOf(random.nextInt(15) + 1);
        // Genera un número entre 1 y 15, el valor se
        // devuelve como string para poder meterlo al array 
    }

    /**
     * Genera un valor ID radom Este valor se utilizara para diferenciar cada
     * libro.
     *
     * @return String de 8 caracteres
     */
    private static String generateRandomID() {
        return UUID.randomUUID().toString().substring(0, 8); // ID único de 8 caracteres
    }

    /**
     * Buscar disponibilidad Verifica la existencia de un libro por autor o
     * titulo
     *
     * @param biblioteca contiene todos los libros
     * @param sc utilizado para pedir datos
     */
    public static void disponibilidad(String[][] biblioteca, Scanner sc) {

        int campo = 0;
        String info;
        System.out.println("*********SELECCIONA*********"
                + "\nA) Para buscar por TITULO"
                + "\nB) para buscar por AUTOR"
                + "\nVolver (marque una letra distinta)"
                + "\n(escribir solo letra... ej: 'A,B,C')");

        String respuesta = sc.nextLine();
        if (respuesta.equalsIgnoreCase("A")) {
            campo = 0; //buscar por TITULO
            System.out.println("TITULO: ");
            info = sc.nextLine(); //Pedimos el titulo

            //esos datos entran en la funcion existeLibro(), que es la encargada de buscar y nos indica si existe o no.
            String[] resultado = existeLibro(biblioteca, info, campo); //el resultado se guarda en un array
            encontrado(resultado); //esta funcion toma el array y muestra el resultado por pantalla (para no repetir codigo)

        } else if (respuesta.equalsIgnoreCase("B")) {
            campo = 1; //buscar por AUTOR
            System.out.println("AUTOR: ");
            info = sc.nextLine();

            String[] resultado = existeLibro(biblioteca, info, campo);
            encontrado(resultado);

        } else {
            System.out.println("volver a inicio...");

        }

    }

    /**
     * Muestra resultado de la busqueda del libro.
     *
     * @param resultado libro encontrado o no.
     */
    public static void encontrado(String[] resultado) {
        if (resultado != null) {
            System.out.println("Título: " + resultado[0]);
            System.out.println("Autor: " + resultado[1]);
            System.out.println("Año: " + resultado[2]);
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    /**
     *
     * @param biblioteca contendra los libros de la biblioteca
     * @param buscarValor string con lo que queremos buscar en la biblio
     * @param campo //campo:0 = titulo, 1 = autor
     * @return true SI existe / false NO existe
     */
    public static String[] existeLibro(String[][] biblioteca, String buscarValor, int campo) {

        //busca por titulo / autor 
        for (int i = 0; i < biblioteca.length; i++) {
            if (biblioteca[i][0] != null && biblioteca[i][campo].equals(buscarValor)) {
                return biblioteca[i];  // Devuelve la fila completa si encuentra el libro
            }

        }
        return null;

    }

    /**
     * Función para ordenar el array por el año mas antiguo
     *
     * @param array
     * @return array ordenado
     */
    public static String[][] ordenarPorAñoDescendente(String[][] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                // Verificar que el valor en el índice 2 (año) no sea nulo o vacío
                String añoStr = array[j][2];
                String añoSiguienteStr = array[j + 1][2];

                // Si alguno de los años es nulo o vacío, lo podemos saltar o establecer un valor predeterminado
                if (añoStr == null || añoStr.isEmpty() || añoSiguienteStr == null || añoSiguienteStr.isEmpty()) {
                    continue;  // O manejar de otra forma el caso de valor nulo
                }

                // Convertir los valores de año a enteros
                int añoActual = Integer.parseInt(añoStr);
                int añoSiguiente = Integer.parseInt(añoSiguienteStr);

                if (añoActual < añoSiguiente) {  // Comparación para orden descendente
                    // Intercambiar las filas
                    String[] temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    /**
     * Ordena por año ascendente
     *
     * @param array
     * @return array ordenado por año
     */
    public static String[][] ordenarPorAñoAscendente(String[][] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                // Verificar que el valor en el índice 2 (año) no sea nulo o vacío
                String añoStr = array[j][2];
                String añoSiguienteStr = array[j + 1][2];

                // Si alguno de los años es nulo o vacío, lo podemos saltar 
                if (añoStr == null || añoStr.isEmpty() || añoSiguienteStr == null || añoSiguienteStr.isEmpty()) {
                    continue;  // O manejar de otra forma el caso de valor nulo
                }

                // Convertir los valores de año a enteros
                int añoActual = Integer.parseInt(añoStr);
                int añoSiguiente = Integer.parseInt(añoSiguienteStr);

                if (añoActual > añoSiguiente) {  // Comparación para orden ascendente
                    // Intercambiar las filas
                    String[] temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    /**
     * Ordenar por autor
     *
     * @param array
     * @return
     */
    public static String[][] ordenarPorAutor(String[][] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                // Obtener los autores, con comprobación para null
                String autorActual = array[j][1];
                String autorSiguiente = array[j + 1][1];

                // Si alguno de los autores es null, lo tratamos como menor alfabéticamente
                if (autorActual == null) {
                    autorActual = "";
                }
                if (autorSiguiente == null) {
                    autorSiguiente = "";
                }

                // Comparar alfabéticamente los autores
                if (autorActual.compareTo(autorSiguiente) > 0) {
                    // Intercambiar las filas si el autor actual es alfabéticamente mayor
                    String[] temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

}
