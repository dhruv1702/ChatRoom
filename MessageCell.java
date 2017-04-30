/* CHAT ROOM <MyClass.java>  
 * EE422C Project 7 submission by  
 * Dhruv Verma  
 * dv7229  
 * 16230
 * Slip days used: <0> 
 * Spring 2017 
 */


package assignment7;

import assignment7.ChatMessage.MessageType;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MessageCell extends ListCell<ChatMessage> {
	@Override
	public void updateItem(ChatMessage item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			setGraphic(null);
		}
		else{
			VBox node = new VBox();
			Text text  = new Text(); 
			if(item.getType() == MessageType.SERVER){
				node.setId("serverMessage");
				text.setText(item.getMessage() + "\n");
			}
			if(item.getType() == MessageType.SENT){
				text.setText(item.getSender() + ": " + item.getMessage() + "\n");
				node.setId("sentMessage");
			}
			if(item.getType() == MessageType.RECEIVED){
				text.setText(item.getSender() + ": " + item.getMessage() + "\n");
				node.setId("receivedMessage");
			}
			if(item.getType() == MessageType.ERROR){
				text.setText(item.getMessage() + "\n");
				node.setId("errorMessage");
			}
			if(item.isImage()){
	        	  text.setText(item.getSender() + ": " );
	        	  ImageView image_view = new ImageView();
	        	  image_view.setPreserveRatio(true);
	        	  image_view.fitWidthProperty().bind(this.widthProperty().subtract(20));
	        	  image_view.setFitHeight(item.getImage().getHeight());
	        	  image_view.setImage(item.getImage());
	              node.getChildren().add(image_view);
	    	  }
			node.getChildren().add(text);
			node.setAlignment(Pos.CENTER_LEFT);
			text.wrappingWidthProperty().bind(this.widthProperty().subtract(20));
			setGraphic(node);
		}
	}
}
