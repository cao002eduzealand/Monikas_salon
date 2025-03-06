package Database;

import Objects.Booking;
import Objects.Hairstyle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HairstyleRepository {

    // Method to create a new hairstyle record in the database
    public void createHairstyle(Hairstyle hairstyle) {

        // SQL query to insert a new hairstyle into the Hairstyles table
        String sql = "INSERT INTO Hairstyles (name, duration) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the hairstyle's name and duration into the prepared statement
            preparedStatement.setString(1, hairstyle.getName());
            preparedStatement.setInt(2, hairstyle.getDuration());

            // Execute the update to insert the hairstyle into the database
            preparedStatement.executeUpdate();

            System.out.println("Hairstyle created successfully!");

        } catch (SQLException e) {
            // Error handling in case of SQL issues
            System.out.println("Error creating hairstyle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to retrieve all hairstyles from the database
    public List<Hairstyle> readHairstyles() {
        List<Hairstyle> hairstyles = new ArrayList<>();

        // SQL query to select all hairstyles from the Hairstyles table
        String sql = "SELECT * FROM Hairstyles";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Loop through the result set and create Hairstyle objects
            while (resultSet.next()) {
                Hairstyle hairstyle = new Hairstyle(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("duration"));

                // Add the hairstyle to the list
                hairstyles.add(hairstyle);
            }

        } catch (SQLException e) {
            // Error handling if the query fails or the result set cannot be processed
            System.out.println("Error reading hairstyles: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the list of hairstyles
        return hairstyles;
    }

    // Method to update an existing hairstyle in the database
    public void updateHairstyle(Hairstyle hairstyle) {
        String sql = "UPDATE Hairstyles SET name = ?, duration = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the hairstyle's name, duration, and ID in the prepared statement
            preparedStatement.setString(1, hairstyle.getName());
            preparedStatement.setInt(2, hairstyle.getDuration());
            preparedStatement.setInt(3, hairstyle.getId()); // Ensure the ID is set to update the correct hairstyle

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Hairstyle updated successfully!");
            } else {
                System.out.println("Hairstyle update failed. No rows affected.");
            }

        } catch (SQLException e) {
            // Error handling if the update fails
            System.out.println("Error updating hairstyle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to delete a hairstyle from the database by its ID
    public void deleteHairstyle(int id) {
        String sql = "DELETE FROM Hairstyles WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the ID of the hairstyle to be deleted
            preparedStatement.setInt(1, id);

            // Execute the delete operation
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Hairstyle deleted successfully.");
            } else {
                System.out.println("No hairstyle found with ID " + id + ". Deletion failed.");
            }

        } catch (SQLException e) {
            // Error handling in case the deletion fails
            System.out.println("Error deleting hairstyle: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
