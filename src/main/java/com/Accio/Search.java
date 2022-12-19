//for making search table
package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//declaring java class as servlet.
//Servlet connects input from user through an HTML form, query records from a database and create web pages.
@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String keyword=request.getParameter("keyword");
        System.out.println(keyword);
        try{
            //connecting to database
            Connection connection=DatabaseConnection.getConnection();

            //executing query
            PreparedStatement preparedStatement=connection.prepareStatement("Insert into history values(?,?)");

            //setting values to the query
            preparedStatement.setString(1,keyword);
            preparedStatement.setString(2,"http://localhost:8080/SearchEngine/Search?keyword="+keyword);
            //updating the result
            preparedStatement.executeUpdate();

            //executing query and getting result from database
            ResultSet resultSet=connection.createStatement().executeQuery("select pageTitle,pageLink,(length(lower(pageData))-length(replace(lower(pageData),'"+keyword+"', \"\" )))/length('"+keyword+"') as countoccurence from pages order by countoccurence desc limit 30;");
            //creating ArrayList to store result
            ArrayList<SearchResult> results=new ArrayList<SearchResult>();
            while(resultSet.next()){
                SearchResult searchResult=new SearchResult();

                //getting title and link
                searchResult.setPageTitle(resultSet.getString("pageTitle"));
                searchResult.setPageLink(resultSet.getString("pageLink"));

                //adding above result to the arrayList
                results.add(searchResult);
               }
            //pass the servlet to jsp file
            request.setAttribute("results",results);

            //dispatching the request to jsp
            request.getRequestDispatcher("/search.jsp").forward(request,response);

            //sets the content type of response
            response.setContentType("text/html");

            //printWriter object to send character text
            PrintWriter out= response.getWriter();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }catch (ServletException servletException){
            servletException.printStackTrace();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

}
