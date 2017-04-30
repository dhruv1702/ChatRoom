package assignment7;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ClientMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Connect");
		dialog.setHeaderText("Connect to Chat Room");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		//TextField ip = new TextField("127.0.0.1");
		TextField ip = new TextField("localhost");
		ip.setPromptText("IP");
		TextField username = new TextField();
		username.setPromptText("Username");

		grid.add(new Label("IP:"), 0, 0);
		grid.add(ip, 1, 0);
		grid.add(new Label("Username:"), 0, 1);
		grid.add(username, 1, 1);
		dialog.getDialogPane().setContent(grid);


		ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		Platform.runLater(() -> ip.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(ip.getText(), username.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(pair -> {
			connect(pair.getKey(), pair.getValue());
		});
	}

	public static void connect(String ip, String username){
		Client client = new Client(ip, username);
		Thread x = new Thread(client);
		Client_GUI gui = new Client_GUI(ip, username, client);
		client.setGUI(gui);
		try {
			gui.start(new Stage());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		x.start();
	}


	public static void main(String[] args) {
		launch(args);
	}
}