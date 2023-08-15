package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JTextField searchField;
    private JButton searchButton;

    public TextEditor() {
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        searchField = new JTextField();
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        JMenu searchMenu = new JMenu("Search");
        JMenuItem searchItem = new JMenuItem("Search");
        JMenuItem clearHighlightsItem = new JMenuItem("Clear Highlights");

        searchItem.addActionListener(this);
        clearHighlightsItem.addActionListener(this);

        searchMenu.add(searchItem);
        searchMenu.add(clearHighlightsItem);

        menuBar.add(searchMenu);
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Add the search components to the search menu
        searchMenu.add(searchPanel);
        searchMenu.add(clearHighlightsItem);

        menuBar.add(searchMenu);
    }

    private void clearHighlights() {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Search")) {
            searchForText();
        } else if (e.getActionCommand().equals("Clear Highlights")) {
            clearHighlights();
        } else if (e.getActionCommand().equals("Open")) {
            openFile();
        } else if (e.getActionCommand().equals("Save")) {
            saveFile();
        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                textArea.setText(content.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            clearTextArea(); // Clear the text area after opening a file
        }
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchForText() {
        String searchText = searchField.getText();
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights(); // Clear existing highlights

        if (!searchText.isEmpty()) {
            try {
                String text = textArea.getText();
                int pos = 0;
                while ((pos = text.indexOf(searchText, pos)) >= 0) {
                    int end = pos + searchText.length();
                    highlighter.addHighlight(pos, end, DefaultHighlighter.DefaultPainter);
                    pos = end;
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching for text.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void clearTextArea() {
        textArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor editor = new TextEditor();
            editor.setVisible(true);
        });
    }


}