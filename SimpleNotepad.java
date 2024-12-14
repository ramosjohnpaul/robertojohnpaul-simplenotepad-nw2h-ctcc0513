import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SimpleNotepad extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, archiveMenu;
    private JMenuItem newFile, openFile, saveFile, deleteFile, archiveNote, seeArchive, exitApp;
    private LinkedList<String> archivedNotes; // LinkedList for dynamic note storage
    private Stack<String> noteStack;  // Stack to store notes in LIFO order
    private Queue<String> noteQueue;  // Queue to store notes in FIFO order

    public SimpleNotepad() {

        setTitle("JJJ Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        archiveMenu = new JMenu("Archive");
        
        // Using LinkedList, Stack, and Queue
        archivedNotes = new LinkedList<>();
        noteStack = new Stack<>();
        noteQueue = new LinkedList<>();

        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        deleteFile = new JMenuItem("Delete Note");
        archiveNote = new JMenuItem("Archive Note");
        seeArchive = new JMenuItem("See Archive");
        exitApp = new JMenuItem("Exit");

        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        deleteFile.addActionListener(this);
        archiveNote.addActionListener(this);
        seeArchive.addActionListener(this);
        exitApp.addActionListener(this);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(deleteFile);
        fileMenu.addSeparator();
        fileMenu.add(exitApp);

        archiveMenu.add(archiveNote);
        archiveMenu.add(seeArchive);

        menuBar.add(fileMenu);
        menuBar.add(archiveMenu);

        setJMenuBar(menuBar);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            newFile();
        } else if (e.getSource() == openFile) {
            openFile();
        } else if (e.getSource() == saveFile) {
            saveFile();
        } else if (e.getSource() == deleteFile) {
            deleteNote();
        } else if (e.getSource() == archiveNote) {
            archiveNote();
        } else if (e.getSource() == seeArchive) {
            seeArchive();
        } else if (e.getSource() == exitApp) {
            exitApp();
        }
    }

    private void newFile() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to create a new file?", "New File", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            textArea.setText("");
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteNote() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this note?", "Delete Note", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            textArea.setText("");
        }
    }

    private void archiveNote() {
        String content = textArea.getText();
        if (!content.isEmpty()) {
            archivedNotes.add(content); // Using LinkedList to store notes
            noteStack.push(content);    // Using Stack to store notes
            noteQueue.offer(content);   // Using Queue to store notes
            JOptionPane.showMessageDialog(this, "Note archived successfully!", "Archive Note", JOptionPane.INFORMATION_MESSAGE);
            textArea.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to archive!", "Archive Note", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seeArchive() {
        StringBuilder archiveContent = new StringBuilder("Archived Notes:\n\n");

        // Sorting the archived notes using Bubble Sort (for simplicity)
        List<String> sortedNotes = new ArrayList<>(archivedNotes);
        Collections.sort(sortedNotes);  // Alphabetical sorting

        if (sortedNotes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No archived notes.", "See Archive", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String note : sortedNotes) {
                archiveContent.append(note).append("\n---\n");
            }
            JOptionPane.showMessageDialog(this, archiveContent.toString(), "See Archive", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exitApp() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleNotepad::new);
    }
}
