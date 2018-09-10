package no.fagskolentelemark.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import no.fagskolentelemark.objects.Student;
import no.fagskolentelemark.util.Util;

public class ActiveDirCheck {

	public static List<Student> processAdList(String adFilePath, boolean popup) {
		try {
			System.out.println("Tester AD listen...");

			// Input
			File adFile = new File(adFilePath);
			BufferedReader adList = new BufferedReader(new InputStreamReader(new FileInputStream(adFile), "UTF8"));

			// Reader
			List<Student> students = new ArrayList<Student>();
			HashMap<String, Integer> headers = new HashMap<String, Integer>();

			String line;
			int rows = 0;
			int warnings = 0;
			while ((line = adList.readLine()) != null) {
				line = line.replace("\"", "").replace("?", "");
				if (!line.isEmpty()) rows++;
				if (!line.isEmpty() && line.contains(";")) {
					String[] row = line.split(";");

					// Load headers
					if (headers.isEmpty()) {
						int i = 0;
						for (String value : row) {
							value = value.replaceAll("[^a-zA-Z0-9 ÆØÅæøå-]", "");
							headers.put(value, i);
							System.out.println(value + " = " + i);
							i++;
						}
					}

					// Handle rows
					else {
						// Last brukerdata
						String group = row[headers.get("Klasse")].replace("TELVS-", "");
						String firstName = row[headers.get("Fornavn")];
						String lastName = row[headers.get("Etternavn")];
						String username = row[headers.get("Brukernavn")];
						String password = row[headers.get("Passord")];
						String telefon = row[headers.get("Telefon")];
						String schoolEmail = row[headers.get("SkoleEpost")];
						int phone = 0;
						try { phone = Util.parsePhone(telefon); } catch (Exception ex) {}

						// Sjekk for feil
						if (String.valueOf(phone).length() != 8) {
							System.out.println(username + " mangler telefon nummer.");
							warnings++;
						}
						if (group.length() < 3) {
							System.out.println(username + " mangler klasse?");
							warnings++;
						}
						if (firstName.length() < 2) {
							System.out.println(username + " mangler fornavn?");
							warnings++;
						}
						if (lastName.length() < 2) {
							System.out.println(username + " mangler etternavn?");
							warnings++;
						}
						if (username.length() < 3) {
							System.out.println(firstName + " " + lastName + " mangler brukernavn?");
							warnings++;
						}
						if (password.length() < 5) {
							System.out.println(username + " mangler passord?");
							warnings++;
						}
						if (!schoolEmail.contains("@")) {
							System.out.println(username + " mangler skole e-post.");
							warnings++;
						}

						// Lag student
						Student student = new Student(group, firstName, lastName, username, password, phone, schoolEmail, null);
						students.add(student);
					}
				}
			}
			adList.close();

			// Feedback
			System.out.println("Studenter funnet: " + students.size());
			System.out.println("Rader funnet:" + rows);
			System.out.println("Feil oppdaget: " + warnings);

			if (popup) {
				final int rowsFound = rows;
				final int finalWarnings = warnings;
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Fagskolen Telemark - Popup");
					alert.setHeaderText("Test av AD listen");
					alert.setContentText("Studenter funnet: " + students.size() + "\nStudenter magler: " + (rowsFound-1-students.size()) + "\nRader funnet: "
							+ rowsFound + "\nFeil oppdaget: " + finalWarnings);
					alert.showAndWait();
				});
			}

			return students;


		} catch (Exception e) {
			if (popup) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fagskolen Telemark - Popup");
					alert.setHeaderText("Sjekk at filstien er riktig!");
					alert.setContentText("En feil ble oppdagget, sjekk konsoll for mer informasjon.");
					alert.showAndWait();
				});
			}

			e.printStackTrace();
			return null;
		}
	}
}
