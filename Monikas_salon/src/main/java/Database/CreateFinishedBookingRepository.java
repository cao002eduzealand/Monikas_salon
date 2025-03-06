package Database;

import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Objects.Employee.getEmployeeById;
import static Objects.Hairstyle.getHairstyleById;

public class CreateFinishedBookingRepository {

    // Method to move bookings with past end time from the "Bookings" table to the "FinishedBookings" table
    public void movePastBookingsToFinished() {
        // SQL query to insert past bookings into the FinishedBookings table, ensuring NULL handling
        String moveSQL = "INSERT INTO FinishedBookings (customer_name, employee_id, hairstyle_id, startTime, endTime) " +
                "SELECT customer_name, " +
                "       CASE WHEN employee_id IS NULL THEN NULL ELSE employee_id END, " +
                "       CASE WHEN hairstyle_id IS NULL THEN NULL ELSE hairstyle_id END, " +
                "       startTime, endTime " +
                "FROM Bookings WHERE endTime < NOW()";

        // SQL query to delete past bookings from the "Bookings" table
        String deleteSQL = "DELETE FROM Bookings WHERE endTime < NOW()";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement moveStmt = connection.prepareStatement(moveSQL);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {

            // Move past bookings to FinishedBookings
            int movedRows = moveStmt.executeUpdate();
            if (movedRows > 0) {
                System.out.println(movedRows + " past bookings moved to FinishedBookings.");
            }

            // Delete the moved bookings from the Bookings table
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            // Error handling if the SQL query execution fails
            System.err.println("Error moving past bookings to FinishedBookings: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Method to read finished bookings from the FinishedBookings table
    public List<Booking> readFinishedBookings() {
        List<Booking> finishedBookings = new ArrayList<>();
        String sql = "SELECT * FROM FinishedBookings";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Process each row of the result set and create Booking objects
            while (rs.next()) {
                int employee_id = rs.getInt("employee_id");
                int hairstyle_id = rs.getInt("hairstyle_id");

                // Retrieve Employee and Hairstyle objects by their respective IDs
                Employee employee = getEmployeeById(employee_id);
                Hairstyle hairstyle = getHairstyleById(hairstyle_id);

                // Create Booking object and add it to the list
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        employee,
                        hairstyle,
                        rs.getTimestamp("startTime"),
                        rs.getTimestamp("endTime")
                );
                finishedBookings.add(booking);
            }
        } catch (SQLException e) {
            // Error handling if the query fails or something goes wrong during data retrieval
            System.out.println("Error reading finished bookings: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the list of finished bookings
        return finishedBookings;
    }

    // Method to delete old bookings from the FinishedBookings table that have an end time older than the provided cutoff date
    public void deleteOldBookings(Timestamp cutoffDate) {
        String sql = "DELETE FROM FinishedBookings WHERE endTime < ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the cutoff date parameter for the query
            statement.setTimestamp(1, cutoffDate);

            // Execute the delete query
            int rowsDeleted = statement.executeUpdate();
            System.out.println(rowsDeleted + " old bookings deleted from FinishedBookings.");

        } catch (SQLException e) {
            // Error handling if the deletion fails
            System.out.println("Error deleting old finished bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
