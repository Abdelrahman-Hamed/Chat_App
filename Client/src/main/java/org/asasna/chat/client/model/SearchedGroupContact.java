package org.asasna.chat.client.model;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.VBox;
import org.asasna.chat.common.model.User;

public class SearchedGroupContact extends Contact {
    private BooleanProperty selected=new SimpleBooleanProperty(false);
    private boolean checked;

    public SearchedGroupContact(User user) {
        super(user);
        VBox box = new VBox();
        JFXCheckBox checkBox = new JFXCheckBox();

        checkBox.setStyle("-jfx-checked-color:#045ba5");
        box.getChildren().add(checkBox);
        box.setStyle("-fx-padding: 20 0 0 15");
        checkBox.selectedProperty().bindBidirectional(selected);
        //checkBox.selectedProperty().bindBidirectional((BooleanProperty) selected);
        //this.selected.bind(checkBox.selectedProperty());
        getChildren().add(box);
    }

    public boolean getSelected() {
        if (selected != null)
            return selected.get();
        else
            return false;
    }

}
