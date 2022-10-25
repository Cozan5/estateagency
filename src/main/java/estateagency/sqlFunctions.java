/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estateagency;
import java.sql.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.String;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author franm
 */
public class sqlFunctions {
    Statement stmt = null;
    Connection conn =null;

    public void connection(){
      try{
         Class.forName("oracle.jdbc.OracleDriver");
         conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
         stmt = conn.createStatement();       
         System.out.println("Connection Successful");
      }
      catch(Exception E){
         System.err.println("Unable to load driver");
         System.err.println("Connection failed");
         E.printStackTrace();
      }  
   }
  
   Connection getConn(){
      connection();
      return conn;
   }
         
 public void read(String table){  
      try{  
          
         String path = "Select * from "+table;
         conn= getConn();
         ResultSet rs = stmt.executeQuery(path);
         while( rs.next()) {
            String refNo = rs.getString("refferenceno");
            String clientname = rs.getString("clientname");
            String idno = rs.getString("idNo");
            String returndate = rs.getString("returndate");
            System.out.println(refNo + "  " + clientname +" "+ idno + "  " + returndate+ "  ");
            
         }
         stmt.close();
         conn.close();
      }
      catch(SQLException ex) {
         System.err.println("SQLException: " + ex.getMessage());
      }
   }
   
   //updates new books to the database
   public void addClient(String refNo, String clientname, String homeno, String street, String status)throws IOException{  
      try{  
         
         conn = getConn();
         
        PreparedStatement pstat = conn.prepareStatement("INSERT INTO Client (refferenceno,clientname,homeno,street,status) Values(?,?,?,?,?)");
        
        pstat.setString(1, refNo);
        pstat.setString(2, clientname);
        pstat.setString(3,homeno);
         //ava.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
        pstat.setString(4, street);
        pstat.setString(5,status);
        pstat.executeUpdate();
                      
         System.out.println("Client added"); 
         stmt.close();
         conn.close();
      }
      catch(SQLException ex) {
         System.err.println("SQLException: " + ex.getMessage());
      }
   }  
   
   //add property
   public void addProperty(String owner, String propertyAddress, String type, int norooms, String salestatus)throws IOException{  
      try{  
         
         conn = getConn();
         //check if owner exists 
         String query = " SELECT * FROM CLIENT where refferenceno ='"+owner+"'";           
         System.out.println(query);     
         PreparedStatement pstat = conn.prepareStatement(query);
         
         ResultSet rs = pstat.executeQuery();
         boolean found = false;
         while(rs.next())
         {
             found = true;
         }
         //if owner exists add otherwise display error
         if(found)
         {
            pstat = conn.prepareStatement("INSERT INTO Property (property_address,refferenceno,roomtype,noOfrooms,statusofsale) Values(?,?,?,?,?)");
        
            pstat.setString(1, propertyAddress);
            pstat.setString(2,owner );
            pstat.setString(3,type);
             //ava.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            pstat.setInt(4, norooms);
            pstat.setString(5,salestatus);
            pstat.executeUpdate();
                      
            System.out.println("property added"); 
            stmt.close();
            conn.close();
         }
         else
         {
             JOptionPane.showMessageDialog(null, "owner doesnt exist do register them first");
         }
         
      }
      catch(SQLException ex) {
         System.err.println("SQLException: " + ex.getMessage());
      }
   }
   
   public void propertyavailable(String address)
   {
      try{  
         
         conn = getConn();
         //check if owner exists 
         String query = " SELECT * FROM Property where property_address ='"+address+"'";           
         System.out.println(query);     
         PreparedStatement pstat = conn.prepareStatement(query);
         
         ResultSet rs = pstat.executeQuery();
         boolean found = false;
         while(rs.next())
         {
            found = true;
            System.out.println("property has been found");
         }
         //if owner exists add otherwise display error
         if(found)
         {
            System.out.println("checking available list");
            query = " SELECT * FROM Property where property_address = '"+address+"' AND statusofsale ='for sale'";           
            System.out.println(query);     
            pstat = conn.prepareStatement(query);
         
            rs = pstat.executeQuery();
            found = false;
            while(rs.next())
            {
               JOptionPane.showMessageDialog(null, "Property "+address+" it still available ");
               found = true;
            }
            
           
            if(found == false)
            {
               System.out.println("checking sold list");
               query = " SELECT * FROM Property where property_address = '"+address+"' AND statusofsale ='sold'";           
               System.out.println(query);     
               pstat = conn.prepareStatement(query);
            
               rs = pstat.executeQuery();
               found = false;
               while(rs.next())
               {
                  JOptionPane.showMessageDialog(null, "Property "+address+" has been sold ");
                  found = true;
               }
            }
            else
            {
             //This property has been sold
             
               JOptionPane.showMessageDialog(null, "Property "+address+" doesnt exist ");
            }
         }
          
         else
         {
             //This property has been sold
             
            JOptionPane.showMessageDialog(null, "Property "+address+" doesnt exist ");
         }
         
      }
      catch(SQLException ex) {
         System.err.println("SQLException: " + ex.getMessage());
      }
   }
   
   public void addvisitation(String propertyAddress, String owner, String state)
   {
      try{  
         
         conn = getConn();
         //check if owner exists 
         String query = " INSERT INTO Propertyvisit(property_address,refferenceno,visitorstatus) Values(?,?,?)";           
         System.out.println(query);     
         PreparedStatement pstat = conn.prepareStatement(query);
        
         pstat.setString(1, propertyAddress);
         pstat.setString(2,owner );
         pstat.setString(3,state);
         pstat.executeUpdate();   
                      
         System.out.println("propertyvisits added"); 
         stmt.close();
         conn.close();
         
      }
      catch(SQLException ex) {
         System.err.println("SQLException: " + ex.getMessage());
      }
   }
   
   public void propertyList(DefaultTableModel model)
   {
           
         try {
            Class.forName("java.sql.DriverManager"); // Getting Driver class
            conn = getConn();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Property";
            ResultSet rs=stmt.executeQuery(query);
            ArrayList<String> list = new ArrayList<>(); 
            int views = 0;
            while(rs.next()) { // While there are still queries being returned, get individual results & store them in new variables
                String property_address = rs.getString("property_address");
                list.add(property_address);
                
                //
            }
            
            for (int i=0; i<list.size(); i++) 
            { 
                String prop = list.get(i);
                //Query to get the number of rows in a table
                query = "select count(*) from propertyvisit where property_address = '"+prop+"' AND visitorstatus = 'Interested'";
                System.out.println(query);
                //Executing the query
                rs = stmt.executeQuery(query);
                //Retrieving the result
                rs.next();
                views = rs.getInt(1);
                System.out.println("Number of records in the cricketers_data table: "+views);
                model.addRow(new Object[] {prop, views}); // Populate table with search results.
                views =0;
            }
            rs.close(); // Close connection
            stmt.close();
            conn.close();
        } catch (Exception e) { // Should there be an error in retrieveing database entries e.g. Failed to connect to database.
            JOptionPane.showMessageDialog(null, e.getMessage());
            
        }
   }
}
