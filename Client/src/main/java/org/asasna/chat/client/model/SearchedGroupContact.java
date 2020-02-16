package org.asasna.chat.client.model;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

public class SearchedGroupContact extends Contact {
    private BooleanProperty isSelected;

    public SearchedGroupContact(User user) {
        super(user);
        VBox box = new VBox();
        JFXCheckBox checkBox = new JFXCheckBox();
        //this.isSelected.bind(checkBox.selectedProperty());
        checkBox.setStyle("-jfx-checked-color:#045ba5");
        box.getChildren().add(checkBox);
        box.setStyle("-fx-padding: 20 0 0 15");
        getChildren().add(box);
    }

    public boolean isIsSelected() {
        return isSelected.get();
    }

}
