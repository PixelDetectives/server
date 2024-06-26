package site.pixeldetective.server.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * create table chat (
    chat_id INT AUTO_INCREMENT PRIMARY KEY,
    message TEXT NOT NULL,
    sender INT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Foreign Key (sender) REFERENCES users(u_num)
);
 * @author WD
 *
 */
public class ChatDTO {

	private int chat_id;
	private String message;
	private int sender;
	private Date sent_at;
	
	@Override
	public String toString() {
		return "ChatDTO [chat_id=" + chat_id + ", message=" + message + ", sender=" + sender + ", sent_at=" + sent_at
				+ "]";
	}

	public ChatDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChatDTO(int chat_id, String message, int sender, Date sent_at) {
		super();
		this.chat_id = chat_id;
		this.message = message;
		this.sender = sender;
		this.sent_at = sent_at;
	}
	
	public ChatDTO(String message, int sender, Date sent_at) {
		super();
		this.chat_id = chat_id;
		this.message = message;
		this.sender = sender;
		this.sent_at = sent_at;
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
	public Date getSent_at() {
		return sent_at;
	}
	public void setSent_at(Date sent_at) {
		this.sent_at = sent_at;
	}
}
