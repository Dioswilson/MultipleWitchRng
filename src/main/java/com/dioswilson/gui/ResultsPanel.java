package com.dioswilson.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsPanel extends JPanel {
    public static DefaultTableModel model;

    public ResultsPanel() {
        model = new DefaultTableModel(new String[]{"From", "To", "Heightmap", "Advancers",""}, 0)/*{
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }*/;
//
//        model.setColumnIdentifiers(new String[]{"From", "To", "Heightmap", "Advancers"});
//        model.addRow(new String[]{"kljdsf","lkajhdjdhf","ljhdfkjh","khjh"});
//        model.setColumnIdentifiers(new String[]{"From", "To", "Heightmap", "Advancers",""});

        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);



        table.getColumn("").setCellRenderer(new ButtonRenderer());
        table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(table));
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
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");

                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}

