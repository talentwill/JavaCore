package core2.chapter08.demo2;

public class Book extends Product {
    private String isbn;

    Book(String title, String isbn, double price) {
        super(title, price);
        this.isbn = isbn;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " " + isbn;
    }
}
