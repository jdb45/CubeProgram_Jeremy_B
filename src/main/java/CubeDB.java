import java.sql.*;

public class CubeDB {

    //the connection string to the database
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cube";
    //assigning user and password
    static final String USER = "Enter user name";
    static final String PASSWORD = "Enter Password ";

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    //setting my column names, and table name
    public final static String CUBE_TABLE_NAME = "cube_solvers";
    public final static String PK_COLUMN = "id";
    public final static String NAME_COLUMN = "cube_solver";
    public final static String TIME_COLUMN = "time_taken";

    private static CubeDataModel cubeModel;

    public static void main(String[] args) {

        //checking for errors on setup
        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllCubeSolvers()) {
            System.exit(-1);
        }

        //starting thr GUI if there are no errors
        Cube_GUI cube = new Cube_GUI(cubeModel);
    }


        //loading all the cube solvers in the database
        public static boolean loadAllCubeSolvers() {

            try {

                if (rs != null) {
                    rs.close();
                }

                String getAllData = "SELECT * FROM " + CUBE_TABLE_NAME;
                rs = statement.executeQuery(getAllData);

                if (cubeModel == null) {
                    //setting the model if there isn't one
                    cubeModel = new CubeDataModel(rs);
                } else {
                    //if there is one, it will update the result set
                    cubeModel.updateResultSet(rs);
                }
                return true;

            } catch (Exception e) {
                System.out.println("Error loading or reloading cube solvers");
                System.out.println(e);
                e.printStackTrace();
                return false;
            }
        }




    public static boolean setup() {

        try {

            //loading the drivers
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            //getting connection to the database
            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


            if (!cubeTableExists()) {

                //Create a table in the database with 2 columns: cube solver and time taken
                String createTableSQL = "CREATE TABLE " + CUBE_TABLE_NAME + " (" + PK_COLUMN + " INT NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " VARCHAR(75), " + TIME_COLUMN + " DOUBLE, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created Cube_Solvers table");

                String addDataSQL = "INSERT INTO " + CUBE_TABLE_NAME + "( " + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Cubestormer II robot', 5.270)";
                statement.executeUpdate(addDataSQL);
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean cubeTableExists() throws SQLException {

        //showing the table that exists
        String checkTablePresenQuery = "SHOW TABLES LIKE '" + CUBE_TABLE_NAME + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresenQuery);
        if(tablesRS.next()) {
            return true;
        }
        return false;
    }


    //shutting down all connections, statements
    public static void shutdown(){
        try{
            if(rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        }catch (SQLException se){
            se.printStackTrace();
        }

        try {
            if(statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        }catch (SQLException se){
            se.printStackTrace();
        }

        try{
            if(conn != null){
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se){
            se.printStackTrace();
        }
    }



}
