package FingerprintScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GenericDropbox extends JDialog {

    public String val;
    protected JLabel ambiguousMsg;
    protected JComboBox comboBox;
    protected JButton ok;

    public GenericDropbox(String titlename, String[] selections) {
        //super(EyelabClient.frame, titlename, true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        val = "";
        cs.fill = GridBagConstraints.NONE;

        ambiguousMsg = new JLabel("Serveral serial ports detected, specify");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(ambiguousMsg, cs);

        comboBox = new JComboBox(selections);
        cs.fill = GridBagConstraints.HORIZONTAL;
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(comboBox, cs);

        ok = new JButton("Ok");
        cs.fill = GridBagConstraints.NONE;
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(ok, cs);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                val = (String) comboBox.getSelectedItem();
                dispose();
            }
        });

        ok.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ok.doClick();
                }
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        setResizable(false);
        // setLocationRelativeTo(EyelabClient.frame);
    }
}