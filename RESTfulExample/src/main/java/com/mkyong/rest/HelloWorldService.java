package com.mkyong.rest;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Path("/hello")
public class HelloWorldService {
    public Connection getConnected() throws SQLException{
     Connection con1=null;    
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/UsersDB");
            con1 = ds.getConnection();

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return con1;
    }
    @GET
    @Path("/producedepartment")
    @Produces("application/json")	
    public Department setDepartmentDetails() {
        Department department = new Department();
        department.setDeptId(109);
        department.setDeptName("cse");
        department.setDeptAddress("svec");
        return department; 
    }
    @GET
    @Path("/getdepartment")
    @Produces("application/json")
    public Department getDepartment() throws SQLException{
        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs = null;
        String output="";
        Department department = new Department();
        try {
            con = getConnected();
            String sql = "select * from department where deptid=101";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            int id = 0;
            String name = "";
            String add = "";
            while(rs.next()) {
                id = rs.getInt("deptid");
                name = rs.getString("deptname");
                add = rs.getString("deptaddress"); 
                
                department.setDeptId(id);
                department.setDeptName(name);
                department.setDeptAddress(add);           
            }
            output = " sucessfully selected";
        }
        catch(SQLException se) {
            output = se.toString();
        }
        catch(Exception e) {
            output = e.toString();
        }
        finally {
            pstmt.close();
            con.close();
        }
        return department;
    }	
    @GET
    @Path("/retrive")
    @Produces("application/json")
    public List retriveDepartment() throws SQLException{
        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs = null;
        String output="";
        List<Department> dlist = new ArrayList<Department>();
        try {
            con = getConnected();
            String sql = "select * from department";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            int id = 0;
            String name = "";
            String add = "";
            while(rs.next()) {
                id = rs.getInt("deptid");
                name = rs.getString("deptname");
                add = rs.getString("deptaddress");
                Department department = new Department();
                department.setDeptId(id);
                department.setDeptName(name);
                department.setDeptAddress(add);
                dlist.add(department);
            }
            output = " sucessfully selected";
        }
        catch(SQLException se) {
            output = se.toString();
        }
        catch(Exception e) {
            output = e.toString();
        }
        finally {
            pstmt.close();
            con.close();
        }
    return dlist; 
    }
    @GET
    @Path("/update")
    @Produces("text/html")
    public Response updateDepartment() throws SQLException{
        Connection con=null;
        PreparedStatement pstmt=null;
        String output="";
        try {
            con = getConnected();
            pstmt = con.prepareStatement("update department set deptaddress= ? where deptid= ?");
            pstmt.setString(1,"lbrce");
            pstmt.setInt(2,112);
            int var = pstmt.executeUpdate();
            output = "updated sucessfully";
        }
        catch(SQLException se) {
            output = se.toString();
        }
        catch(Exception e) {
            output = e.toString();
        }
        finally {
            pstmt.close();
            con.close();
        }
        return Response.status(201).entity(output).build(); 
    }
  
    @Path("/insertdepartment")
    @POST
    @Consumes("application/json")
    public Response getDepartmentDetails(Department department) throws SQLException{
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement pstmt=null;
        //department = new Department();
        String output="";
        try {

            con = getConnected();
            int deptid = department.getDeptId();
            String deptname = department.getDeptName();
            String deptaddress = department.getDeptAddress();
            //prepared statement 
            pstmt = con.prepareStatement("insert into department values(?,?,?)");
            pstmt.setInt(1,deptid);
            pstmt.setString(2,deptname);
            pstmt.setString(3,deptaddress);
            int var = pstmt.executeUpdate();
            output =  "deptname" + deptname + "department address" + deptaddress;
        }
        catch(SQLException se) {
            output = se.toString();
        }
        catch(Exception e) {
            output = e.toString();
        }
        finally {
           // rs.close();
            pstmt.close();
            con.close();
        }
        return Response.status(201).entity(output).build(); 
    }
    @GET
    @Path("/delete")
    @Produces("text/html")
    public Response deleteDepartment() throws SQLException{
        PreparedStatement pstmt = null;
        String output="";
        Connection con = null;
        try {
            con = getConnected();
            pstmt = con.prepareStatement("delete from department where deptid=109");
            int var = pstmt.executeUpdate();
            output = "deleted sucessfully";
        }
        catch(SQLException se) {
            output = se.toString();
        }
        catch(Exception e) {
            output = e.toString();
        }
        finally {
           // rs.close();
            pstmt.close();
            con.close();
        }
        return Response.status(201).entity(output).build();
    }
}
