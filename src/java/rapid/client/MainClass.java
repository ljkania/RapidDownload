/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import rapid.common.DownloadRequest;
import rapid.common.DownloadResponse;
//import rapid.common.RegisterRequest;
import rapid.common.User;

/**
 *
 * @author ljk
 */
public class MainClass {

    private static User usr = new User(1L);
    private static final String SERVER = "http://localhost:8084/RapidDownload/PrimaryServlet";

    public static void main(String[] args) {
        try {
            URL address = new URL(SERVER);

            HttpURLConnection urlCon = (HttpURLConnection) address.openConnection();

            urlCon.setDoOutput(true); // to be able to write.
            urlCon.setDoInput(true); // to be able to read.

            ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
//            out.writeObject(new RegisterRequest(true));
            out.close();

//                ((ListModel)table.getModel()).addItem("adam");
        } catch (Exception ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        final JTextArea url = new JTextArea();
//        final JTable table = new JTable(new ListModel());
        JButton send = new JButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GoGOURL!");

                try {
                    URL address = new URL(SERVER);

                    HttpURLConnection urlCon = (HttpURLConnection) address.openConnection();

                    urlCon.setDoOutput(true); // to be able to write.
                    urlCon.setDoInput(true); // to be able to read.

//                    ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
//                    out.writeObject(new DownloadRequest(usr, url.getText()));
//                    out.close();
                    
                    url.setText("");

//                    ObjectInputStream ois = new ObjectInputStream(urlCon.getInputStream());
//                    DownloadResponse resp = (DownloadResponse) ois.readObject();
//                    System.out.println(resp.getResp());
//                    ois.close();

//                ((ListModel)table.getModel()).addItem("adam");
                } catch (Exception ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        send.setText("Download");

        JPanel centerPanel = new JPanel();
        centerPanel.add(url);
//        centerPanel.add(table);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(send, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setSize(new Dimension(500, 400));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e); //To change body of generated methods, choose Tools | Templates.
                try {
                    URL address = new URL(SERVER);

                    HttpURLConnection urlCon = (HttpURLConnection) address.openConnection();

                    urlCon.setDoOutput(true); // to be able to write.
                    urlCon.setDoInput(true); // to be able to read.

                    ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
//                    out.writeObject(new RegisterRequest(false));
                    out.close();

                } catch (Exception ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

}