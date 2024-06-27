package site.pixeldetective.server.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class ChatDTO {

	private int chat_id;
	private String message;
	private String uName;
	private int sender;
	private Timestamp sent_at;


	@Override
	public String toString() {
		return "ChatDTO{" +
				"chat_id=" + chat_id +
				", message='" + message + '\'' +
				", uName='" + uName + '\'' +
				", sender=" + sender +
				", sent_at=" + sent_at +
				'}';
	}

	public ChatDTO(int chat_id, String message, String uName, int sender, Timestamp sent_at) {
		this.chat_id = chat_id;
		this.message = message;
		this.uName = uName;
		this.sender = sender;
		this.sent_at = sent_at;
	}

	public ChatDTO(String message, String uName, int sender, Timestamp sent_at) {
		this.message = message;
		this.uName = uName;
		this.sender = sender;
		this.sent_at = sent_at;
	}

	public ChatDTO() {
	}

	public int getChat_id() {
		return chat_id;
	}
	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public Timestamp getSent_at() {
		return sent_at;
	}
	public void setSent_at(Timestamp sent_at) {
		this.sent_at = sent_at;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}
}