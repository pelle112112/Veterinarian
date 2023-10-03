import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AppointmentDTO {
    private PatientDTO patient;
    private LocalDate date;
}
