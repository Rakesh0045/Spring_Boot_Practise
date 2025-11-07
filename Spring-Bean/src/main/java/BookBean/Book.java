package BookBean;

public class Book {
    private String authorName;
    private String bookName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void display(){
        System.out.println("Book Name: "+bookName);
        System.out.println("Author Name: "+authorName);
    }
}
