package site.pixeldetective.server.dto;

public class GameDTO {
	private int g_num;
	private String g_image1;
	private String g_image2;
	private String g_name;
	private int g_difficulty;
	
	@Override
	public String toString() {
		return "gameDTO [g_num=" + g_num + ", g_image1=" + g_image1 + ", g_image2=" + g_image2 + ", g_name=" + g_name
				+ ", g_difficulty=" + g_difficulty + "]";
	}

	public GameDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameDTO(int g_num, String g_image1, String g_image2, String g_name, int g_difficulty) {
		super();
		this.g_num = g_num;
		this.g_image1 = g_image1;
		this.g_image2 = g_image2;
		this.g_name = g_name;
		this.g_difficulty = g_difficulty;
	}
	
	public int getG_num() {
		return g_num;
	}
	public void setG_num(int g_num) {
		this.g_num = g_num;
	}
	public String getG_image1() {
		return g_image1;
	}
	public void setG_image1(String g_image1) {
		this.g_image1 = g_image1;
	}
	public String getG_image2() {
		return g_image2;
	}
	public void setG_image2(String g_image2) {
		this.g_image2 = g_image2;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public int getG_difficulty() {
		return g_difficulty;
	}
	public void setG_difficulty(int g_difficulty) {
		this.g_difficulty = g_difficulty;
	}
}