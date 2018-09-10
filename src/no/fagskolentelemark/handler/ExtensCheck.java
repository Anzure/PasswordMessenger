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

public class ExtensCheck {

	public static List<Student> processExtensList(String extensFilePath, boolean popup) {
		try {
			System.out.println("Tester Extens listen...");

			// Input
			File extensFile = new File(extensFilePath);
			BufferedReader extensList = new BufferedReader(new InputStreamReader(new FileInputStream(extensFile), "UTF8"));

			// Reader
			List<Student> students = new ArrayList<Student>();
			HashMap<String, Integer> headers = new HashMap<String, Integer>();

			String line;
			int rows = 0;
			int warnings = 0;
			while ((line = extensList.readLine()) != null) {
				line = line.replace("\"", "").replace("?", "");
				if (line.endsWith(";")) line+= " ";
				if (line.contains(";;")) line.replace(";;", "; ;");
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
						if (row.length != 5) {
							System.out.println("DEBUG: " + line);

						} else {

							// Last brukerdata
							String group = row[headers.get("Klasse")];
							String firstName = row[headers.get("Fornavn")];
							String lastName = row[headers.get("Etternavn")];
							String telefon = row[headers.get("Telefon")];
							String personalEmail = row[headers.get("Epost")];
							int phone = 0;
							try { phone = Util.parsePhone(telefon); } catch (Exception ex) {}

							// Sjekk for feil
							if (String.valueOf(phone).length() != 8) {
								System.out.println(firstName + " " + lastName + " mangler telefon nummer.");
								warnings++;
							}
							if (group.length() < 3) {
								System.out.println(firstName + " " + lastName + " mangler klasse?");
								warnings++;
							}
							if (firstName.length() < 2) {
								System.out.println(firstName + " " + lastName + " mangler fornavn?");
								warnings++;
							}
							if (lastName.length() < 2) {
								System.out.println(firstName + " " + lastName + " mangler etternavn?");
								warnings++;
							}
							if (!personalEmail.contains("@")) {
								System.out.println(firstName + " " + lastName + " mangler personlig e-post.");
								warnings++;
							}

							// Lag student
							Student student = new Student(group, firstName, lastName, null, null, phone, null, personalEmail);
							students.add(student);
						}
					}
				}
			}
			extensList.close();

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
					alert.setHeaderText("Test av Extens listen");
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