//Programmed by Mao Yurun
package comp307.backend.account.Object.DataTransferObject;

public class MessageRequest {
    private String senderEmail;
    private String receiverEmail;
    private String message;
    public MessageRequest() {}
    public MessageRequest(String senderEmail, String receiverEmail, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;

    }
    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getMessage() {
        return message;
    }
}
