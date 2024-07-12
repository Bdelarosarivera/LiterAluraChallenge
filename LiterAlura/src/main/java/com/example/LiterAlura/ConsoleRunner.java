package com.example.LiterAlura;

import com.example.LiterAlura.client.GutendexClient;
import com.example.LiterAlura.model.Author;
import com.example.LiterAlura.model.Book;
import com.example.LiterAlura.service.AuthorService;
import com.example.LiterAlura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GutendexClient gutendexClient;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        boolean running = true;

        while (running) {
            mostrarMenu();

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea después de la opción

                switch (option) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarTodosLosLibros();
                        break;
                    case 3:
                        listarLibrosPorIdioma();
                        break;
                    case 4:
                        listarAutores();
                        break;
                    case 5:
                        listarAutoresVivosEnAnio();
                        break;
                    case 6:
                        System.out.println("¡Hasta luego!");
                        running = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Inténtelo de nuevo.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }

        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar todos los libros");
        System.out.println("3. Listar libros por idioma");
        System.out.println("4. Listar autores");
        System.out.println("5. Listar autores vivos en un año específico");
        System.out.println("6. Salir");
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro:");
        String title = scanner.nextLine();
        gutendexClient.searchBookByTitle(title)
                .subscribe(
                        book -> {
                            bookService.saveBook(book);
                            System.out.println("Libro encontrado:");
                            System.out.println(book);
                        },
                        error -> {
                            System.out.println("Libro no encontrado o error: " + error.getMessage());
                        }
                );
    }

    private void listarTodosLosLibros() {
        List<Book> books = bookService.getAllBooks();
        System.out.println("Listado de todos los libros:");
        books.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma:");
        String language = scanner.nextLine();
        List<Book> booksByLanguage = bookService.getBooksByLanguage(language);
        if (booksByLanguage.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            System.out.println("Listado de libros en " + language + ":");
            booksByLanguage.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        List<Author> authors = authorService.getAllAuthors();
        System.out.println("Listado de todos los autores:");
        authors.forEach(System.out::println);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año:");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea después del número
        List<Author> livingAuthors = authorService.getLivingAuthorsInYear(year);
        if (livingAuthors.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año especificado.");
        } else {
            System.out.println("Listado de autores vivos en " + year + ":");
            livingAuthors.forEach(System.out::println);
        }
    }
}
