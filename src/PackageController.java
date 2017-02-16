/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */

import GUI.MainFrame;
import javax.swing.JFrame;



/**
 * Package Controller, used for Initialization   
 * @author Mark Culhane
 */
public class PackageController {
    public static void main(String[] args) {
        JFrame newFrame = new MainFrame();
        newFrame.setTitle("Mark's RSA and ECC App");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setVisible(true);
    }
}
