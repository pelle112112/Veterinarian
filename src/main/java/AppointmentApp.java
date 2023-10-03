import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentApp {
    public static void main(String[] args) {
        List<Appointment> appointments = new ArrayList<>();
        List<Patient> patients = new ArrayList<>();
        LocalDate date = LocalDate.of(2021, 1, 1);
        LocalDate date2 = LocalDate.of(2021, 1, 2);
        LocalDate date3 = LocalDate.of(2021, 1, 3);
        LocalDate date4 = LocalDate.of(2021, 1, 4);
        LocalDate date5 = LocalDate.of(2021, 1, 5);
        appointments.add(new Appointment(new Patient("1", "John Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), date));
        appointments.add(new Appointment(new Patient("2", "Jane Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), date2));
        appointments.add(new Appointment(new Patient("3", "John Smith", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), date3));
        appointments.add(new Appointment(new Patient("4", "Jane Smith", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), date4));
        appointments.add(new Appointment(new Patient("5", "John Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), date5));
        patients.add(new Patient("1", "John Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        patients.add(new Patient("2", "Jane Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        patients.add(new Patient("3", "John Smith", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        patients.add(new Patient("4", "Jane Smith", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        patients.add(new Patient("5", "John Doe", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(javalinConfig -> javalinConfig.routing.contextPath = ("/api/vet")).start(7007);
        //Implement separate handlers (AppointmentHandler, PatientHandler) for each GET endpoint to manage the retrieval logic
        Handler appointmentHandler = ctx -> ctx.json(appointments);
        Handler patientHandler = ctx -> {
            List<Patient> patients1 = new ArrayList<>();
            for (Appointment appointment : appointments) {
                patients1.add(appointment.getPatient());
            }
            ctx.json(patients1);
        };




        //app.get("/appointments", ctx -> ctx.json(appointments));
        getAllAppointments(app, appointmentHandler);
        getAppointmentById(app, appointments);
        addAppointment(app, appointments);
        updateAppointment(app, appointments);
        deleteAppointment(app, appointments);
        getAllPatients(app, appointments, patientHandler);

    }
    public static void getAllAppointments(Javalin app, Handler handler) {
        app.get("/appointments", handler);
    }
    public static void getAppointmentById(Javalin app, List<Appointment> appointments) {
        app.get("/appointments/appointment/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Appointment appointment = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null);
            if (appointment == null) {
                ctx.status(404);
                ctx.result("Appointment not found");
            } else {
                ctx.json(appointment);
            }
        });
    }
    public static List<Appointment> addAppointment(Javalin app, List<Appointment> appointments) {
        app.post("/appointments/appointment", ctx -> {
            Appointment appointment = ctx.bodyAsClass(Appointment.class);
            appointments.add(appointment);
            ctx.status(201);
            ctx.json(appointment);
        });
        return appointments;
    }
    public static List<Appointment> updateAppointment(Javalin app, List<Appointment> appointments) {
        app.put("/appointments/appointment/new/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Appointment appointment = ctx.bodyAsClass(Appointment.class);
            Appointment appointmentToUpdate = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null);
            if (appointmentToUpdate == null) {
                ctx.status(404);
                ctx.result("Appointment not added");
            } else {
                appointmentToUpdate.setPatient(appointment.getPatient());
                appointmentToUpdate.setDate(appointment.getDate());
                ctx.json(appointmentToUpdate);
            }
        });
        return appointments;
    }
    public static List<Appointment> deleteAppointment(Javalin app, List<Appointment> appointments) {
        app.delete("/appointments/appointment/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Appointment appointment = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null);
            if (appointment == null) {
                ctx.status(404);
                ctx.result("Appointment not found");
            } else {
                appointments.remove(appointment);
                ctx.status(204);
            }
        });
        return appointments;
    }

    public static void getAllPatients(Javalin app, List<Appointment> appointments, Handler handler ){

        app.get("/patients", handler);

}
    public static void getPatientById(Javalin app, List<Appointment> appointments) {
        app.get("/patients/patient/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Patient patient = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null).getPatient();
            if (patient == null) {
                ctx.status(404);
                ctx.result("Patient not found");
            } else {
                ctx.json(patient);
            }
        });
    }
    public static void addPatient(Javalin app, List<Appointment> appointments) {
        app.post("/patients/patient", ctx -> {
            Patient patient = ctx.bodyAsClass(Patient.class);
            ctx.status(201);
            ctx.json(patient);
        });
    }
    public static void updatePatient(Javalin app, List<Appointment> appointments) {
        app.put("/patients/patient/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Patient patient = ctx.bodyAsClass(Patient.class);
            Patient patientToUpdate = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null).getPatient();
            if (patientToUpdate == null) {
                ctx.status(404);
                ctx.result("Patient not found");
            } else {
                patientToUpdate.setName(patient.getName());
                patientToUpdate.setAllergies(patient.getAllergies());
                patientToUpdate.setMedications(patient.getMedications());
                patientToUpdate.setAppointmentHistory(patient.getAppointmentHistory());
                ctx.json(patientToUpdate);
            }
        });
    }
    public static void deletePatient(Javalin app, List<Appointment> appointments) {
        app.delete("/patients/patient/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Patient patient = appointments.stream().filter(a -> a.getPatient().getId().equals(id)).findFirst().orElse(null).getPatient();
            if (patient == null) {
                ctx.status(404);
                ctx.result("Patient not found");
            } else {
                appointments.remove(patient);
                ctx.status(204);
            }
        });
    }

}
