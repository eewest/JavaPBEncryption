package userinterface;

import cryptolib.CipherHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author eewest
 */
public class EncryptionPanel extends JPanel {

    JButton btn_Encrypt;
    JTextField tbx_Message;
    public JTextField tbx_Password;
    public JTextArea txa_Result;
    
    CipherHelper cHelper;
    
    Base64.Decoder b64Decoder;
    Base64.Encoder b64Encoder;
    
    public EncryptionPanel(CipherHelper cipherHelper) {
        this.setLayout(new GridBagLayout());
        initComponents();
        layoutComponents();
        cHelper = cipherHelper;
    }

    private void initComponents() {
        btn_Encrypt = new JButton("Encrypt");

        btn_Encrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                byte[] cipherOut = cHelper.encryptData(tbx_Message.getText().getBytes(), tbx_Password.getText().getBytes());
                updateResult(cipherOut);
            }
        });

        tbx_Message = new JTextField();
        tbx_Password = new JTextField();
        txa_Result = new JTextArea("");
        txa_Result.setRows(4);
        txa_Result.setLineWrap(true);
    }

    private void layoutComponents() {
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = c.weighty = 1;

        //set constraints for first label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new JLabel("Message:"), c);

        //set constraints for tbx_Message
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 0;
        this.add(tbx_Message, c);

        //set constraints for second label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        this.add(new JLabel("Password:"), c);

        //set constraints for tbx_Password
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 1;
        this.add(tbx_Password, c);

        //set constraints for btn_Encrypt
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        this.add(btn_Encrypt, c);

        //set constraints for third label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 3;
        this.add(new JLabel("Result:"), c);

        //set constraints for txa_Result
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 4;
        c.gridheight = 3;
        c.gridx = 0;
        c.gridy = 4;
        this.add(txa_Result, c);
    }

    private void updateResult(byte[] data) {
        try {

            byte[] iv = new byte[cHelper.getBlockSize()];
            byte[] salt = new byte[16];
            System.arraycopy(data, 0, salt, 0, salt.length);
            if(cHelper.getUseCipherSalt()){
                System.arraycopy(data, salt.length, iv, 0, iv.length);
            }
            byte[] cipherText = new byte[data.length - ((cHelper.getUseCipherSalt()) ? iv.length : 0) - salt.length];
            System.arraycopy(data, ((cHelper.getUseCipherSalt()) ? iv.length : 0) + salt.length, cipherText, 0, cipherText.length);
            String cipherDataStr = "Password Salt: " + Base64.getEncoder().encodeToString(salt);
            if(cHelper.getUseCipherSalt()){
                cipherDataStr += "\nCipher IV: " + Base64.getEncoder().encodeToString(iv);
            }
            cipherDataStr += "\nCipher Text: " + Base64.getEncoder().encodeToString(cipherText);

            txa_Result.setText(cipherDataStr);
        } catch (Exception ex) {
            Logger.getLogger(EncryptionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
