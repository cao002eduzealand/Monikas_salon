package Objects;

public class SessionManager {


    //Class to get the current emplooye who is logged in.
    private static Employee currentEmployee;

    public static void setCurrentEmployee(Employee employee) {
        currentEmployee = employee;
    }
    public static Employee getCurrentEmployee() {
        return currentEmployee;
    }
    public static int getCurrentEmployeeId() {
        return currentEmployee.getId();
    }
}
