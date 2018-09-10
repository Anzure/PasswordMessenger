package no.fagskolentelemark.handler;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import no.fagskolentelemark.PasswordMain;
import no.fagskolentelemark.objects.Student;
import no.fagskolentelemark.util.Email;

public class SendEmail {

	public static void sendEmail(String adFilePath, String extensFilePath, boolean testMode) {
		List<Student> students = ComboCheck.comboTest(adFilePath, extensFilePath, false);
		System.out.println("Sender e-poster...");

		Email.init();

		int i = 0;
		int emailSent = 0;
		for (Student s : students) {
			// Send email
			if (s.getPersonalEmail().contains("@")) {
				String htmlText = "<p><b>Hei, " + s.getFirstName() + " " + s.getLastName() + "</b></p>"
						+ "<p>&nbsp;</p><p>Fagskolen Telemark sitt nettverk er tilknyttet et fylkeskommentalt nettverk. For å få tilgang til nettverket er det viktig du tar vare på denne informasjonen, og eventuelt husker denne meldingen.</p><p>&nbsp;</p>"
						+ "<p><b>Ditt brukernavn: " + s.getUsername() + "</b></p><p><b>Ditt passord: " + s.getPassword() + "</b></p>"
						+ "<p>&nbsp;</p><p>Ditt brukernavn og passord har flere bruksområder, dette bruker du for å logge inn på WiFi, Fronter feide, Office 365 og kopimaskinen på skolen.</p><p>&nbsp;</p><p>Fronter feide og Office innlogging finner du nederst på <a href='http://fagskolentelemark.no'>http://fagskolentelemark.no</a></p><p>&nbsp;</p><p><b>Office installasjon</b></p><p>Se video her <a href='https://www.youtube.com/watch?v=9h3cR2B0KF8'>https://www.youtube.com/watch?v=9h3cR2B0KF8</a></p><p>&nbsp;</p><p><b>VIKTIG:</b></p><p>Dersom du har glemt passordet ditt, så kan du lage nytt via nettstedet <a href='https://passord.vfk.no'>https://passord.vfk.no</a> </p><p>(Husk å velg domene SKOLE)</p><p>&nbsp;</p>"
						+ "<p><b>Diverse linker</b></p><p><a href='https://www.autodesk.com/education/free-software/autocad'>AutoCad</a> og <a href='https://www.autodesk.com/education/free-software/revit'>Revit</a></p><p><a href='https://www.dropbox.com/s/zo3q3xzn8zzhx51/AutoCAD%20brukerveiledning.pdf?dl=0'>Lage konto Autodesk</a></p><p><a href='http://fagskolentelemark.vgs.t-fk.no/Praktisk-info/IKT/Systemkrav-for-kjoep-av-PC-og-kalkulator-til-vaare-utdanninger'>Systemkrav for kjøp av PC</a></p><p><a href='https://www.dropbox.com/s/gzah5hhx07hoeea/Innlogging%2Btil%2BFEIDE.pdf?dl=0'>Innlogging Fronter Feide</a></p>";

				if (!testMode) Email.sendMail(htmlText, s.getPersonalEmail());

				else if (i == 3) Email.sendMail(htmlText, "adriant@t-fk.no");

				else if (i == 4) Email.sendMail(htmlText, "0810maan@t-fk.no");

				else try { Thread.sleep(50); } catch (Exception e) {}
				emailSent++;
			}

			// Update progress
			i++;
			final double progress = (double)i / (double)students.size();
			Platform.runLater(() -> {
				if (PasswordMain.loader != null) {
					ProgressBar progressBar = (ProgressBar) PasswordMain.loader.getNamespace().get("progress");
					progressBar.setVisible(true);
					progressBar.setProgress(progress);
				}
			});

		}
		final int sent = emailSent;


		Platform.runLater(() -> {
			if (PasswordMain.loader != null) {
				ProgressBar progressBar = (ProgressBar) PasswordMain.loader.getNamespace().get("progress");
				progressBar.setVisible(false);
				progressBar.setProgress(0);
			}
		});

		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Fagskolen Telemark - Popup");
			alert.setHeaderText("E-post utsending fullført");
			alert.setContentText("Studenter funnet: " + students.size() + "\nE-poster sendt: " + sent);
			alert.showAndWait();
		});
	}
}