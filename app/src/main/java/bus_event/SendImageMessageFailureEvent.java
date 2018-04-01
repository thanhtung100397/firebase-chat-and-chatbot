package bus_event;

import com.ttt.chat_module.models.message_models.ImageMessage;

public class SendImageMessageFailureEvent {
    private ImageMessage imageMessage;

    public SendImageMessageFailureEvent(ImageMessage imageMessage) {
        this.imageMessage = imageMessage;
    }

    public ImageMessage getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(ImageMessage imageMessage) {
        this.imageMessage = imageMessage;
    }
}
