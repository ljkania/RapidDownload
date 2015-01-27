package rapid.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import rapid.common.*;
import static rapid.common.Config.*;
import rapid.server.UserServer;


/**
 *
 * @author ljk
 */
public class DownloadManager extends JFrame implements Observer {
    
    private JTextField addTextField;
    private DownloadsTableModel tableModel;
    private JTable table;
    
    private JButton clearButton, restartButton;
    
    private Download selectedDownload;
    
    private boolean clearing;
    
    public DownloadManager() {
        setTitle("Download Manager");
        setSize(640, 480);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        
        JPanel addPanel = new JPanel();
        addTextField = new JTextField(30);
        addPanel.add(addTextField);
        JButton addButton = new JButton("Download");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        addPanel.add(addButton);
        
        tableModel = new DownloadsTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel();
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClear();
            }
        });
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);
        
        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionRestart();
            }
        });
        restartButton.setEnabled(false);
        buttonsPanel.add(restartButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);    }

    private void actionExit() {
        System.exit(0);
    }
    
    private void actionAdd() {
        String url = addTextField.getText();
        addTextField.setText("");
        
        URL verifiedUrl = verifyUrl(url);
        
        if(verifiedUrl!=null)
            tableModel.addDownload(new Download(verifiedUrl));
        else
            JOptionPane.showMessageDialog(this, "Invalid Download URL: " + url, "Error", JOptionPane.ERROR_MESSAGE);
        
    }
    
    private URL verifyUrl(String url) {
        if(!url.toLowerCase().startsWith("http://"))
            url = (new StringBuffer("http://")).append(url).toString();
        
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        
        System.out.println(verifiedUrl);
        
        return verifiedUrl;
    }
    
    private void tableSelectionChanged() {
        if(selectedDownload!=null)
            selectedDownload.deleteObserver(DownloadManager.this);
        
        if(clearing)
               return;
        
        selectedDownload = tableModel.getDownload(table.getSelectedRow());
        selectedDownload.addObserver(DownloadManager.this);
        updateButtons();
    }
        
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }
    
    private void actionRestart() {
        selectedDownload.restart();
        updateButtons();
    }

    
    private void updateButtons() {
        if(selectedDownload==null || selectedDownload.getStatus()==Config.DOWNLOADING) {
            clearButton.setEnabled(false);
            restartButton.setEnabled(false);
        } else {
            clearButton.setEnabled(true);
            restartButton.setEnabled(true);
        }
    }
    
    public void update(Observable o, Object arg) {
        if(selectedDownload!=null && selectedDownload.equals(o))
            updateButtons();
    }
    
    public static void main(String[] args) throws IOException {
        Socket FileSharkSock = new Socket(SERVER_IP, SERVER_PORT);
        new Thread(new UserServer(FileSharkSock, Module.FILESHARK)).start();
        
        DownloadManager manager = new DownloadManager();
        manager.setVisible(true);
    }
}