package Database;

import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Objects.Employee.getEmployeeById;
import static Objects.Hairstyle.getHairstyleById;

public class CreateCancelledBookingRepository {

    // Creates a cancelled booking record in the database.
    // This method inserts the booking details into the CancelledBookings table.
    public void createCancelledBookingRepository(Booking booking) {

        // SQL query to insert a cancelled booking into the CancelledBookings table
        String sql = "INSERT INTO CancelledBookings (customer_name, employee_id, hairstyle_id, startTime, endTime) VALUES (?,?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters for the SQL query based on the booking details
            preparedStatement.setString(1, booking.getCustomer_name());

            // Handle potential null employee
            if (booking.getEmployee() != null) {
                preparedStatement.setInt(2, booking.getEmployee().getId());
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }

            // Handle potential null hairstyle
            if (booking.getHairstyle() != null) {
                preparedStatement.setInt(3, booking.getHairstyle().getId());
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }

            preparedStatement.setTimestamp(4, booking.getStartTime()); // Start time
            preparedStatement.setTimestamp(5, booking.getEndTime());   // End time

            // Execute the update (insert the cancelled booking)
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Log the exception and provide a more specific message
            System.err.println("Error occurred while inserting cancelled booking: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }

    // Retrieves all cancelled bookings from the database.
    // This method reads the CancelledBookings table and returns a list of Booking objects.
    public List<Booking> readCancelledBookings() {
        List<Booking> bookings = new ArrayList<>(); // List to store cancelled bookings
        String sql = "SELECT * FROM CancelledBookings"; // SQL query to fetch all cancelled bookings

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Iterate over the result set and create Booking objects
            while (resultSet.next()) {

                // Retrieve employee_id and hairstyle_id from the result set
                int employee_id = resultSet.getInt("employee_id");
                int hairstyle_id = resultSet.getInt("hairstyle_id");

                // Fetch Employee and Hairstyle objects using the retrieved IDs
                Employee employee = getEmployeeById(employee_id);
                Hairstyle hairstyle = getHairstyleById(hairstyle_id);

                // Create a Booking object using the data from the result set
                Booking booking = new Booking(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        employee,
                        hairstyle,
                        resultSet.getTimestamp("startTime"),
                        resultSet.getTimestamp("endTime"));

                // Add the booking to the list
                bookings.add(booking);
            }
        } catch (SQLException e) {
            // Log the exception and provide a specific message for reading cancelled bookings
            System.out.println("Error occurred while reading cancelled bookings: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }

        return bookings; // Return the list of cancelled bookings
    }

    // Deletes old cancelled bookings from the database.
    // This method removes records from the CancelledBookings table that have an endTime before the given cutoff date.
    public void deleteOldBookings(Timestamp cutoffDate) {
        // SQL query to delete cancelled bookings with an endTime earlier than the cutoff date
        String sql = "DELETE FROM CancelledBookings WHERE endTime < ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameter for the cutoff date
            statement.setTimestamp(1, cutoffDate);

            // Execute the update (delete old bookings)
            int rowsDeleted = statement.executeUpdate();
            System.out.println(rowsDeleted + " old cancelled bookings deleted.");

        } catch (SQLException e) {
            // Log the exception and provide a specific message for deleting old bookings
            System.out.println("Error occurred while deleting old cancelled bookings: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }
}

