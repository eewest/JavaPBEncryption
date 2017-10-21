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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author eewest
 */
public class DecryptionPanel extends JPanel{
    JButton btn_Decrypt;
    JTextField tbx_Message;
    JTextField tbx_Password;
    JTextField tbx_IV;
    JTextField tbx_salt;
    JTextArea txa_Result;
    CipherHelper cHelper;
    public DecryptionPanel(CipherHelper cipherHelper){
        this.setLayout(new GridBagLayout());
        initComponents();
        layoutComponents();
       cHelper = cipherHelper;
    }
    
    private void initComponents(){
        btn_Decrypt = new JButton("Decrypt");
        
        btn_Decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    if((tbx_IV.getText().length() > 0 || !cHelper.getUseCipherSalt()) && tbx_Message.getText().length() > 0 && tbx_Password.getText().length() > 0){
                        byte[] cipherText = cHelper.decryptData(Base64.getDecoder().decode(tbx_Message.getText()) 
                                , tbx_Password.getText().getBytes(), 
                                Base64.getDecoder().decode(tbx_IV.getText()), 
                                Base64.getDecoder().decode(tbx_salt.getText()));
                        String cipherTextStr = new String(cipherText);

                        txa_Result.setText(cipherTextStr);
                        txa_Result.setRows(4);
                    }else{
                        if(!(tbx_IV.getText().length() > 0 && tbx_Message.getText().length() > 0 && tbx_Password.getText().length() > 0)){
                            JOptionPane.showMessageDialog(null, "Must fill all text fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(!(tbx_IV.getText().length() > 0)){
                            JOptionPane.showMessageDialog(null, "Must fill IV field!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(!(tbx_Message.getText().length() > 0)){
                            JOptionPane.showMessageDialog(null, "Must fill Message field!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(!(tbx_Password.getText().length() > 0)){
                            JOptionPane.showMessageDialog(null, "Must fill Password field!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DecryptionPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        tbx_Message = new JTextField();
        tbx_Password = new JTextField();
        tbx_IV = new JTextField();
        tbx_salt = new JTextField();
        
        txa_Result = new JTextArea("");
        txa_Result.setRows(4);
        txa_Result.setLineWrap(true);
        
    }
    
    private void layoutComponents(){
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = c.weighty = 1;
        
        //set constraints for first label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        this.add(new JLabel("Message:"), c);
        
        //set constraints for tbx_Message
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2; c.gridy = 0;
        this.add(tbx_Message,c);
        
        //set constraints for second label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1;
        this.add(new JLabel("Password:"),c);
        
        //set constraints for tbx_Password
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2; c.gridy = 1;
        this.add(tbx_Password,c);
        
        //set constraints for third label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 2;
        this.add(new JLabel("IV:"),c);
        
        //set constraints for tbx_IV
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2; c.gridy = 2;
        this.add(tbx_IV,c);
        
        //set constraints for fourth label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 3;
        this.add(new JLabel("Salt:"),c);
        
        //set constraints for tbx_IV
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 2; c.gridy = 3;
        this.add(tbx_salt,c);
        
        //set constraints for btn_Decrypt
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1; c.gridy = 4;
        this.add(btn_Decrypt,c);
        
        //set constraints for third label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 4;
        c.gridx = 0; c.gridy = 5;
        this.add(new JLabel("Result:"),c);
        
        //set constraints for txa_Result
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 4;
        c.gridheight = 3;
        c.gridx = 0; c.gridy = 6;
        this.add(txa_Result,c);
    }
    
    public void CopyCipherData(String data){
        String[] args = data.split("\n");
        tbx_Password.setText(args[0]);
        tbx_salt.setText(args[1].substring(args[1].lastIndexOf(" ")+1));
        if(cHelper.getUseCipherSalt()){
            tbx_IV.setText(args[2].substring(args[2].lastIndexOf(" ")+1));
            tbx_Message.setText(args[3].substring(args[3].lastIndexOf(" ")+1));
        }else{
            tbx_Message.setText(args[2].substring(args[2].lastIndexOf(" ")+1));
        }
    }
    
}
