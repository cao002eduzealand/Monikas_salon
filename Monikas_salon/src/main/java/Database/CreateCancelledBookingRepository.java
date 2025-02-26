package Database;

import Objects.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateCancelledBookingRepository {


    public static void createCancelledBookingRepository(Booking booking) {

        String sql = "INSERT INTO CancelledBookings (customer_name, employee_id, hairstyle_id, startTime, endtime) VALUES (?,?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, booking.getCustomer_name());
            preparedStatement.setInt(2, booking.getEmployee().getId());
            preparedStatement.setInt(3, booking.getHairstyle().getId());
            preparedStatement.setTimestamp(4, booking.getStartTime());
            preparedStatement.setTimestamp(5, booking.getEndTime());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    }

