/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.client;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class WindowClient extends JFrame{
    private JTable table;
    private JButton btnAdd;
    private DefaultTableModel tableModel;
    private JTextField txtField;

    public WindowClient() {
        createGUI();
    }
   
    
    private void Submit()
    {
        int count = tableModel.getRowCount()+1;
        tableModel.addRow(new Object[]{txtField.getText(),"", "Pending"});
        txtField.setText("");
        tableModel.fireTableDataChanged();
    }

    private void createGUI() {
        setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane();
        table = new JTable();
        pane.setViewportView(table);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        txtField = new JTextField();
        northPanel.add(txtField);
        btnAdd = new JButton("Download");
        txtField.addKeyListener(
            new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        Submit();
                    }
                }
            }
        );
        
        btnAdd.setAlignmentX(CENTER_ALIGNMENT);
        northPanel.add(btnAdd);

        add(northPanel, BorderLayout.NORTH);
        add(pane,BorderLayout.CENTER);
        
        tableModel = new DefaultTableModel(new Object[] {"URL", "File Name", "Status", "Delete"},0){
            public boolean isCellEditable(int row, int col) { if(col<3) return false; else return true; }
        };
        table.setModel(tableModel);
        TableButton button = new TableButton("Delete",table.getModel());
        table.getColumn("Delete").setCellRenderer(button);
        table.getColumn("Delete").setCellEditor(button);
        
        btnAdd.addActionListener((ActionEvent e) -> {
            Submit();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WindowClient frm = new WindowClient();
            frm.setLocationByPlatform(true);
            frm.pack();
            frm.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frm.setVisible(true);
        });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

  public ButtonRenderer() {
    setOpaque(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(UIManager.getColor("Button.background"));
    }
    setText("DELETE");
    System.out.println(value+ " " + row);
    return this;
  }
}

interface TableButtonListener extends EventListener {
  public void tableButtonClicked( int row, int col );
}

class TableButton extends JButton implements TableCellRenderer, TableCellEditor {
  private int selectedRow;
  private int selectedColumn;
  private DefaultTableModel tableModel;
  Vector<TableButtonListener> listener;

  public TableButton(String text,TableModel model) {
    super(text); 
    tableModel = (DefaultTableModel)model;
    listener = new Vector<TableButtonListener>();
    addActionListener(new ActionListener() { 
      public void actionPerformed( ActionEvent e ) { 
        for(TableButtonListener l : listener) { 
          l.tableButtonClicked(selectedRow, selectedColumn);
        }
      }
    });
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
            int reply = JOptionPane.showConfirmDialog(null, "Sure?", "Close?",  JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION)
            {
                if(selectedRow>=tableModel.getRowCount())
                    return;
                
               
                tableModel.removeRow(selectedRow);
                tableModel.fireTableDataChanged();
            }
        }
    });
  
  }

  public void addTableButtonListener( TableButtonListener l ) {
    listener.add(l);
  }

  public void removeTableButtonListener( TableButtonListener l ) { 
    listener.remove(l);
  }

  @Override 
  public Component getTableCellRendererComponent(JTable table,
    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    return this;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table,
      Object value, boolean isSelected, int row, int col) {
    selectedRow = row;
    selectedColumn = col;
    return this;
  } 

  @Override
  public void addCellEditorListener(CellEditorListener arg0) {      
  } 

  @Override
  public void cancelCellEditing() {
  } 

  @Override
  public Object getCellEditorValue() {
    return "";
  }

  @Override
  public boolean isCellEditable(EventObject arg0) {
    return true;
  }

  @Override
  public void removeCellEditorListener(CellEditorListener arg0) {
  }

  @Override
  public boolean shouldSelectCell(EventObject arg0) {
    return true;
  }

  @Override
  public boolean stopCellEditing() {
    return true;
  }

   
}
