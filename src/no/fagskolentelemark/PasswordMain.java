package no.fagskolentelemark;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import no.fagskolentelemark.handler.ActiveDirCheck;
import no.fagskolentelemark.handler.ComboCheck;
import no.fagskolentelemark.handler.ExtensCheck;
import no.fagskolentelemark.handler.SendEmail;
import no.fagskolentelemark.handler.SendSMS;

public class PasswordMain extends Application {

	public static Scene scene = null;
	public static FXMLLoader loader = null;

	@Override
	public void start(Stage primaryWindow) throws Exception {
		primaryWindow.getIcons().add(new Image("icon.png"));

		// Load scene
		loader = new FXMLLoader(getClass().getResource("/PasswordScene.fxml"));
		Parent root = loader.load();
		scene = new Scene(root);

		// AD check button
		TextField adFilePath = (TextField)loader.getNamespace().get("adList");
		Button adCheckButton = (Button)loader.getNamespace().get("adCheck");
		adCheckButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Runnable r = new Runnable() {
					public void run() {
						ActiveDirCheck.processAdList(adFilePath.getText(), true);
					}
				};
				new Thread(r).start();
			}
		});

		// Extens check button
		TextField extensFilePath = (TextField)loader.getNamespace().get("extensList");
		Button extensCheckButton = (Button)loader.getNamespace().get("extensCheck");
		extensCheckButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Runnable r = new Runnable() {
					public void run() {
						ExtensCheck.processExtensList(extensFilePath.getText(), true);
					}
				};
				new Thread(r).start();
			}
		});

		// Combination check button
		Button comboCheckButton = (Button)loader.getNamespace().get("testButton");
		comboCheckButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Runnable r = new Runnable() {
					public void run() {
						ComboCheck.comboTest(adFilePath.getText(), extensFilePath.getText(), true);
					}
				};
				new Thread(r).start();
			}
		});

		// SMS send button
		CheckBox testMode = (CheckBox)loader.getNamespace().get("testBox");
		Button sendSmsButton = (Button)loader.getNamespace().get("sendSMS");
		sendSmsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Runnable r = new Runnable() {
					public void run() {
						SendSMS.sendSMS(adFilePath.getText(), extensFilePath.getText(), testMode.isSelected());
					}
				};
				new Thread(r).start();
			}
		});

		// Email send button
		Button sendEmailButton = (Button)loader.getNamespace().get("sendEmail");
		sendEmailButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Runnable r = new Runnable() {
					public void run() {
						SendEmail.sendEmail(adFilePath.getText(), extensFilePath.getText(), testMode.isSelected());
					}
				};
				new Thread(r).start();
			}
		});

		// Start scene
		primaryWindow.setScene(scene);
		primaryWindow.setTitle("Fagskolen Telemark - Passord utsender");
		primaryWindow.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}