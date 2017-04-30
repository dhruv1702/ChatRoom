/* CHAT ROOM <MyClass.java>  
 * EE422C Project 7 submission by  
 * Dhruv Verma  
 * dv7229  
 * 16230
 * Sagar Krishnaraj	
 * sk37433
 * Slip days used: 1 
 * Spring 2017 
 */


package assignment7;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserCell extends ListCell<String> {
	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		HBox node = new HBox();
		Circle online = new Circle(3);
		Text text  = new Text(item);
		if (item != null) {
			online.setFill(Color.web("#0ad159"));
			node.setSpacing(5);
			node.setAlignment(Pos.CENTER_LEFT);

			node.getChildren().addAll(online, text);
			setGraphic(node);
		}else{
			setGraphic(null);
		}
	}
}
