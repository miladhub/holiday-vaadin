package org.miladhub.holidays;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VaadinHolidayPlanner extends UI {
    private final HolidayContext context = new InMemoryHolidaySettings();
    private final HoursTaken hoursTaken = new InMemoryHoursTaken();
    private final Map<String, PlannedMonth> months = new LinkedHashMap<>();
    private final Plan plan = (month, remainingHoursOff, remainingVacationHours) ->
            months.put(month, new PlannedMonth(month, remainingHoursOff, remainingVacationHours,
                    hoursTaken.hoursOffTaken(month), hoursTaken.vacationHoursTaken(month)));
    private final HolidayPlanner planner = new HolidayPlanner(plan, hoursTaken, context);

    private String endMonth = "Aug-2016";
    private GridLayout grid;
    private VerticalLayout rightColumn;
    private Table table;
    private String selectedMonth;
    private Label selectedMonthLabel;
    private Button applyTakeHours;
    private Button applyTakeVacationHours;
    private TextField takeHoursField;
    private TextField takeVacationHoursField;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        rightColumn = new VerticalLayout();
        rightColumn.setSpacing(true);
        rightColumn.setMargin(true);

        grid = new GridLayout(2, 1);
        grid.addComponent(rightColumn, 1, 0);

        setContent(grid);

        createTable();
        createEndMonthField();
        createTakeHoursFields();

        plan();
    }

    private void createTakeHoursFields() {
        selectedMonthLabel = new Label("-");
        selectedMonthLabel.setCaption("Selected month:");
        rightColumn.addComponent(selectedMonthLabel);

        takeHoursField = new TextField();
        takeHoursField.setCaption("Take hours:");
        takeHoursField.setConverter(Double.class);
        takeHoursField.setNullRepresentation("");
        rightColumn.addComponent(takeHoursField);

        applyTakeHours = new Button("Apply");
        applyTakeHours.addClickListener((Button.ClickListener) event -> {
            PlannedMonth month = months.get(selectedMonth);
            month.setTakenHoursOff((Double) takeHoursField.getConvertedValue());
            takeHoursOff(month);
        });
        applyTakeHours.setEnabled(false);
        rightColumn.addComponent(applyTakeHours);

        takeVacationHoursField = new TextField();
        takeVacationHoursField.setCaption("Take vacation hours:");
        takeVacationHoursField.setConverter(Double.class);
        takeVacationHoursField.setNullRepresentation("");
        rightColumn.addComponent(takeVacationHoursField);

        applyTakeVacationHours = new Button("Apply");
        applyTakeVacationHours.addClickListener((Button.ClickListener) event -> {
            PlannedMonth month = months.get(selectedMonth);
            month.setTakenVacationHours((Double) takeVacationHoursField.getConvertedValue());
            takeVacationHours(month);
        });
        applyTakeVacationHours.setEnabled(false);
        rightColumn.addComponent(applyTakeVacationHours);
    }

    private void createTable() {
        table = new Table();

        table.setSortEnabled(false);
        table.setSelectable(true);
        table.addValueChangeListener((Property.ValueChangeListener) event -> {
            takeHoursField.setValue(null);
            takeVacationHoursField.setValue(null);
            if (event.getProperty().getValue() != null) {
                String month = (String) event.getProperty().getValue();
                selectedMonth = month;
                applyTakeHours.setEnabled(true);
                applyTakeVacationHours.setEnabled(true);
                selectedMonthLabel.setValue(month);
                takeHoursField.setValue(Double.toString(months.get(month).getTakenHoursOff()));
                takeVacationHoursField.setValue(Double.toString(months.get(month).getTakenVacationHours()));
            } else {
                selectedMonth = null;
                applyTakeHours.setEnabled(false);
                applyTakeVacationHours.setEnabled(false);
                selectedMonthLabel.setValue("-");
            }
        });

        table.addContainerProperty("Month", String.class, null);
        table.addContainerProperty("Hours off", Double.class, null);
        table.addContainerProperty("Hours off taken", Double.class, null);
        table.addContainerProperty("Vacations", Double.class, null);
        table.addContainerProperty("Vacations taken", Double.class, null);

        grid.addComponent(table, 0, 0);
    }

    private void createEndMonthField() {
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
        final DateField endMonthField = new DateField();
        endMonthField.setCaption("End month:");
        endMonthField.addValueChangeListener((Property.ValueChangeListener) event -> {
            endMonth = df.format(event.getProperty().getValue());
            planAndRefreshTable();
        });
        try {
            endMonthField.setValue(df.parse(endMonth));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        rightColumn.addComponent(endMonthField);
    }

    private void takeHoursOff(PlannedMonth month) {
        planner.takeHoursOff(month.getTakenHoursOff(), month.getMonth());
        planAndRefreshTable();
    }

    private void takeVacationHours(PlannedMonth month) {
        planner.takeVacationHours(month.getTakenVacationHours(), month.getMonth());
        planAndRefreshTable();
    }

    private void planAndRefreshTable() {
        plan();
        refreshTable();
    }

    private void plan() {
        months.clear();
        planner.planUntil(endMonth);
    }

    private void refreshTable() {
        table.getContainerDataSource().removeAllItems();
        populateTable();
    }

    private void populateTable() {
        for (PlannedMonth m : months.values()) {
            table.addItem(new Object[]{m.getMonth(), m.getHoursOff(), m.getTakenHoursOff(),
                    m.getVacationHours(), m.getTakenVacationHours()}, m.getMonth());
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VaadinHolidayPlanner.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
