package Objects;

import java.sql.Timestamp;

public class Booking {

    //Attributtes
private int id;
private String customer_name;
private Employee employee;
private Hairstyle hairstyle;
private Timestamp startTime;
private Timestamp endTime;


//Constructor
public Booking(int id, String customer_name, Employee employee, Hairstyle hairstyle, Timestamp startTime, Timestamp endTime){
    this.id = id;
    this.customer_name = customer_name;
    this.employee = employee;
    this.hairstyle = hairstyle;
    this.startTime = startTime;
    this.endTime = endTime;

}


// Getters and setters

public int getId() {
    return id;
}
public String getCustomer_name() {
    return customer_name;
}
public void setCustomer_name(String customer_name) {
    this.customer_name = customer_name;
}
public Employee getEmployee() {
    return employee;
}
public int getEmployeeId() {
    return employee.getId();
}
public String getEmployeeName() {
    return employee.getName();
}
public void setEmployee(Employee employee) {
    this.employee = employee;
}

public Hairstyle getHairstyle() {
    return hairstyle;
}
public int getHairstyleId() {
    return hairstyle.getId();
}

public Timestamp getStartTime() {
    return startTime;
}
public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
}
public Timestamp getEndTime() {
    return endTime;
}
public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
}

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer_name='" + customer_name + '\'' +
                ", employee=" + employee.getName() +
                ", hairstyle=" + hairstyle.getId() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }



}
