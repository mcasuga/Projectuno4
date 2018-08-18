package training.mccasugadev.com.project_uno_4;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ItemAdapter itemAdapter;
    Context currentContext;
    ListView theListView;
    TextView progressTextView;
    Map<String, Double> fruitMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        theListView = findViewById(R.id.theListView);
        progressTextView = findViewById(R.id.progressTextView);
        currentContext = this;

        progressTextView.setText("");

        Button getDataButton = findViewById(R.id.getDataButton);
        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class GetData extends AsyncTask<String, String, String> {

        String progressMsg = "";

        // JDBC driver name and database URL
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL_COMPLETE = "jdbc:mysql://" + DBStrings.DB_URL + "/" + DBStrings.DB_NAME;

        @Override
        protected void onPreExecute() {
            progressTextView.setText("Contacting Database...");
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection connection = null;
            Statement stmt = null;

            try {

                // Load the JDBC Class dynamically during program runtime
                Class.forName(JDBC_DRIVER);

                // Establishing a connection to the chosen database
                connection = DriverManager.getConnection(DB_URL_COMPLETE, DBStrings.DB_USERNAME, DBStrings.DB_PASSWORD);

                // Creating a statement for selecting data in the Database
                stmt = connection.createStatement();
                String sqlStatement = "SELECT * FROM fruits";

                // Execute the SELECT query and storing the result in a ResultSet object
                ResultSet resultSet = stmt.executeQuery(sqlStatement);

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");

                    fruitMap.put(name, price);
                }

                progressMsg = "Done.";

                // Terminate all process that are not necessary
                resultSet.close();
                stmt.close();
                connection.close();

            } catch (ClassNotFoundException e) {
                progressMsg = "Error contacting  the Database - JDBC Class not found";
                e.printStackTrace();
            } catch (SQLException connectionError) {
                progressMsg = "Error contacting the Database - SQL Error";
                connectionError.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            progressTextView.setText(this.progressMsg);

            if (fruitMap.size() > 0) {
                itemAdapter = new ItemAdapter(currentContext, fruitMap);
                theListView.setAdapter(itemAdapter);
            }
        }
    }
} // End of MainActivity
