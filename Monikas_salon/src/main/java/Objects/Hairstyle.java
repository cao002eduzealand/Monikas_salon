package Objects;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hairstyle {

    private int id;
    private String name;
    private int duration;

    // Constructor to initialize a hairstyle object
    public Hairstyle(int id, String name, int duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    // Getter methods
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getDuration() {
        return duration;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    // Retrieves a hairstyle from the database using the given hairstyle ID
    public static Hairstyle getHairstyleById(int hairstyleId) throws SQLException {
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

    // Returns a string representation of the hairstyle object
    @Override
    public String toString() {
        return name + " -  " + duration + " Minutes";
    }
}