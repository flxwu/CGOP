import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class GUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JLabel lblInfo;
    private JButton buttonPaste;

    public GUI() {
        super.setBounds(500, 100, 480, 600);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonPaste.addActionListener(e -> {
            try {
                String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                textArea1.setText(data);
            } catch (UnsupportedFlavorException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void onOK() {
        Parser parser = new Parser();
        try {
            parser.parse(new StringTokenizer(textArea1.getText()));
        } catch (TokenizerEmptyException e) {
            JOptionPane.showMessageDialog(null, "Please insert your grader output into the text box before analysing!");
        }
        Analysis.create(parser.getAnalysis());
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        GUI dialog = new GUI();
        //dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
