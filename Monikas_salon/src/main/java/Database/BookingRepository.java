package Database;

import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Objects.Employee.getEmployeeById;
import static Objects.Hairstyle.getHairstyleById;

public class BookingRepository {

    // Creates a new booking in the database
    public void createBooking(Booking booking) {

        String sql = "INSERT INTO Bookings (customer_name, employee_id, hairstyle_id, startTime, endTime) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, booking.getCustomer_name());
            preparedStatement.setInt(2, booking.getEmployeeId());
            preparedStatement.setInt(3, booking.getHairstyleId());
            preparedStatement.setTimestamp(4, booking.getStartTime());
            preparedStatement.setTimestamp(5, booking.getEndTime());

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error occurred while creating booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Retrieves all bookings from the database
    public List<Booking> readBookings() {
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM Bookings";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Loop through the result set and construct Booking objects
            while (resultSet.next()) {

                int employee_id = resultSet.getInt("employee_id");
                int hairstyle_id = resultSet.getInt("hairstyle_id");

                // Fetch Employee and Hairstyle objects based on IDs
                //Methods should be in EmployeeService and HairstyleService
                Employee employee = getEmployeeById(employee_id);
                Hairstyle hairstyle = getHairstyleById(hairstyle_id);

              //Employee employee = employeeService.getEmployeeById(employee_id);
               // Hairstyle hairstyle = hairstyleService.getHairstyleById(hairstyle_id);

                // Create a Booking object and add it to the list
                Booking booking = new Booking(resultSet.getInt("id"), resultSet.getString("customer_name"),
                        employee, hairstyle, resultSet.getTimestamp("startTime"), resultSet.getTimestamp("endTime"));

                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while reading bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    // Updates an existing booking in the database
    //not in use
    public void updateBooking(Booking booking) {

        String sql = "UPDATE Bookings SET customer_name = ?, employee_id = ?, hairstyle_id = ?, startTime = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL update query
            preparedStatement.setString(1, booking.getCustomer_name());
            preparedStatement.setInt(2, booking.getEmployeeId()); // Corrected to set employee ID
            preparedStatement.setInt(3, booking.getHairstyle().getId());
            preparedStatement.setTimestamp(4, booking.getStartTime());

            // Execute the update operation
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking updated successfully");
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while updating booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Deletes a booking from the database
    public void deleteBooking(int id) {
        String sql = "DELETE FROM Bookings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the booking ID parameter for deletion
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Booking deleted successfully");
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Checks if an employee is available for a specific time slot
    // should be moved into BookingService class
    public boolean isEmployeeAvailable(int employeeId, Timestamp startTime, Timestamp endTime) {
        String sql = "SELECT COUNT(*) FROM Bookings " +
                "WHERE employee_id = ? " +
                "AND (startTime < ? AND endTime > ?)"; // Checks if any booking overlaps with the requested time slot

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            stmt.setInt(1, employeeId);
            stmt.setTimestamp(2, endTime);
            stmt.setTimestamp(3, startTime);

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Employee is not available for the requested time slot
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while checking employee availability: " + e.getMessage());
            e.printStackTrace();
        }
        return true; // Employee is available
    }

    // Retrieves all the booked time slots for an employee on a specific date
    // should be moved into BookingService class
    public List<Timestamp[]> getBookedTimeSlots(int employeeId, LocalDate date) {
        List<Timestamp[]> bookedSlots = new ArrayList<>();

        String sql = "SELECT startTime, endTime FROM Bookings " +
                "WHERE employee_id = ? " +
                "AND DATE(startTime) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            stmt.setInt(1, employeeId);
            stmt.setDate(2, java.sql.Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp start = rs.getTimestamp("startTime");
                Timestamp end = rs.getTimestamp("endTime");
                bookedSlots.add(new Timestamp[]{start, end});
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching booked time slots: " + e.getMessage());
            e.printStackTrace();
        }
        return bookedSlots;
    }

    // Deletes all bookings that are older than a given cutoff date
    // should be moved into BookingService class
    public void deleteOldBookings(Timestamp cutoffDate) {
        String sql = "DELETE FROM Bookings WHERE endTime < ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the cutoff date for deleting old bookings
            statement.setTimestamp(1, cutoffDate);
            int rowsDeleted = statement.executeUpdate();
            System.out.println(rowsDeleted + " old bookings deleted from Bookings.");

        } catch (SQLException e) {
            System.err.println("Error occurred while deleting old bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
