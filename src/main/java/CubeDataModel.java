
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CubeDataModel extends AbstractTableModel {


    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;

    public CubeDataModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    //setting up the table
    private void setup() {

        countRows();

        try {
            colCount = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting coulmns" + se);
        }
    }

    //method to update the results
    public void updateResultSet(ResultSet newRS) {
        resultSet = newRS;
        setup();
    }

    private void countRows() {
        rowCount = 0;
        try {
            resultSet.beforeFirst();

            while (resultSet.next()) {
                rowCount++;
            }
            resultSet.beforeFirst();
        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }
    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount(){
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            resultSet.absolute(row + 1);
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException se) {
            System.out.println(se);
            return se.toString();

        }
    }

    //a method to delete a row from the table
    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    //returns true if successful, false if error occurs
    public boolean insertRow(String Name , Double Time) {

        try {
            //inserting a new row with the information
            resultSet.moveToInsertRow();
            resultSet.updateString(CubeDB.NAME_COLUMN, Name);
            resultSet.updateDouble(CubeDB.TIME_COLUMN, Time);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }

    }


    @Override
    //This method is called when user edits an editable cell
    public void setValueAt(Object newValue, int row, int col) {


        double newRating;

            newRating = Double.parseDouble(newValue.toString());
        //setting the updated values
        try {
            resultSet.absolute(row + 1);
            resultSet.updateDouble(CubeDB.TIME_COLUMN, newRating);
            resultSet.updateRow();
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("error changing rating " + e);
        }

    }

    //TODO To fix: look into table column models, and generate the number columns based on the columns found in the ResultSet.
    public boolean isCellEditable(int row, int col){
        if (col == 2) {
            return true;
        }
        return false;
    }
    @Override
    public String getColumnName(int col){
        //Getting from ResultSet metadata, which contains the database column names
        //TODO translate DB column names into something nicer for display, so "YEAR_RELEASED" becomes "Year Released"
        try {
            return resultSet.getMetaData().getColumnName(col + 1);
        } catch (SQLException se) {
            System.out.println("Error fetching column names" + se);
            return "?";
        }
    }

}
