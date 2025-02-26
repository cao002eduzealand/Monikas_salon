package Database;

import Objects.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBookingRepository {

    public static void updateBooking(Booking booking) {

        String sql = "UPDATE Bookings SET customer_name = ?, employee_id = ?, hairstyle_id = ?, startTime = ?  WHERE id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, booking.getCustomer_name());
            preparedStatement.setString(2, booking.getEmployeeName());
            preparedStatement.setInt(3, booking.getHairstyle().getId());
            preparedStatement.setTimestamp(4, booking.getStartTime());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking updated");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
