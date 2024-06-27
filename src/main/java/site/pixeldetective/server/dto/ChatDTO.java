package site.pixeldetective.server.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class ChatDTO {

	private int chat_id;
	private String message;
	private int sender;
	private Timestamp sent_at;
	
	@Override
	public String toString() {
		return "ChatDTO [chat_id=" + chat_id + ", message=" + message + ", sender=" + sender + ", sent_at=" + sent_at
				+ "]";
	}
	
	public ChatDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatDTO(int chat_id, String message, int sender, Timestamp sent_at) {
		super();
		this.chat_id = chat_id;
		this.message = message;
		this.sender = sender;
		this.sent_at = sent_at;
	}
	
	public ChatDTO(String message, int sender, Timestamp sent_at) {
		super();
		this.message = message;
		this.sender = sender;
		this.sent_at = sent_at;
	}
	
	public ChatDTO(String message, int sender) {
		super();
		this.message = message;
		this.sender = sender;
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
}