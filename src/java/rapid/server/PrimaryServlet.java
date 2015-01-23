/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rapid.common.DownloadRequest;
import rapid.common.DownloadResponse;
import rapid.common.RegisterRequest;

/**
 *
 * @author ljk
 */
@WebServlet(name = "PrimaryServlet", urlPatterns = {"/PrimaryServlet"})
public class PrimaryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            ObjectInputStream inputStr = new ObjectInputStream(request.getInputStream());
            String text;

            Object reqObj = inputStr.readObject();

            if (reqObj instanceof RegisterRequest) {

                if (((RegisterRequest) reqObj).isType()) {
                    //register
                } else {
                    //unregister
                }
                // some response
                // ObjectOutputStream outStr = new ObjectOutputStream(response.getOutputStream());
                // response.setContentType("object");
                // outStr.writeObject(new SomeResponse(text));

            } else if (reqObj instanceof DownloadRequest) {
                text = "OUT" + ((DownloadRequest) reqObj).getUrlToDownload();

                ObjectOutputStream outStr = new ObjectOutputStream(response.getOutputStream());
                response.setContentType("object");

                if (((DownloadRequest) reqObj).getUser().getId().equals(1L)) {
                    outStr.writeObject(new DownloadResponse(text));
                }
                outStr.writeObject(new DownloadResponse("null"));

                System.out.println(text);
            }
        } catch (Exception ex) {
            Logger.getLogger(PrimaryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
//          ObjectInputStream output = new ObjectInputStream(request.getOutputStream());
//          response.getOutputStream().

//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet NewServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void fileSharkCheck(String urlStr) {
        URL address;
        try {
            address = new URL("http://localhost:8080/Leszek/FileSharkServlet");

            HttpURLConnection urlCon = (HttpURLConnection) address.openConnection();

            urlCon.setDoOutput(true); // to be able to write.
            urlCon.setDoInput(true); // to be able to read.

            ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
            out.writeObject("BLAH");
            out.close();

            ObjectInputStream ois = new ObjectInputStream(urlCon.getInputStream());
            DownloadResponse resp = (DownloadResponse) ois.readObject();
            System.out.println(resp.getResp());
            ois.close();

        } catch (Exception ex) {
            Logger.getLogger(PrimaryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}