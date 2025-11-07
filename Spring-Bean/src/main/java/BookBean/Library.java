package BookBean;

public class Library {
    private String libraryName;
    private Book book;

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void showLibraryDetails(){
        System.out.println("Library Name: "+getLibraryName());
        System.out.println("Book Name: "+book.getBookName());
        System.out.println("Author Name: "+book.getAuthorName());
    }
}
