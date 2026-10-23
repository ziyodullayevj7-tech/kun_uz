package jahongir.kun_uz.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.service.StudentService;

import java.util.List;

@Route("students")
@PageTitle("Students")
public class StudentView extends VerticalLayout {

    private final StudentService studentService;

    // ── CRUD grid ─────────────────────────────────────────────────────────────
    private final Grid<StudentEntity> crudGrid = new Grid<>(StudentEntity.class, false);

    // ── Analytics grid ────────────────────────────────────────────────────────
    private final Grid<AnalyticsDTO.StudentSummaryAnalytics> analyticsGrid =
            new Grid<>(AnalyticsDTO.StudentSummaryAnalytics.class, false);

    public StudentView(StudentService studentService) {
        this.studentService = studentService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Student Management"));
        buildCrudSection();
        add(new H3("Analytics Overview — Active Students With Grades"));
        buildAnalyticsSection();

        refreshCrud();
    }

    // ── CRUD section ──────────────────────────────────────────────────────────

    private void buildCrudSection() {
        crudGrid.addColumn(StudentEntity::getId).setHeader("ID").setWidth("260px").setFlexGrow(0);
        crudGrid.addColumn(s -> s.getFirstName() + " " + s.getLastName()).setHeader("Full Name").setAutoWidth(true);
        crudGrid.addColumn(StudentEntity::getStudentNumber).setHeader("Student №").setAutoWidth(true);
        crudGrid.addColumn(StudentEntity::getEmail).setHeader("E-mail").setAutoWidth(true);
        crudGrid.addColumn(StudentEntity::getStatus).setHeader("Status").setAutoWidth(true);

        crudGrid.addComponentColumn(student -> {
            Button edit = new Button("Edit", e -> openEditDialog(student));
            edit.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button del = new Button("Delete", e -> confirmDelete(student));
            del.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, del);
        }).setHeader("Actions").setAutoWidth(true);

        add(crudGrid);
    }

    private void openEditDialog(StudentEntity student) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Student");

        TextField firstName = new TextField("First Name", student.getFirstName(), "");
        TextField lastName  = new TextField("Last Name",  student.getLastName(),  "");
        TextField status    = new TextField("Status",     student.getStatus(),    "");

        FormLayout form = new FormLayout(firstName, lastName, status);

        Button save = new Button("Save", e -> {
            StudentEntity patch = new StudentEntity();
            patch.setFirstName(firstName.getValue().trim());
            patch.setLastName(lastName.getValue().trim());
            patch.setStatus(status.getValue().trim());
            try {
                studentService.update(student.getId(), patch);
                Notification.show("Student updated.");
                dialog.close();
                refreshAll();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(form);
        dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
        dialog.open();
    }

    private void confirmDelete(StudentEntity student) {
        ConfirmDialog cd = new ConfirmDialog(
                "Delete Student",
                "Delete " + student.getFirstName() + " " + student.getLastName() + "?",
                "Delete", e -> {
                    studentService.delete(student.getId());
                    Notification.show("Deleted.");
                    refreshAll();
                },
                "Cancel", e -> {}
        );
        cd.setConfirmButtonTheme("error primary");
        cd.open();
    }

    // ── Analytics section ─────────────────────────────────────────────────────

    private void buildAnalyticsSection() {
        analyticsGrid.addColumn(AnalyticsDTO.StudentSummaryAnalytics::studentId)
                .setHeader("ID").setWidth("260px").setFlexGrow(0);
        analyticsGrid.addColumn(AnalyticsDTO.StudentSummaryAnalytics::fullName)
                .setHeader("Full Name").setAutoWidth(true);
        analyticsGrid.addColumn(AnalyticsDTO.StudentSummaryAnalytics::studentNumber)
                .setHeader("Student №").setAutoWidth(true);
        analyticsGrid.addColumn(r -> String.format("%.2f", r.gpaScore()))
                .setHeader("GPA").setAutoWidth(true);
        analyticsGrid.addColumn(AnalyticsDTO.StudentSummaryAnalytics::totalCampusVisits)
                .setHeader("Campus Visits (IN)").setAutoWidth(true);
        analyticsGrid.addColumn(AnalyticsDTO.StudentSummaryAnalytics::totalLmsMinutes)
                .setHeader("LMS Minutes").setAutoWidth(true);

        Button refresh = new Button("Generate Analytics", e -> refreshAnalytics());
        add(refresh, analyticsGrid);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCrud() {
        crudGrid.setItems(studentService.findAll());
    }

    private void refreshAnalytics() {
        try {
            List<AnalyticsDTO.StudentSummaryAnalytics> data = studentService.rebuildStudentAnalytics();
            analyticsGrid.setItems(data);
            Notification.show("Student analytics table updated.");
        } catch (Exception ex) {
            Notification.show("Could not generate analytics: " + ex.getMessage());
        }
    }

    private void refreshAll() {
        refreshCrud();
    }
}
