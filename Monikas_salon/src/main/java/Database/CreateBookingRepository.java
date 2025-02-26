package Database;

import Objects.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CreateBookingRepository {

    public static void createBooking(Booking booking) {

        String sql = "INSERT INTO Bookings (customer_name, employeeId, hairstyleId, startDate, endTime) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){


            preparedStatement.setString(1, booking.getCustomer_name());
            preparedStatement.setInt(2, booking.getEmployeeId());
            preparedStatement.setInt(3, booking.getHairstyleId());
            preparedStatement.setTimestamp(4, booking.getStartTime());
            preparedStatement.setTimestamp(5, booking.getEndTime());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }




}
