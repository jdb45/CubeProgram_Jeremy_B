import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Cube_GUI extends JFrame implements WindowListener{
    private JTextField txtCubeSolver;
    private JTextField txtTime;
    private JButton addNewCubeSolverButton;
    private JButton deleteCubeSolverButton;
    private JButton quitButton;
    private JPanel rootPanel;
    private JTable cubeTable;


    Cube_GUI(final CubeDataModel cubeModel) {

        setContentPane(rootPanel);
        pack();
        setTitle("Cube Solver Database GUI");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //setting up the JTable
        cubeTable.setGridColor(Color.BLACK);
        cubeTable.setModel(cubeModel);

        //event handler for Add button
        addNewCubeSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String solverName = txtCubeSolver.getText();

                //checking to make sure a name has been entered
                if (solverName == null || solverName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a name");
                }

                //checking to make sure the time ins't 0
                Double timeData;

                try {
                    timeData = Double.parseDouble(txtTime.getText());
                    if (timeData < 0) {
                        throw new NumberFormatException("Time needs to be more than 0");
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane, "Time needs to be more than 0");
                    return;
                }
                System.out.println("Adding " + solverName + " " + timeData);
                boolean insertedRow = cubeModel.insertRow(solverName, timeData);

                //checking to make sure the data was entered in
                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new cube solver");
                }

                //setting the values back to empty after a new cube solver was entered
                txtCubeSolver.setText("");
                txtTime.setText("");
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CubeDB.shutdown();
                System.exit(0);
            }
        });


        //delete action listener
        deleteCubeSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = cubeTable.getSelectedRow();

                //checking to see if a cube solver has been selected
                if (currentRow == -1) {
                    JOptionPane.showMessageDialog(rootPane, "Please choose a solver to delete");
                }
                boolean deleted = cubeModel.deleteRow(currentRow);
                if (deleted) {
                    CubeDB.loadAllCubeSolvers();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting cube solver");
                }
            }
        });

    }


    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        CubeDB.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}

