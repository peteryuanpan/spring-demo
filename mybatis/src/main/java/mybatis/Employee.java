package mybatis;

public class Employee {

    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    @Override
    public String toString() {
        return "Emplyee{" +
            "id=" + id +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", gender='" + gender + '\'' +
            '}';
    }
}
