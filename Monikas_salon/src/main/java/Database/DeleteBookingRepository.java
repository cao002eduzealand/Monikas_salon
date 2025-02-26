package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBookingRepository {


    public static void deleteBooking(int id) {
        String sql = "DELETE FROM Bookings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Booking deleted");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
