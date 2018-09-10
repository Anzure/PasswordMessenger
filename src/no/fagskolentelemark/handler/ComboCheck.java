package no.fagskolentelemark.handler;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import no.fagskolentelemark.objects.Student;

public class ComboCheck {

	public static List<Student> comboTest(String adFilePath, String extensFilePath, boolean popup){
		try {
			System.out.println("Tester kombinering...");

			List<Student> adStudents = ActiveDirCheck.processAdList(adFilePath, false);
			List<Student> extensStudents = ExtensCheck.processExtensList(extensFilePath, false);
			List<Student> comboStudents = new ArrayList<Student>();
			int warnings = 0;
			int phoneMissing = 0;
			int emailMissing = 0;
			int phoneAndEmailMissing = 0;

			for (Student adStudent : adStudents){
				for (Student exStudent : extensStudents) {
					if (exStudent.getFirstName().equalsIgnoreCase(adStudent.getFirstName())
							&& adStudent.getLastName().equalsIgnoreCase(exStudent.getLastName())) {
						if (exStudent.getPhoneNumber() == adStudent.getPhoneNumber()
								|| exStudent.getGroup().equalsIgnoreCase(adStudent.getGroup())) {
							// Correct student found
							adStudent.setPersonalEmail(exStudent.getPersonalEmail());
							if (adStudent.getPhoneNumber() == 0 && exStudent.getPhoneNumber() > 0) {
								adStudent.setPhoneNumber(exStudent.getPhoneNumber());
							}
							comboStudents.add(adStudent);
							if (adStudent.getPersonalEmail() == null || !adStudent.getPersonalEmail().contains("@")) {
								emailMissing++;
							}
							if (String.valueOf(adStudent.getPhoneNumber()).length() != 8) {
								phoneMissing++;
							}
							if ((adStudent.getPersonalEmail() == null || !adStudent.getPersonalEmail().contains("@"))
									&& String.valueOf(adStudent.getPhoneNumber()).length() != 8) {
								phoneAndEmailMissing++;
							}
						} else {
							warnings++;
						}
						continue;
					}
				}
			}

			// Feedback
			System.out.println("AD liste: " + adStudents.size() + " studenter");
			System.out.println("Extens liste: " + extensStudents.size() + " studenter");
			System.out.println("Resultat: " + comboStudents.size() + " studenter");

			if (popup) {
				final int finalWarnings = warnings;
				final int tlfMissing = phoneMissing;
				final int mailMissing = emailMissing;
				final int tlfAndEmailMissing = phoneAndEmailMissing;
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Fagskolen Telemark - Popup");
					alert.setHeaderText("Test av AD listen");
					alert.setContentText("AD studenter: " + adStudents.size() + "\nExtens studenter: " + extensStudents.size() + "\nResultat: "
							+ comboStudents.size() + " studenter\nFeil oppdaget: " + finalWarnings + "\n\nMangler tlf: " + tlfMissing + "\n"
							+ "Mangler epost: " + mailMissing + "\nMangler tlf & epost: " + tlfAndEmailMissing);
					alert.showAndWait();
				});
			}
			return comboStudents;

		} catch (Exception ex) {
			if (popup) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fagskolen Telemark - Popup");
					alert.setHeaderText("Sjekk at filformatet stemmer!");
					alert.setContentText("En feil ble oppdagget, sjekk konsoll for mer informasjon.");
					alert.showAndWait();
				});
			}

			return null;
		}
	}
}