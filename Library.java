import java.util.*;
import java.util.stream.Collectors;

class Book {
    private String title;
    private String author;
    private String genre;
    private int year;
    private int pages;
    private boolean isRare;

    public Book(String title, String author, String genre, int year, int pages, boolean isRare) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.pages = pages;
        this.isRare = isRare;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public int getPages() {
        return pages;
    }

    public boolean isRare() {
        return isRare;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Genre: " + genre +
               ", Year: " + year + ", Pages: " + pages + ", Rare: " + (isRare ? "Yes" : "No");
    }
}

public class Library {
    private List<Book> books;

    public Library(List<Book> books) {
        this.books = books;
    }

    // Stream API implementations
    public Map<Boolean, List<Book>> divideByRarity() {
        return books.stream().collect(Collectors.partitioningBy(Book::isRare));
    }

    public Map<String, List<Book>> groupByGenre() {
        return books.stream().collect(Collectors.groupingBy(Book::getGenre));
    }

    public Map<String, String> averagePagesByGenre() {
        return books.stream()
                .collect(Collectors.groupingBy(Book::getGenre,
                        Collectors.averagingDouble(Book::getPages)))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> String.format("%.2f", entry.getValue())));
    }

    public List<Book> sortByYear() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getYear))
                .collect(Collectors.toList());
    }

    public List<Book> sortByPages() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getPages))
                .collect(Collectors.toList());
    }

    public Set<String> uniqueAuthors() {
        return books.stream()
                .map(Book::getAuthor)
                .collect(Collectors.toSet());
    }

    public Optional<Book> bookWithMostPages() {
        return books.stream()
                .max(Comparator.comparingInt(Book::getPages));
    }

    // Non-Stream API implementations
    public Map<Boolean, List<Book>> divideByRarityWithoutStream() {
        Map<Boolean, List<Book>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());

        for (Book book : books) {
            if (book.isRare()) {
                result.get(true).add(book);
            } else {
                result.get(false).add(book);
            }
        }
        return result;
    }

    public Map<String, List<Book>> groupByGenreWithoutStream() {
        Map<String, List<Book>> genreMap = new HashMap<>();
        for (Book book : books) {
            genreMap.computeIfAbsent(book.getGenre(), k -> new ArrayList<>()).add(book);
        }
        return genreMap;
    }

    public Map<String, String> averagePagesByGenreWithoutStream() {
        Map<String, List<Integer>> genrePages = new HashMap<>();
        for (Book book : books) {
            genrePages.computeIfAbsent(book.getGenre(), k -> new ArrayList<>()).add(book.getPages());
        }
        Map<String, String> averagePages = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : genrePages.entrySet()) {
            List<Integer> pagesList = entry.getValue();
            double total = 0;
            
            // Підрахунок загальної кількості сторінок
            for (Integer pages : pagesList) {
                total += pages;
            }
            
            double average = total / pagesList.size();
            averagePages.put(entry.getKey(), String.format("%.2f", average));
        }
        return averagePages;
    }

    public List<Book> sortByYearWithoutStream() {
        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort(Comparator.comparingInt(Book::getYear));
        return sortedBooks;
    }

    public List<Book> sortByPagesWithoutStream() {
        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort(Comparator.comparingInt(Book::getPages));
        return sortedBooks;
    }

    public Set<String> uniqueAuthorsWithoutStream() {
        Set<String> authors = new HashSet<>();
        for (Book book : books) {
            authors.add(book.getAuthor());
        }
        return authors;
    }

    public Book bookWithMostPagesWithoutStream() {
        Book maxBook = null;
        for (Book book : books) {
            if (maxBook == null || book.getPages() > maxBook.getPages()) {
                maxBook = book;
            }
        }
        return maxBook;
    }

    public static void main(String[] args) {
        List<Book> books = Arrays.asList(
                new Book("1984", "George Orwell", "Dystopian", 1949, 328, true),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", "Classic", 1925, 180, false),
                new Book("Moby Dick", "Herman Melville", "Classic", 1851, 635, true),
                new Book("Pride and Prejudice", "Jane Austen", "Romance", 1813, 279, false),
                new Book("Emma", "Jane Austen", "Romance", 1815, 432, true),
                new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, 277, true),
                new Book("Brave New World", "Aldous Huxley", "Dystopian", 1932, 268, false),
                new Book("The Picture of Dorian Gray", "Oscar Wilde", "Philosophical", 1890, 254, true),
                new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 1937, 310, false),
                new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, 281, false),
                new Book("The Alchemist", "Paulo Coelho", "Adventure", 1988, 208, true),
                new Book("Fahrenheit 451", "Ray Bradbury", "Dystopian", 1953, 160, false),
                new Book("The Grapes of Wrath", "John Steinbeck", "Historical", 1939, 464, true),
                new Book("Les Miserables", "Victor Hugo", "Historical", 1862, 1232, false),
                new Book("Jane Eyre", "Charlotte Bronte", "Classic", 1847, 507, false)
        );

        Library library = new Library(books);
        Scanner scanner = new Scanner(System.in);
        boolean continueChoosing = true;

        while (continueChoosing) {
            System.out.println("\nChoose the method of output:");
            System.out.println("1. Using Stream API");
            System.out.println("2. Without Stream API");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.println("\n1. Rare books:");
                Map<Boolean, List<Book>> dividedBooks = library.divideByRarity();
                dividedBooks.get(true).forEach(System.out::println);

                System.out.println("\nCommon books:");
                dividedBooks.get(false).forEach(System.out::println);

                System.out.println("\n2. Grouping by genre:");
                Map<String, List<Book>> groupedBooks = library.groupByGenre();
                groupedBooks.forEach((genre, booksList) -> {
                    System.out.println("\n" + genre + ":");
                    booksList.forEach(System.out::println);
                });

                System.out.println("\n3. Average number of pages by genre:");
                Map<String, String> averagePages = library.averagePagesByGenre();
                averagePages.forEach((genre, avg) -> System.out.println("Genre: " + genre + ", Average Pages: " + avg));

                System.out.println("\n4. Sorted books by year:");
                List<Book> sortedByYear = library.sortByYear();
                sortedByYear.forEach(System.out::println);

                System.out.println("\nSorted books by number of pages:");
                List<Book> sortedByPages = library.sortByPages();
                sortedByPages.forEach(System.out::println);

                System.out.println("\n5. Unique authors:");
                Set<String> authors = library.uniqueAuthors();
                authors.forEach(System.out::println);

                System.out.println("\n6. Book with the most pages:");
                Optional<Book> mostPagesBook = library.bookWithMostPages();
                if (mostPagesBook.isPresent()) {
                    Book book = mostPagesBook.get();
                    System.out.println("Title: \"" + book.getTitle() + "\", Pages: " + book.getPages());
                } else {
                    System.out.println("No books available.");
                }
            } else if (choice == 2) {
                System.out.println("\n1. Rare books:");
                Map<Boolean, List<Book>> dividedBooks = library.divideByRarityWithoutStream();
                dividedBooks.get(true).forEach(System.out::println);

                System.out.println("\nCommon books:");
                dividedBooks.get(false).forEach(System.out::println);

                System.out.println("\n2. Grouping by genre:");
                Map<String, List<Book>> groupedBooks = library.groupByGenreWithoutStream();
                groupedBooks.forEach((genre, booksList) -> {
                    System.out.println("\n" + genre + ":");
                    booksList.forEach(System.out::println);
                });

                System.out.println("\n3. Average number of pages by genre:");
                Map<String, String> averagePages = library.averagePagesByGenreWithoutStream();
                averagePages.forEach((genre, avg) -> System.out.println("Genre: " + genre + ", Average Pages: " + avg));

                System.out.println("\n4. Sorted books by year:");
                List<Book> sortedByYear = library.sortByYearWithoutStream();
                sortedByYear.forEach(System.out::println);

                System.out.println("\nSorted books by number of pages:");
                List<Book> sortedByPages = library.sortByPagesWithoutStream();
                sortedByPages.forEach(System.out::println);

                System.out.println("\n5. Unique authors:");
                Set<String> authors = library.uniqueAuthorsWithoutStream();
                authors.forEach(System.out::println);

                System.out.println("\n6. Book with the most pages:");
                Book mostPagesBook = library.bookWithMostPagesWithoutStream();
                if (mostPagesBook != null) {
                    System.out.println("Title: \"" + mostPagesBook.getTitle() + "\", Pages: " + mostPagesBook.getPages());
                } else {
                    System.out.println("No books available.");
                }
            } else if (choice == 3) {
                continueChoosing = false;
                System.out.println("Exiting the program.");
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }
}
