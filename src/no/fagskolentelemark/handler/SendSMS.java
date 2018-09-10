package no.fagskolentelemark.handler;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import no.fagskolentelemark.PasswordMain;
import no.fagskolentelemark.objects.Student;
import no.fagskolentelemark.util.SMS;

public class SendSMS {

	public static void sendSMS(String adFilePath, String extensFilePath, boolean testMode) {
		List<Student> students = ComboCheck.comboTest(adFilePath, extensFilePath, false);
		System.out.println("Sender SMS...");

		int i = 0;
		int smsSent = 0;
		for (Student s : students) {
			// Sent SMS
			if (String.valueOf(s.getPhoneNumber()).length() == 8) {
				String msg = "Ditt brukernavn: " + s.getUsername() + "\nDitt passord: " + s.getPassword() + "\nhttp://bit.ly/fatelit";;

				if (!testMode) SMS.sendSMS(s.getPhoneNumber(), msg);

				else if (i == students.size()/2) SMS.sendSMS(45660785, msg);

				else if (i == students.size()-1) SMS.sendSMS(95242538, msg);

				else try { Thread.sleep(150); } catch (Exception e) {}
				smsSent++;
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
		final int sent = smsSent;



		Platform.runLater(() -> {
			if (PasswordMain.loader != null) {
				ProgressBar progressBar = (ProgressBar) PasswordMain.loader.getNamespace().get("progress");
				progressBar.setVisible(false);
				progressBar.setProgress(0);
			}


			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Fagskolen Telemark - Popup");
			alert.setHeaderText("SMS utsending fullført");
			alert.setContentText("Studenter funnet: " + students.size() + "\nSMS sendt: " + sent + "\nPris per: 0,195 kr\nTotal pris: "
					+ String.format("%.2f", sent*0.195) + " kr");
			alert.showAndWait();
		});
	}
}