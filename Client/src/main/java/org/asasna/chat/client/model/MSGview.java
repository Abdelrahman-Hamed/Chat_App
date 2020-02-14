package org.asasna.chat.client.model;

import javafx.scene.layout.HBox;
import org.asasna.chat.common.model.Message;

public class MSGview extends HBox {
    private Message message ;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MSGview(Message message) {
        this.message = message;
    }

    public void setTextMSGview(){

    }
    public void setVoiceMSGview(){

    }
    public void setImageMSGview(){

    }
    public void setFileMSGview(){

    }

}
