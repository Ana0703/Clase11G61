package co.edu.utp.misiontic2022.c2.bookshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager implements AutoCloseable {
    private Connection connection;

    public DBManager() throws SQLException {
        connect();
    }

    private void connect() throws SQLException {
        // TODO: program this method
        //Cambie la ruta para que puede ver mi base de datos y que este en el repositorio
        var url="jdbc:sqlite:C:/Users/Ana Maria/Desktop/MISIONTIC 2022/Ruta 2/CICLO 2/Semana_4/G61/clase11-bookshop/BookShop.db";
        connection = DriverManager.getConnection(url);
    }

    /**
     * Close the connection to the database if it is still open.
     *
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = null;
    }

     /**
     * Return the number of units in stock of the given book.
     *
     * @param book The book object.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     * @throws SQLException If somthing fails with the DB.
     */
    public int getStock(Book book) throws SQLException {
        return getStock(book.getId());
    }

    /**
     * Return the number of units in stock of the given book.
     *
     * @param bookId The book identifier in the database.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     */
    public int getStock(int bookId) throws SQLException {
        int r=0;
        try (var u=connection.prepareStatement("Select existencia From unidades Where id_libro=?")){
            u.setInt(1, bookId);
            try(var rs=u.executeQuery()){
                if (rs.next()){
                    r=rs.getInt("existencia");
            }
            
            }
        }
    

        return r;
    }

    /**
     * Search book by ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The Book object, or null if not found.
     * @throws SQLException If somthing fails with the DB.
     */
    public Book searchBook(String isbn) throws SQLException {
        // TODO: program this method
        Book resp=null;// genero un variable resp nula desde Book no la tabla, si no la clase.
        Statement u = null;// elemento de statemt u
        ResultSet rs = null;// Resultset rs
        try{
            u=connection.createStatement();
            rs=u.executeQuery("Select * From Book Where isbn='"+isbn+"'");
            //selecciono todos los elementos de Book tabla donde el isbn sea igual
            //isbn que paso al metodo searchBook(String isbn)
            if(rs.next()){
                resp=new Book();
                //rs.getInt("id")
                //resultset genera rs, se va a parar en id de la tabla, por ello id se debe
                //escribir como esta en la tabla
                resp.setId(rs.getInt("id"));
                //resp que es un instacia de Book clase, pone directamente en el metodo setID, 
                //el numero que se tiene id de la tabla.

                resp.setIsbn(rs.getString("isbn"));

                resp.setTitle(rs.getString("title"));
                resp.setYear(rs.getInt("year"));
            }
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (u!=null){
                u.close();
            }
        }

        return resp;
    }

    /**
     * Sell a book.
     *
     * @param book The book.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If somthing fails with the DB.
     */
    public boolean sellBook(Book book, int units) throws SQLException {
        return sellBook(book.getId(), units);
    }

    /**
     * Sell a book.
     *
     * @param book The book's identifier.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If something fails with the DB.
     */
    public boolean sellBook(int book, int units) throws SQLException {
        // TODO: program this method
        return false;
    }

    /**
     * Return a list with all the books in the database.
     *
     * @return List with all the books.
     * @throws SQLException If something fails with the DB.
     */
    public List<Book> listBooks() throws SQLException {
        // TODO: program this method
        // creo una variable list de tipo arrayList 
        List<Book> resp=new ArrayList<>();
        try (var u=connection.createStatement();
            var rs=u.executeQuery("Select * From Book")){
                while (rs.next()) {
                    var e=new Book();
                    e.setId(rs.getInt("id"));
                    e.setIsbn(rs.getString("isbn"));
                    e.setTitle(rs.getString("title"));
                    e.setYear(rs.getInt("year"));
                    resp.add(e);
            }

            
        }
        return resp;
    }
}
