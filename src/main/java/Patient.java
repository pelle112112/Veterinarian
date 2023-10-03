import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Patient {
    private String id;
    private String name;
    private List<String> allergies;
    private List<String> medications;
    private List<Appointment> appointmentHistory;


}
