//for making history table

package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//declaring java class as servlet.
//Servlet connects input from user through an HTML form, query records from a database and create web pages.
@WebServlet("/History")

public class History extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
       try {
           //connecting to database
           Connection connection = DatabaseConnection.getConnection();

           //executing query and getting result from database
           ResultSet resultSet = connection.createStatement().executeQuery("Select * from history");

           //creating ArrayList to store history result
           ArrayList<HistoryResult> results = new ArrayList<HistoryResult>();
           while (resultSet.next()) {
               HistoryResult historyResult = new HistoryResult();

               //getting keyword and link
               historyResult.setKeyword(resultSet.getString("keyword"));
               historyResult.setLink(resultSet.getString("searchLink"));

               //adding above result to the arrayList
               results.add(historyResult);
           }
           //pass the servlet to jsp file
           request.setAttribute("results",results);
           //dispatching the request to jsp
           request.getRequestDispatcher("/history.jsp").forward(request,response);

       }catch (SQLException sqlException){
           sqlException.printStackTrace();
       }catch (ServletException servletException){
           servletException.printStackTrace();
       }catch (IOException ioException){
           ioException.printStackTrace();
       }
    }
}
