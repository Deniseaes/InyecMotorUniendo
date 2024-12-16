package front.inyecmotor.login;
import java.util.List;

public class AuthDTO {
    private String password;

    public AuthDTO(String password) {
        this.password = password;
    }

    // Getter y setter para el campo password
    public String getPass() {
        return password;
    }

    public void setPass(String password) {
        this.password = password;
    }
}
