package Database;

import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadBookingRepository {

    public static List<Booking> readBookings() {
        List<Booking> bookings = new ArrayList<Booking>();

        String sql = "SELECT * FROM Bookings";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

            while (resultSet.next()){

                int employee_id = resultSet.getInt("employee_id");
                int hairstyle_id = resultSet.getInt("hairstyle_id");

                Employee employee = getEmployeeById(employee_id);
                Hairstyle hairstyle = getHairstyleById(hairstyle_id);



                Booking booking = new Booking(resultSet.getInt("id"), resultSet.getString("customer_name"), employee,
                        hairstyle, resultSet.getTimestamp("startTime"), resultSet.getTimestamp("endTime"));

                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            return bookings;
    }


    private static Employee getEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
            }
        }
        return null;  // Return null if employee is not found
    }
    private static Hairstyle getHairstyleById(int hairstyleId) throws SQLException {
        String sql = "SELECT * FROM Hairstyles WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, hairstyleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Hairstyle(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("duration")  // Assuming 'duration' is in minutes
                );
            }
        }
        return null;  // Return null if hairstyle is not found
    }
}
