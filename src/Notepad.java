import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JFileChooser fileChooser;
    private String currentFile;
    private JTextField findField;
    private JTextField replaceField;
    private JButton boldButton;
    private JButton italicButton;

    public Notepad() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        saveAsMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem findMenuItem = new JMenuItem("Find");
        JMenuItem replaceMenuItem = new JMenuItem("Replace");
        JMenuItem developerMenuItem = new JMenuItem("Developer: Sumalya Chatterjee");

        findMenuItem.addActionListener(this);
        replaceMenuItem.addActionListener(this);
        developerMenuItem.addActionListener(this);
        editMenu.add(findMenuItem);
        editMenu.add(replaceMenuItem);
        editMenu.add(developerMenuItem);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        JPanel findReplacePanel = new JPanel();
        findReplacePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel findLabel = new JLabel("Find:");
        findField = new JTextField(15);
        JButton findButton = new JButton("Find");
        JLabel replaceLabel = new JLabel("Replace:");
        replaceField = new JTextField(15);
        JButton replaceButton = new JButton("Replace");

        findButton.addActionListener(this);
        replaceButton.addActionListener(this);

        findReplacePanel.add(findLabel);
        findReplacePanel.add(findField);
        findReplacePanel.add(findButton);
        findReplacePanel.add(replaceLabel);
        findReplacePanel.add(replaceField);
        findReplacePanel.add(replaceButton);

        JPanel formattingPanel = new JPanel();
        formattingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        boldButton = new JButton("Bold");
        italicButton = new JButton("Italic");

        boldButton.addActionListener(this);
        italicButton.addActionListener(this);

        formattingPanel.add(boldButton);
        formattingPanel.add(italicButton);

        add(findReplacePanel, BorderLayout.NORTH);
        add(formattingPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("New")) {
            textArea.setText("");
            currentFile = null;
        } else if (command.equals("Open")) {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                currentFile = selectedFile.getAbsolutePath();
                openFile();
            }
        } else if (command.equals("Save")) {
            if (currentFile != null) {
                saveFile(currentFile);
            } else {
                saveFileAs();
            }
        } else if (command.equals("Save As")) {
            saveFileAs();
        } else if (command.equals("Exit")) {
            System.exit(0);
        } else if (command.equals("Find")) {
            String findText = findField.getText();
            findText(findText);
        } else if (command.equals("Replace")) {
            String findText = findField.getText();
            String replaceText = replaceField.getText();
            replaceText(findText, replaceText);
        } else if (command.equals("Bold")) {
            setBold();
        } else if (command.equals("Italic")) {
            setItalic();
        }
    }

    private void openFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(currentFile));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            textArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(textArea.getText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFileAs() {
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            saveFile(filePath);
            currentFile = filePath;
        }
    }

    private void findText(String findText) {
        String content = textArea.getText();
        int index = content.indexOf(findText);
        if (index != -1) {
            textArea.setCaretPosition(index);
            textArea.setSelectionStart(index);
            textArea.setSelectionEnd(index + findText.length());
        } else {
            JOptionPane.showMessageDialog(null, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void replaceText(String findText, String replaceText) {
        String content = textArea.getText();
        String modifiedContent = content.replace(findText, replaceText);
        textArea.setText(modifiedContent);
    }

    private void setBold() {
        Font font = textArea.getFont();
        Font boldFont = font.deriveFont(font.getStyle() ^ Font.BOLD);
        textArea.setFont(boldFont);
    }


    private void setItalic() {
        Font font = textArea.getFont();
        Font italicFont = font.deriveFont(font.getStyle() ^ Font.ITALIC);
        textArea.setFont(italicFont);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}
