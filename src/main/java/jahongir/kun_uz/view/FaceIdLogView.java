package jahongir.kun_uz.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.FaceIdLogEntity;
import jahongir.kun_uz.service.FaceIdLogService;
import jahongir.kun_uz.service.StudentService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route("faceid")
@PageTitle("Face-ID Logs")
public class FaceIdLogView extends VerticalLayout {

    private final FaceIdLogService faceIdLogService;
    private final StudentService   studentService;

    private final Grid<FaceIdLogEntity>                crudGrid      = new Grid<>(FaceIdLogEntity.class, false);
    private final Grid<AnalyticsDTO.FaceIdFrequencyMetric> leaderGrid = new Grid<>(AnalyticsDTO.FaceIdFrequencyMetric.class, false);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FaceIdLogView(FaceIdLogService faceIdLogService, StudentService studentService) {
        this.faceIdLogService = faceIdLogService;
        this.studentService   = studentService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Face-ID Campus Access Log"));
        buildCrudSection();
        add(new H3("Attendance Leaderboard (last 30 days)"));
        buildLeaderSection();

        refreshCrud();
        refreshLeaderboard();
    }

    // ── CRUD section ──────────────────────────────────────────────────────────

    private void buildCrudSection() {
        crudGrid.addColumn(FaceIdLogEntity::getId).setHeader("ID").setWidth("260px").setFlexGrow(0);
        crudGrid.addColumn(l -> l.getStudent().getFirstName() + " " + l.getStudent().getLastName())
                .setHeader("Student").setAutoWidth(true);
        crudGrid.addColumn(FaceIdLogEntity::getLocation).setHeader("Location").setAutoWidth(true);
        crudGrid.addColumn(FaceIdLogEntity::getDirection).setHeader("Direction").setAutoWidth(true);
        crudGrid.addColumn(l -> l.getTimestamp().format(FMT)).setHeader("Timestamp").setAutoWidth(true);

        crudGrid.addComponentColumn(log -> {
            Button del = new Button("Delete", e -> confirmDelete(log));
            del.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return del;
        }).setHeader("Actions").setAutoWidth(true);

        add(crudGrid);
    }

    private void confirmDelete(FaceIdLogEntity log) {
        ConfirmDialog cd = new ConfirmDialog(
                "Delete Log",
                "Delete this log entry?",
                "Delete", e -> {
                    faceIdLogService.delete(log.getId());
                    Notification.show("Deleted.");
                    refreshAll();
                },
                "Cancel", e -> {}
        );
        cd.setConfirmButtonTheme("error primary");
        cd.open();
    }

    // ── Leaderboard section ───────────────────────────────────────────────────

    private void buildLeaderSection() {
        leaderGrid.addColumn(AnalyticsDTO.FaceIdFrequencyMetric::studentName)
                .setHeader("Student").setAutoWidth(true);
        leaderGrid.addColumn(AnalyticsDTO.FaceIdFrequencyMetric::entryCount)
                .setHeader("Campus Visits (IN)").setAutoWidth(true);
        leaderGrid.addColumn(r -> r.lastRecordedEntry() != null ? r.lastRecordedEntry().format(FMT) : "-")
                .setHeader("Last Entry").setAutoWidth(true);

        // Access-ratio mini-panel
        TextField ratioStudentId = new TextField("Student ID");
        Button ratioBtn = new Button("Check Access Ratio", e -> showRatio(ratioStudentId.getValue().trim()));
        ratioBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button refresh = new Button("Refresh Leaderboard", ev -> refreshLeaderboard());

        add(new HorizontalLayout(refresh, ratioStudentId, ratioBtn), leaderGrid);
    }

    private void showRatio(String studentId) {
        if (studentId.isBlank()) {
            Notification.show("Please enter a Student ID.");
            return;
        }
        try {
            Map<String, Long> ratio = faceIdLogService.getStudentAccessRatio(studentId);
            long in  = ratio.getOrDefault("IN",  0L);
            long out = ratio.getOrDefault("OUT", 0L);

            Dialog d = new Dialog();
            d.setHeaderTitle("Access Ratio for " + studentId);
            d.add(new Span("IN: " + in + "   |   OUT: " + out));
            d.getFooter().add(new Button("Close", e -> d.close()));
            d.open();
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCrud() {
        crudGrid.setItems(faceIdLogService.findAll());
    }

    private void refreshLeaderboard() {
        List<AnalyticsDTO.FaceIdFrequencyMetric> data = faceIdLogService.getAttendanceLeaderboard(30);
        leaderGrid.setItems(data);
    }

    private void refreshAll() {
        refreshCrud();
        refreshLeaderboard();
    }
}
