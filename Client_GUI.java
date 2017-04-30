package assignment7;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import assignment7.ChatMessage.MessageType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Client_GUI extends Application implements Runnable  {
	private String hostname;
	private int port;
	private String username;
	private Client client;
	private ListView<ChatMessage> chatListView;
	private TextArea chatInput;
	private ListView<String> userListView;
	private ObservableList<String> userList;
	private ObservableList<ChatMessage> chatList;
	private ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
    private FileChooser fileChooser = new FileChooser();


	public Client_GUI(String host, String user, Client client) {
		this.hostname = host;
		this.port = 4000;
		this.username = user;
		this.client = client;
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();
/*	    HBox clientPane = new HBox();
	    VBox chatPane = new VBox();
		root.getChildren().add(clientPane);*/
		
		Scene scene = new Scene(root, 605, 450);

		chatInput = new TextArea();
		chatInput.setLayoutX(5);
		chatInput.setLayoutY(400);
		chatInput.setPrefSize(385, 45);
		chatInput.requestFocus();
		chatInput.setWrapText(true);
		root.getChildren().add(chatInput);

		chatListView = new ListView<ChatMessage>();
		chatListView.setLayoutX(5);
		chatListView.setLayoutY(5);
		chatListView.setPrefSize(440, 390);
		chatListView.setEditable(false);
		chatList = FXCollections.observableArrayList();
		chatListView.setItems(chatList);
		chatListView.setCellFactory((ListView<ChatMessage> l) -> new MessageCell());
		chatListView.setStyle("-fx-background-color: transparent;");

		root.getChildren().add(chatListView);

		userListView = new ListView<String>();
		userListView.setLayoutX(450);
		userListView.setLayoutY(5);
		userListView.setPrefSize(150, 440);
		userList = FXCollections.observableArrayList();
		userListView.setItems(userList);
		userListView.setStyle("");
		userListView.setCellFactory((ListView<String> l) -> new UserCell());
		root.getChildren().add(userListView);

/*		chatPane.getChildren().addAll(chatListView,chatInput);
	    clientPane.getChildren().addAll(chatPane,userListView);
	    chatPane.setVgrow(chatListView, Priority.ALWAYS);
        clientPane.setHgrow(chatPane, Priority.ALWAYS);*/



		chatListView.prefWidthProperty().bind(root.widthProperty().subtract(userListView.widthProperty()).subtract(10));
		chatListView.prefHeightProperty().bind(root.heightProperty().subtract(chatInput.heightProperty().add(15)));
		userListView.layoutXProperty().bind(root.widthProperty().subtract(155));
		userListView.prefHeightProperty().bind(root.heightProperty().subtract(10));
		chatInput.layoutYProperty().bind(root.heightProperty().subtract(chatInput.heightProperty().add(5)));

	        

		stage.setTitle("Chat Room " + hostname + ":" + port);
		stage.setScene(scene);

		stage.show();

		chatListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				event.consume();
			}
		});

		chatInput.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					try {
						client.sendMessage(chatInput.getText());
						chatInput.clear();
						ke.consume();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		

    	Image image = new Image("file:camera.png");
        ImageView imageView = new ImageView(image);
        //imageView.setPreserveRatio(true);
        
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
    	Button imageButton = new Button("",imageView);
    	imageButton.setLayoutX(395);
    	imageButton.setLayoutY(400);
    	imageButton.setPrefSize(45, 44);
    	root.getChildren().add(imageButton);

		imageButton.layoutXProperty().bind(userListView.layoutXProperty().subtract(imageButton.widthProperty()).subtract(5));
	    imageButton.layoutYProperty().bind(root.heightProperty().subtract(imageButton.heightProperty()).subtract(6));


    	imageButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle(ActionEvent e) {
    			File file = fileChooser.showOpenDialog(stage);
    			if(file != null){
    				try {
    					Image image = new Image("file:///" + file.getAbsolutePath());
    					client.sendPhoto(image);
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
    			}
    		}
    	});
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				System.exit(0);
			}
		});    

	}

	public void displayMessage(ChatMessage message){	
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				if(message.getSender().equals(username)){
					message.setType(MessageType.SENT);
				}
				else {	
					if(message.getSender().equals("SERVER")){
						message.setType(MessageType.SERVER);
					}
					else {
						if(message.getSender().equals("ERROR")){
							message.setType(MessageType.ERROR);
						}
						else {
							message.setType(MessageType.RECEIVED);
						}
					}
				}
				history.add(message);

				chatList.clear();
				chatList = FXCollections.observableArrayList(history);
				chatListView.setItems(chatList);

				userList.clear();
				userList = FXCollections.observableArrayList(message.getUsers());
				userListView.setItems(userList);
				chatListView.scrollTo(history.size()-1); 
			}
		});
	}

	@Override
	public void run() {
		try {
			start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}