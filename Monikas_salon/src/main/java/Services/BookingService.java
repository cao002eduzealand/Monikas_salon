package Services;

import Database.*;
import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

//bookingservice

public class BookingService {
    private final BookingRepository bookingRepository = new BookingRepository();
    private final CreateCancelledBookingRepository cancelledBookingRepository = new CreateCancelledBookingRepository();
    private final CreateFinishedBookingRepository finishedBookingRepository = new CreateFinishedBookingRepository();
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final HairstyleRepository hairstyleRepository = new HairstyleRepository();

    public BookingService() {
        finishedBookingRepository.movePastBookingsToFinished(); // Ensure past bookings are moved
    }

    public void createBooking(Booking booking) {
        bookingRepository.createBooking(booking);
    }

    public List<Booking> getCreatedBookings() {
        return bookingRepository.readBookings();
    }

    public List<Booking> getFinishedBookings() {
        return finishedBookingRepository.readFinishedBookings();
    }

    public List<Booking> getCancelledBookings() {
        return cancelledBookingRepository.readCancelledBookings();
    }

    public void cancelBooking(Booking booking) {

        cancelledBookingRepository.createCancelledBookingRepository(booking);
        bookingRepository.deleteBooking(booking.getId());
    }
    public void deleteBookingFromBookings(int bookingId) {
        bookingRepository.deleteBooking(bookingId);
    }
    public void movePastBookingsToFinished() {
        finishedBookingRepository.movePastBookingsToFinished();
    }
    public void createEmployee(Employee employee) {
        employeeRepository.createEmployee(employee);
    }

    public List<Employee> getEmployees() {
      return employeeRepository.readEmployees();
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public void deleteEmployee(int employeeId) {
        employeeRepository.deleteEmployee(employeeId);
    }

    public void createHairstyle(Hairstyle hairstyle) {
        hairstyleRepository.createHairstyle(hairstyle);
    }
    public List<Hairstyle> getHairstyles() {
        return hairstyleRepository.readHairstyles();
    }
    public void updateHairstyle(Hairstyle hairstyle) {
        hairstyleRepository.updateHairstyle(hairstyle);
    }
    public void deleteHairstyle(int hairstyleId) {
        hairstyleRepository.deleteHairstyle(hairstyleId);
    }



    public List<Timestamp[]> getBookedTimeSlots(int employeeId, LocalDate date) {
        return bookingRepository.getBookedTimeSlots(employeeId, date);
    }

    public boolean isEmployeeAvailable(int employeeId, Timestamp startTime, Timestamp endTime) {
        return bookingRepository.isEmployeeAvailable(employeeId, startTime, endTime);
    }




    public void deleteOldBookings() {
        // Calculate the cutoff date 5 years ago from today
        LocalDateTime fiveYearsAgo = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        Timestamp cutoffTimestamp = Timestamp.valueOf(fiveYearsAgo);

        // Delete old bookings based on endTime
        bookingRepository.deleteOldBookings(cutoffTimestamp);
        finishedBookingRepository.deleteOldBookings(cutoffTimestamp);
        cancelledBookingRepository.deleteOldBookings(cutoffTimestamp);

        System.out.println("Old bookings (older than 5 years based on endTime) have been deleted.");
    }



}