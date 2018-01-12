/**
 * William Kendall
 * CMSC330
 * 
 * Evaluator class
 * This class builds a gui from data that has been parsed by the parser class
 */
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Evaluator {
  /**
   * guiEvaluator turns parsed graph data into a gui
   * @param gui graph of parsed data
   * @param object a JFrame or null
   */
  public void guiEvaluator(Node gui, Object object) {
    switch (gui.token.data) {
      case "gui": {
        System.out.println("Creating GUI");
        int nCount = 0;
        while (gui.children.size() > nCount
            && gui.children.get(nCount).token.data.compareTo("End") != 0) {
          guiEvaluator(gui.children.get(nCount), object);
          nCount++;
        }
        break;
      }
      case "Window": {
        System.out.println("Creating Window");
        JFrame frame = new JFrame(gui.children.get(0).token.data);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension((Integer.parseInt(gui.children.get(1).token.data)),
            Integer.parseInt(gui.children.get(2).token.data)));
        // build window contents
        int nCount = 3;
        while (gui.children.size() > nCount) {
          guiEvaluator(gui.children.get(nCount), frame);
          nCount++;
        }
        frame.revalidate();
        frame.pack();
        frame.setVisible(true);
        object = frame;
        break;
      }
      case "Layout": {
        System.out.println("Setting Layout");
        java.awt.Container content = (java.awt.Container) object;

        switch (gui.children.get(0).token.data) {
          case "Flow": {
            content.setLayout(new FlowLayout());
            break;
          }
          case "Grid": {
            if (gui.children.get(0).children.size() > 2)
              content.setLayout(
                  new GridLayout(Integer.parseInt(gui.children.get(0).children.get(0).token.data),
                      Integer.parseInt(gui.children.get(0).children.get(1).token.data),
                      Integer.parseInt(gui.children.get(0).children.get(2).token.data),
                      Integer.parseInt(gui.children.get(0).children.get(3).token.data)));
            else
              content.setLayout(
                  new GridLayout(Integer.parseInt(gui.children.get(0).children.get(0).token.data),
                      Integer.parseInt(gui.children.get(0).children.get(1).token.data)));
            break;
          }
        }
        break;
      }
      case "Textfield": {
        System.out.println("Creating TextField");
        java.awt.Container content = (java.awt.Container) object;
        JTextField text = new JTextField();
        text.setColumns(Integer.parseInt(gui.children.get(0).token.data));
        content.add(text);
        break;
      }
      case "Panel": {
        System.out.println("Creating Panel");
        java.awt.Container content = (java.awt.Container) object;
        JPanel panel = new JPanel();

        // create object on panel
        int nCount = 0;
        while (gui.children.size() > nCount) {
          guiEvaluator(gui.children.get(nCount), panel);
          nCount++;
        }

        content.add(panel);
        break;
      }
      case "Button": {
        System.out.println("Creating Button: " + gui.children.get(0).token.data);
        java.awt.Container content = (java.awt.Container) object;
        JButton button = new JButton(gui.children.get(0).token.data);
        content.add(button);
        break;
      }
      case "Label": {
        System.out.println("Creating Label");
        java.awt.Container content = (java.awt.Container) object;
        JLabel label = new JLabel(gui.children.get(0).token.data);
        content.add(label);
        break;
      }
      case "Group": {
        java.awt.Container content = (java.awt.Container) object;
        java.awt.Container groupMembers = new java.awt.Container();
        // get the objects in the group
        int nCount = 0;
        while (gui.children.size() > nCount) {
          guiEvaluator(gui.children.get(nCount), groupMembers);
          nCount++;
        }

        // and container contents to group
        ButtonGroup group = new ButtonGroup();
        for (java.awt.Component obj : groupMembers.getComponents()) {
          content.add(obj);
          group.add((javax.swing.AbstractButton) obj);
        }
        break;
      }
      case "Radio": {
        System.out.println("Creating Radio Button");
        java.awt.Container content = (java.awt.Container) object;
        JRadioButton radio = new JRadioButton(gui.children.get(0).token.data);
        content.add(radio);
        break;
      }
      case "End":{
        return;//end is not needed
      }
    }// end switch
  }


}
