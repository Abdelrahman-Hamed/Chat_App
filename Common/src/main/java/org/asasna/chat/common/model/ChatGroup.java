package org.asasna.chat.common.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChatGroup implements Serializable {

    private int groupId;
    private List<Integer> participents;
    private transient Image image;
    private String name;

    public ChatGroup() {
    }

    public ChatGroup(int groupId, List<Integer> participents, Image image, String name) {
        this.groupId = groupId;
        this.participents = participents;
        this.image = image;
        this.name = name;
    }

    public ChatGroup(int groupId, List<Integer> participents, String name) {
        this.groupId = groupId;
        this.participents = participents;
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Integer> getParticipents() {
        return participents;
    }

    public void setParticipents(List<Integer> participents) {
        this.participents = participents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(objectOutputStream);
        if (image != null)
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", gzipOutputStream);
        gzipOutputStream.finish();
        //objectOutputStream.writeObject(name);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        GZIPInputStream inputStream = new GZIPInputStream(objectInputStream);
        new Thread(() -> {
           /* try {
                image = SwingFXUtils.toFXImage(ImageIO.read(inputStream), null);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }).start();
    }
}
