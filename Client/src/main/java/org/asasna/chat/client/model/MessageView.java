package org.asasna.chat.client.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.util.Duration;
import org.asasna.chat.client.view.ChatController;
import org.asasna.chat.client.view.SpeechDirection;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.MessageType;
import org.asasna.chat.common.model.User;
import org.jcodec.common.model.AudioBuffer;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class MessageView extends HBox {
    private SpeechDirection direction;
    private Text displayedText;
    private Message message;
    private Image image;
    private Timestamp timestamp;
    static int i=0;

    public MessageView(Message message) {
        this.message = message;


    }

    public Text getDisplayedText() {
        return displayedText;
    }

    public void setDirection(SpeechDirection direction) {
        this.direction = direction;
        setupElements();
    }

    private void setupElements() {
        if (direction == SpeechDirection.RIGHT) {
            configureForSender();
        } else {
            configureForReceiver();
        }
    }

    private void setCommonConfigurations() {
        timestamp = new Timestamp(System.currentTimeMillis());
        if (message.getMessageType() != MessageType.AUDIO) {
            displayedText = new Text(message.getMesssagecontent());
            displayedText.setTextAlignment(TextAlignment.LEFT);
            displayedText.setFont(Font.font("Consolas", FontPosture.REGULAR, 16));
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void configureForSender() {
        HBox container;
        setCommonConfigurations();
        if (message.getMessageType() != MessageType.AUDIO) {
            displayedText.setFill(Color.WHITE);
            TextFlow textFlow = new TextFlow(displayedText);
            container = new HBox(textFlow);
            container.setMargin(textFlow, new Insets(10, 10, 15, 10));
        } else {
            container = recieveRecord(message.getUserId(), message.getMesssageAudioContent());

        }
        container.setStyle("-fx-background-radius: 15; -fx-background-color: #1e82dc ");
        container.setAlignment(Pos.CENTER_LEFT);
        container.maxWidthProperty().bind(widthProperty().multiply(0.70));
        getChildren().add(container);
        setAlignment(Pos.CENTER_RIGHT);
        this.setMargin(container, new Insets(5, 10, 10, 10));

    }

    public void configureForReceiver() {
        setCommonConfigurations();
        HBox container;
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setCenterY(75);
        if (image != null) {
            circle.setFill(new ImagePattern(image));
        }
        if (message.getMessageType() != MessageType.AUDIO) {
            displayedText.setTextAlignment(TextAlignment.LEFT);
            displayedText.setFill(Color.BLACK);
            TextFlow textFlow = new TextFlow(displayedText);
            container = new HBox(circle, textFlow);
            container.setMargin(textFlow, new Insets(10, 10, 15, 10));
        } else {
            HBox smallContainer = recieveRecord(message.getUserId(), message.getMesssageAudioContent());
            container = new HBox(circle, smallContainer);
        }

        container.setStyle("-fx-background-radius: 15; -fx-background-color: GAINSBORO ");
        container.setAlignment(Pos.CENTER_LEFT);
        container.maxWidthProperty().bind(widthProperty().multiply(0.70));
        getChildren().addAll(circle, container);
        setAlignment(Pos.CENTER_LEFT);
        this.setMargin(container, new Insets(5, 10, 10, 10));
    }

    public HBox recieveRecord(int senderId, byte[] buf) {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        AudioBuffer audioBuffer = new AudioBuffer(ByteBuffer.wrap(buf), new org.jcodec.common.AudioFormat(44100, 16, 2, true, false), buf.length);

        //            FileOutputStream fileOutputStream=new FileOutputStream();
        // checks if system supports the data line
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
//            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
//
//            line.write(buf, 0, buf.length);
        for (int i = 0; i < 10; i++)
            System.out.println("Buf: " + buf[i]);

        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(buf), format, buf.length / format.getFrameSize());
        File wavFile = new File("./Client/src/main/resources/org/asasna/chat/client/audio/record2"+i+".wav");
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ais.close();
           // wavFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //AudioClip clip=new AudioClip(getClass().getResource("record2.wav").toExternalForm());


        Media media = new Media(Paths.get("./Client/src/main/resources/org/asasna/chat/client/audio/record2"+i+".wav").toUri().toString());
        //AudioClip audioClip = new AudioClip(Paths.get("./Client/src/main/resources/org/asasna/chat/client/audio/record2.wav").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        HBox box = new HBox();
        //box.setStyle("-fx-background-color: red;-fx-pref-height: 20px");
        FontIcon playIcon = new FontIcon();
        playIcon.setIconLiteral("dashicons-controls-play");
        playIcon.setIconColor(Color.BLACK);
        playIcon.setIconSize(25);
        JFXButton startStop = new JFXButton(">");
        /*startStop.setOnAction(e->{
        box.setStyle("-fx-background-color: red;-fx-pref-height: 20px");
        JFXButton startStop = new JFXButton(">");
        startStop.setOnAction(e -> {
            mediaPlayer.play();
            mediaPlayer.seek(Duration.ZERO);
            /*if(mediaPlayer.getStatus()== MediaPlayer.Status.STOPPED){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });*/
        playIcon.setOnMouseClicked(e -> {
            Platform.runLater(() -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    playIcon.setIconLiteral("dashicons-controls-play");
                    mediaPlayer.pause();
                } else {
                    playIcon.setIconLiteral("dashicons-controls-pause");
                    playIcon.setIconColor(Color.RED);
                    mediaPlayer.play();
                }

                // playIcon.setIconColor(Color.BLACK);
            });


        });

        mediaPlayer.setOnPlaying(() -> {
            playIcon.setIconColor(Color.RED);
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            playIcon.setIconColor(Color.BLACK);
            playIcon.setIconLiteral("dashicons-controls-play");
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
        JFXSlider slider = new JFXSlider();
        /*slider.setMax(mediaPlayer.getTotalDuration().toMillis());
        slider.setOnMouseClicked((MouseEvent mouseEvent) -> {
            mediaPlayer.seek(Duration.millis(slider.getValue()));
        });
        mediaPlayer.currentTimeProperty().addListener((ob, ol, nw) -> {
            slider.setValue(nw.toMillis());
        });*/
        Platform.runLater(() -> {
            slider.maxProperty().bind(Bindings.createDoubleBinding(
                    () -> mediaPlayer.getTotalDuration().toSeconds(),
                    mediaPlayer.totalDurationProperty()));
            mediaPlayer.currentTimeProperty().addListener((ob, ol, nw) -> {
                slider.setValue(nw.toSeconds());
            });
            slider.setOnMouseClicked((e) -> {
                mediaPlayer.seek(Duration.seconds(slider.getValue()));
            });
        });

        MediaView mediaView = new MediaView(mediaPlayer);
        box.setSpacing(10);
        box.getChildren().add(playIcon);
        box.getChildren().add(slider);
        i++;
        return box;

    }
}