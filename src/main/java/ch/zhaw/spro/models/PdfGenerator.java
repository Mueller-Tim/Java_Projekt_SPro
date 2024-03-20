package ch.zhaw.spro.models;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


/**
 * The PdfGenerator class is responsible for creating and managing PDF documents
 * related to employee shift schedules. It allows for sorting and displaying shifts
 * in a weekly format.
 */
public class PdfGenerator {

	private static final String LOGO = ".\\src\\main\\resources\\images\\logo_transparent_s_pro_big.png";
	private ArrayList<Employee> employees;
	private ArrayList<ShiftType> shiftTypes;
	private ArrayList<Absence> absences;
	private PDPageContentStream contentStream;
	private PDDocument document;
	private int cellHeight;
	private int[] columWidth;

	private int xInitialPosition;
	private int xPosition;
	private int yPosition;
	private Color fontColor;
	private PDPage page;

	private int pageHeight;
	private int pageWidth;
	private final List<String> paths = new ArrayList<>();
	private static final PDFont FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

	private void initializeDocument() throws IOException {
		document = new PDDocument();
		pageHeight = (int) PDRectangle.A4.getWidth();
		pageWidth = (int) PDRectangle.A4.getHeight();
		PDRectangle a4Landscape = new PDRectangle(pageWidth, pageHeight);
		page = new PDPage(a4Landscape);
		page.setTrimBox(a4Landscape);
		document.addPage(page);
		contentStream = new PDPageContentStream(document, page);
	}

	/**
	 * Creates PDF documents for shift schedules, sorted by calendar weeks.
	 * A new PDF document is generated for each calendar week that has at least one shift scheduled.
	 * If more than 11 employees have shifts in the same week, the document will automatically create a second page.
	 * At the end, the PDFs are automatically saved in the downloads folder with the naming convention "time_schedule_calendarWeek_X",
	 * where X represents the number of the calendar week.
	 * PDFs are only generated for weeks with at least one scheduled shift; thus, some calendar weeks may not have a corresponding PDF.
	 * The method returns a list of strings, each representing the file path of a generated PDF document.
	 *
	 * @param unsortedShifts A list of unsorted shifts.
	 * @param employees      A list of employees.
	 * @param shiftTypes     A list of shift types.
	 * @param absences		 A list of all absences.
	 * @return List<String>  A list of file paths for the generated PDF documents.
	 * @throws IOException If an error occurs during the creation of the PDF.
	 */
	public List<String> createPdf(List<Shift> unsortedShifts, List<Employee> employees, List<ShiftType> shiftTypes, List<Absence> absences) throws IOException {
		paths.clear();
		Map<Integer, List<Shift>> weeklyShifts = sortShiftsIntoWeeks(unsortedShifts);

		this.employees = (ArrayList<Employee>) employees;
		this.shiftTypes = (ArrayList<ShiftType>) shiftTypes;
		this.absences = (ArrayList<Absence>) absences;
		sortShifts(weeklyShifts);

		for (Map.Entry<Integer, List<Shift>> calendarWeekShift : weeklyShifts.entrySet()){
			int calendarWeek = calendarWeekShift.getKey();
			List<Shift> shifts = calendarWeekShift.getValue();
			initializeDocument();
			createInfo();
			addTitle(calendarWeek);
			List<List<Shift>> pagesOfShifts = splitShiftsIntoPages(shifts);
			for (List<Shift> pageShifts : pagesOfShifts) {
				addLogo();
				printTable(pageShifts, calendarWeek);
				if (!pageShifts.equals(pagesOfShifts.get(pagesOfShifts.size() - 1))) {
					createNewPage();
				}
			}

			contentStream.close();
			paths.add(savePdf(calendarWeek));

			if (document != null) {
				document.close();
			}
		}
		return paths;
	}

	private List<List<Shift>> splitShiftsIntoPages(List<Shift> shifts) {
		int maxEmployeesPerPage = 11;
		List<List<Shift>> pages = new ArrayList<>();
		Set<String> seenEmployeeIds = new HashSet<>();
		List<Shift> currentPageShifts = new ArrayList<>();

		for (Shift shift : shifts) {
			if (seenEmployeeIds.contains(shift.getEmployeeId())) {
				currentPageShifts.add(shift);
			} else {
				if (seenEmployeeIds.size() == maxEmployeesPerPage) {
					pages.add(new ArrayList<>(currentPageShifts));
					currentPageShifts.clear();
					seenEmployeeIds.clear();
				}
				seenEmployeeIds.add(shift.getEmployeeId());
				currentPageShifts.add(shift);
			}
		}
		if (!currentPageShifts.isEmpty()) {
			pages.add(currentPageShifts);
		}

		return pages;
	}

	private void createNewPage() throws IOException {
		if (contentStream != null) {
			contentStream.close();
		}
		PDRectangle a4Landscape = new PDRectangle(pageWidth, pageHeight);
		page = new PDPage(a4Landscape);
		page.setTrimBox(a4Landscape);
		document.addPage(page);
		contentStream = new PDPageContentStream(document, page);
	}

	private void addLogo() throws IOException {
		PDImageXObject logo = PDImageXObject.createFromFile(LOGO, document);
		int originalWidth = logo.getWidth();
		int originalHeight = logo.getHeight();
		double aspectRatio = (double) originalWidth / (double) originalHeight;
		int desiredWidth = 100;
		int desiredHeight = (int) (desiredWidth / aspectRatio);
		contentStream.drawImage(logo, (float) pageWidth-120 , (float) pageHeight-55, desiredWidth, desiredHeight);
	}
	private void createInfo(){
		PDDocumentInformation information = document.getDocumentInformation();
		information.setTitle("Time Schedule");
		information.setCreationDate(Calendar.getInstance());
		information.setProducer("S-Pro");
	}

	private void printTable(List<Shift> shifts, int calendarWeek) throws IOException {
		contentStream.setFont(FONT,10);
		int nameCellWidth = 100;
		int timeCellWidth = 80;
		int shiftCellWidth = 110;
		int cellHeightTable = 40;
		int tableYPosition = pageHeight - 100;
		int tableXPosition = 25;
		int[] cellWidths = {nameCellWidth, timeCellWidth, shiftCellWidth, shiftCellWidth, shiftCellWidth,
				shiftCellWidth, shiftCellWidth};
		setTable(cellWidths, cellHeightTable, tableXPosition, tableYPosition);

		printTableHeader(cellWidths, calendarWeek, shifts.get(0).getDate());

		printEmployeeRows(shifts, cellWidths, tableXPosition, tableYPosition, cellHeightTable);

	}

	private void printEmployeeRows(List<Shift> shifts, int[] cellWidths, int tableXPosition, int tableYPosition, int cellHeight) throws IOException {
		int numberOfEmployees = countUniqueEmployeeIds(shifts);
		int columPosition = 0;
		int shiftCounter = 0;
		for (int j = 0; j < numberOfEmployees; j++){
			int[] nameCell = Arrays.copyOfRange(cellWidths, 0,1);
			int[] innerCell = Arrays.copyOfRange(cellWidths, 1, cellWidths.length);
			setTable(nameCell, cellHeight, tableXPosition, tableYPosition-cellHeight*(j+1));
			Shift currentShift = shifts.get(shiftCounter);
			String employeeId = currentShift.getEmployeeId();
			Employee employee = findEmployeeById(employeeId);
			String employeeName = employee.getFirstName() + " " + employee.getLastName();
			columPosition = addCell(employeeName, Color.WHITE, columPosition);
			setTable(innerCell, cellHeight/2, cellWidths[0] + tableXPosition, tableYPosition-cellHeight*(j+1)+cellHeight/2);
			int[] returnRow = printEmployeeRow(shifts, shiftCounter, columPosition, employeeId);
			shiftCounter = returnRow[0];
			columPosition = returnRow[1];
		}
	}

	private int[] printEmployeeRow(List<Shift> shifts, int shiftCounter, int columPosition, String employeeId) throws IOException {
		int numberOfTimeCells = 2;
		int shiftsPerEmployee = 10;
		int[] returnRow = new int[2];
		Shift currentShift;
		LocalDate currentDay = shifts.get(0).getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		ArrayList<LocalDate> absenceDate = getAbsencesFromEmployee(employeeId);
		for (int k = 0; k < shiftsPerEmployee + numberOfTimeCells; k++) {
			Color color = Color.WHITE;
			currentShift = shifts.get(shiftCounter);
			String text = addTimeText(k);
			if(Objects.equals(text, "")){
				ShiftType shiftType = findShiftTypeById(currentShift.getShiftTypeId());
				if (Objects.equals(currentShift.getEmployeeId(), employeeId) && currentShift.getDate().equals(currentDay) &&
						(k > 6 && Objects.equals(currentShift.getTime(), "Lunch Shift") ||
								k < 6 && Objects.equals(currentShift.getTime(), "Morning Shift"))) {
					color = Color.decode(shiftType.getColor());
					text = shiftType.getName();
					if(shiftCounter < shifts.size() -1){
						shiftCounter++;
					}
				}
				Color colorShift = color;
				color = checkForAbsence(absenceDate, color, currentDay);
				text = checkAbsenceColor(color, colorShift, text);

				currentDay = getNextWeekday(currentDay);
			}
			columPosition = addCell(text, color, columPosition);
		}
		returnRow[0] = shiftCounter;
		returnRow[1] = columPosition;
		return returnRow;
	}

	private Color checkForAbsence(ArrayList<LocalDate> absenceDate, Color color, LocalDate currentDay){
		String grey = "#999999";
		if (absenceDate.contains(currentDay)) {
			color = Color.decode(grey);
		}
		return color;
	}

	private String checkAbsenceColor(Color colorAbsence, Color colorShift, String text){
		if(colorAbsence != colorShift){
			text = "Absent";
		}
		return text;
	}

	private ArrayList<LocalDate> getAbsencesFromEmployee(String employeeId){
		ArrayList<LocalDate> absenceDate = new ArrayList<>();
		for (Absence absence: absences){
			if (Objects.equals(absence.getEmployeeId(), employeeId)){
				absenceDate.add(absence.getDate());
			}
		}
		return absenceDate;
	}

	private String addTimeText(int k){
		String text = "";
		int morningCell = 0;
		int afternoonCell = 6;
		if(k == morningCell){
			text = "08:00 - 12:00";
		} else if(k == afternoonCell){
			text = "13:00 - 16:00";
		}
		return text;
	}

	private LocalDate getNextWeekday(LocalDate date) {
		LocalDate nextDate = date.plusDays(1);
		if(nextDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
			nextDate = nextDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
		}
		return nextDate;
	}

	private int countUniqueEmployeeIds(List<Shift> shifts) {
		Set<String> uniqueEmployeeIds = new HashSet<>();
		for (Shift shift : shifts) {
			uniqueEmployeeIds.add(shift.getEmployeeId());
		}
		return uniqueEmployeeIds.size();
	}

	private void printTableHeader(int[] cellWidths, int calendarWeek, LocalDate year) throws IOException {
		DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

		LocalDate weekStart = year.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, calendarWeek)
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		int columPosition = 0;
		for (int i = 0; i < cellWidths.length; i++) {
			String text;
			if (i == 0) {
				text = "Name";
			} else if (i == 1) {
				text = "Time";
			} else {
				LocalDate date = weekStart.plusDays((long) i - 2);
				text = customFormatter.format(date);
			}
			columPosition = addCell(text, Color.WHITE, columPosition);
		}
	}


	private void addTitle(int calenderWeek) throws IOException {
		String title = "Time Schedule for Calender week " + calenderWeek + ":";
		int xPositionTitle = 20;
		int yPositionTitle = pageHeight - 50;
		float fontSize = 20;
		Color color = Color.BLACK;
		contentStream.beginText();
		contentStream.setFont(FONT, fontSize);
		contentStream.setNonStrokingColor(color);
		contentStream.newLineAtOffset(xPositionTitle, yPositionTitle);
		contentStream.showText(title);
		contentStream.endText();
		contentStream.moveTo(0, 0);
	}

	private int addCell(String text, Color fillColor, int columPosition) throws IOException{
		contentStream.setStrokingColor(Color.BLACK);
		if(fillColor != null){
			contentStream.setNonStrokingColor(fillColor);
		}

		contentStream.addRect(xPosition, yPosition, columWidth[columPosition], cellHeight);

		if(fillColor == null){
			contentStream.stroke();
		} else {
			contentStream.fillAndStroke();
		}

		contentStream.beginText();
		contentStream.setNonStrokingColor(fontColor);

		contentStream.newLineAtOffset((float) xPosition+10, yPosition + (float) (cellHeight - 10) / 2);

		contentStream.showText(text);
		contentStream.endText();

		xPosition = xPosition + columWidth[columPosition];
		columPosition++;

		if(columPosition == columWidth.length){
			columPosition = 0;
			xPosition = xInitialPosition;
			yPosition -= cellHeight;
		}
		return columPosition;
	}

	private void setTable(int[] columWidth, int cellHeight, int xPosition, int yPosition){
		this.columWidth = columWidth;
		this.cellHeight = cellHeight;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		xInitialPosition = xPosition;
		this.fontColor = Color.BLACK;
	}

	private String savePdf(int calenderWeek) throws IOException {
		String home = System.getProperty("user.home");
		String downloadPath = Paths.get(home, "Downloads", "time_schedule_calenderWeek_" + calenderWeek + ".pdf").toString();
		document.save(downloadPath);
		return downloadPath;
	}

	private Map<Integer, List<Shift>> sortShiftsIntoWeeks(List<Shift> shifts) {
		Map<Integer, List<Shift>> weeklyShifts = new HashMap<>();

		for (Shift shift : shifts) {
			int weekNumber = shift.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
			weeklyShifts.computeIfAbsent(weekNumber, k -> new ArrayList<>()).add(shift);
		}
		return weeklyShifts;
	}

	private void sortShifts(Map<Integer, List<Shift>> weeklyShifts) {
		for (Map.Entry<Integer, List<Shift>> entry : weeklyShifts.entrySet()) {
			List<Shift> sortedShifts = entry.getValue().stream()
					.sorted(Comparator.comparing(
							(Shift shift) -> findEmployeeById(shift.getEmployeeId()).getFirstName()
					).thenComparing(
							shift -> findEmployeeById(shift.getEmployeeId()).getLastName()
					).thenComparing(shift -> {
						if ("Morning Shift".equals(shift.getTime())){
							return 0;
						} else if ("Lunch Shift".equals(shift.getTime())) {
							return 1;
						} else {
							return -1;
						}
					}).thenComparing(Shift::getDate))
					.toList();
			entry.setValue(sortedShifts);
		}
	}
	private Employee findEmployeeById(String employeeId) {
		return employees.stream()
				.filter(employee -> String.valueOf(employee.getId()).equals(employeeId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeId + " not found"));
	}

	private ShiftType findShiftTypeById(String shiftTypeId) {
		return shiftTypes.stream()
				.filter(shiftType -> String.valueOf(shiftType.getId()).equals(shiftTypeId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Employee with ID " + shiftTypeId + " not found"));
	}
}