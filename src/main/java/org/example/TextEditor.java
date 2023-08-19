package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
        //New window button
        JMenu newMenu = new JMenu("New");
        JMenuItem newWindowItem = new JMenuItem("New");
        newWindowItem.addActionListener(this);

        newMenu.add(newWindowItem);
        menuBar.add(newMenu);

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

        JMenu PrintMenu = new JMenu("Print");
        JMenuItem printItem = new JMenuItem("Print");
        printItem.addActionListener(this);
        PrintMenu.add(printItem);
        menuBar.add(PrintMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem selectItem = new JMenuItem("Select All");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem cutItem = new JMenuItem("Cut");

        selectItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        cutItem.addActionListener(this);

        editMenu.add(selectItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(cutItem);

        menuBar.add(editMenu);

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
        //add the about menu into the code
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);

        aboutMenu.add(aboutItem);
        menuBar.add(aboutMenu);


    }
    // The clearHighlight function for search text function
    private void clearHighlights() {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Select All")) {
            selectAllText();
        } else if (e.getActionCommand().equals("Copy")) {
            copyText();
        } else if (e.getActionCommand().equals("Paste")) {
            pasteText();
        } else if (e.getActionCommand().equals("Cut")) {
            cutText();
        } else if (e.getActionCommand().equals("Search")) {
            searchForText();
        } else if (e.getActionCommand().equals("Clear Highlights")) {
            clearHighlights();
        } else if (e.getActionCommand().equals("Open")) {
            openFile();
        } else if (e.getActionCommand().equals("Save")) {
            saveFile();
        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("About")) {
            showAboutMessage();
        } else if (e.getActionCommand().equals("New")) {
            createNewWindow();
        } else if (e.getActionCommand().equals("Print")) {
            printText();
        }
    }
    // New window opener
    private void createNewWindow() {
        TextEditor newEditor = new TextEditor();
        newEditor.setVisible(true);
    }

    private void selectAllText() {
        textArea.selectAll();
    }

    private void copyText() {
        textArea.copy();
    }

    private void pasteText() {
        textArea.paste();
    }

    private void cutText() {
        textArea.cut();
    }

    private void printText() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            job.setPrintable(textArea.getPrintable(null, null));
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Error printing the text.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    private void showAboutMessage() {
        String aboutMessage = "Team Members:\n"
                + "Member 1: Tongye XU, ID :22009649\n"
                + "Member 2: Zhaoyang Chen, ID :20013083\n"
                + "This is a simple text editor application co-developed by both team member.";

        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
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