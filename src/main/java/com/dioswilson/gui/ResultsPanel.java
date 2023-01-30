package com.dioswilson.gui;

import com.dioswilson.SeedFinder;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ResultsPanel extends JPanel {
    public static DefaultTableModel model;
    JTable table;
    private InputValuesPanel dataPanel;

    public ResultsPanel(InputValuesPanel dataPanel) {
        this.dataPanel = dataPanel;

        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"From", "To", "Heightmap", "Advancers","Quality" ,"Litematica"}, 0)/*{
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }*/;
//
//        model.setColumnIdentifiers(new String[]{"From", "To", "Heightmap", "Advancers"});
//        model.addRow(new String[]{"kljdsf","lkajhdjdhf","ljhdfkjh","khjh"});
//        model.setColumnIdentifiers(new String[]{"From", "To", "Heightmap", "Advancers",""});

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(175);


        table.getColumn("Litematica").setCellRenderer(new ButtonRenderer());
        table.getColumn("Litematica").setCellEditor(new ButtonEditor(new JCheckBox()));

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
            }
            else {
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
            button.addActionListener(e -> {
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            }
            else {
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

                int selectedRow = table.getSelectedRow();
                String[] from = ((String) table.getValueAt(selectedRow, 0)).split(",");
                int witchX = Integer.parseInt(from[0].split(":")[1]);
                int witchZ = Integer.parseInt(from[1].split(":")[1]);
                int advancers = Integer.parseInt((String) table.getValueAt(selectedRow, 3));
                new SeedFinder(dataPanel.getPlayerX(), dataPanel.getPlayerZ(), advancers, dataPanel.getWitchChunks(), dataPanel.getSeed(), witchX, witchZ).start();

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

