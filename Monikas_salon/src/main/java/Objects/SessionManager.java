package Objects;

public class SessionManager {

    private static Employee currentEmployee;

    public static void setCurrentEmployee(Employee employee) {
        currentEmployee = employee;
    }
    public static Employee getCurrentEmployee() {
        return currentEmployee;
    }
}
