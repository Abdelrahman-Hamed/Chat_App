package org.asasna.chat.client.model;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.asasna.chat.client.view.SpeechDirection;
import org.asasna.chat.common.model.Message;

public class MSGview extends HBox {

    private Color DEFAULT_SENDER_COLOR = Color.DARKBLUE;
    private Color DEFAULT_RECEIVER_COLOR = Color.CYAN;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;
    private SpeechDirection direction;
    private Label displayedText;
    private SVGPath directionIndicator;
    private Message message ;
    IChatController cController;
    boolean file;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MSGview(Message message) {
        this.message = message;
        initialiseDefaults();
        setupElements();
    }
    public MSGview(Message message,IChatController cController) {
        this.cController=cController;
        this.message = message;
        initialiseDefaults();
        //setupFileElements();
        file=true;
        setupElements();

    }

    public void setTextMSGview(SpeechDirection direction){
        this.direction = direction ;
        setupElements() ;
    }
    public void setVoiceMSGview(){

    }
    public void setImageMSGview(){

    }
    public void setFileMSGview(){

    }



    private void initialiseDefaults(){
        DEFAULT_SENDER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
    }

 /*   private void setupFileElements(){
        Image image = new Image(getClass().getResource("file.png").toExternalForm());
        displayedText  = new Label(message.getMesssagecontent());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        displayedText.setGraphic(imageView);
        /*System.out.println("l;j[hohophyi09oi1");
        imageView.setOnMouseClicked(e->{
            System.out.println("hello");
        });
        displayedText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Image Clicked");
            }
        });
//        displayedText.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println("1");
//                cController.reciveFile(message.getMesssagecontent());
//
//                event.consume();
//            }
//        });

        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        directionIndicator = new SVGPath();
        if(direction == SpeechDirection.LEFT){
            configureForReceiver();
        }
        else{
            configureForSender();
        }
    }*/

    private void setupElements(){
        displayedText = new Label(message.getMesssagecontent());
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        directionIndicator = new SVGPath();
        if(direction == SpeechDirection.LEFT){
            configureForReceiver();
        }
        else{
            configureForSender();
        }
    }

    private void configureForSender(){
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);

        HBox container = new HBox(displayedText, directionIndicator);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver(){
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);
        HBox container;

        if(file) {
            Image image = new Image(getClass().getResource("file.png").toExternalForm());
            displayedText  = new Label(message.getMesssagecontent());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
             container = new HBox(imageView,directionIndicator, displayedText);
             container.setBackground(DEFAULT_RECEIVER_BACKGROUND);
            // container.setFillHeight(a);
            //Use at most 75% of the width provided to the SpeechBox for displaying the message
            container.maxWidthProperty().bind(widthProperty().multiply(0.75));
            //container.getChildren().add(imageView);

            container.setOnMouseClicked((e) -> {
                System.out.println("file");
                cController.reciveFile(message.getMesssagecontent());
            });
            file=false;
        }
        else{
             container = new HBox(directionIndicator, displayedText);
            //Use at most 75% of the width provided to the SpeechBox for displaying the message
            container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        }
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }

}
