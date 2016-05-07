/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movielist;

/**
 *
 * @author Amish Naik
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 *
 * @author Amish Naik
 */
public class LoadDriver {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public LoadDriver(String address, String user, String password){
        try {
            conn =
               DriverManager.getConnection(address,user,password);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public ResultSet selectQuery(String query){
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        finally {
            //closeStatement();
        }
        return null;
    }
    
    public void insertQuery(String query){
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        finally {
            //closeStatement();
        }
    }
    
    public void prepareInsertMovieQuery(String insertQuery, String viewDate, String imdbID, String title, String relDate, String dir, String rnTm, String genresString){
        try{
            PreparedStatement pstmt = conn.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
            String string = viewDate;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = format.parse(string);
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            pstmt.setString(2, imdbID);
            pstmt.setString(3, title);
            pstmt.setString(4, relDate);
            pstmt.setString(5, dir);
            pstmt.setString(6, rnTm);
            pstmt.setString(7, genresString);
            pstmt.execute();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public int insertQueryReturnKey(String query, String groupName, int groupID, String fileName){
        try{
            PreparedStatement pstmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, groupName);
            pstmt.setInt(2, groupID);
            pstmt.setString(3, fileName);
            pstmt.execute();
            
            //stmt = conn.createStatement();
            //stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            int autoIncKey=0;

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                autoIncKey = rs.getInt(1);
            }
            return autoIncKey;
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return 0;
    }
    
    private void closeStatement(){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) { } // ignore

            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) { } // ignore

            stmt = null;
        }
    }
}
