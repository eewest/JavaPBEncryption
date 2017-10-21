/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import cryptolib.CipherHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author eewest
 */
public class FeaturesPanel extends JPanel implements ActionListener{
    CipherHelper cHelper;
    
    ButtonGroup bug_CipherType;
    JRadioButton[] rbn_CipherType;
    JCheckBox cbx_UseCipherSalt;
    
    public FeaturesPanel(CipherHelper cipherHelper){
        this.setLayout(new GridBagLayout());
        initComponents();
        layoutComponents();
        cHelper = cipherHelper;
    }
    
    private void initComponents(){
        bug_CipherType = new ButtonGroup();
        
        rbn_CipherType = new JRadioButton[2];
        rbn_CipherType[0] = new JRadioButton("AES", true);
        rbn_CipherType[0].addActionListener(this);
        rbn_CipherType[1] = new JRadioButton("DES", false);
        rbn_CipherType[1].addActionListener(this);
        
        bug_CipherType.add(rbn_CipherType[0]);
        bug_CipherType.add(rbn_CipherType[1]);
        
        cbx_UseCipherSalt = new JCheckBox("Use Cipher Salt", true);
        cbx_UseCipherSalt.addActionListener(this);
    }
    
    private void layoutComponents(){
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = c.weighty = 1;
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new JLabel("Algorithm Type:"), c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        this.add(rbn_CipherType[0], c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        this.add(rbn_CipherType[1], c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        this.add(cbx_UseCipherSalt, c);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(cbx_UseCipherSalt)){
            cHelper.setUseCipherSalt(cbx_UseCipherSalt.isSelected());
            
        }else if( e.getSource().equals(rbn_CipherType[0]) || e.getSource().equals(rbn_CipherType[1])){
            if(rbn_CipherType[0].isSelected()){
                cHelper.setAlgorithm(CipherHelper.AES_ALGORITHM);
            }else{
                cHelper.setAlgorithm(CipherHelper.DES_ALGORITHM);
            }
        }
    }
}
