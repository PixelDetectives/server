package site.pixeldetective.server.dto;

public class AnswerDTO {
	private int a_num;
	private int g_num;
	private int a_radius;
	private int a_x;
	private int a_y;
	
	@Override
	public String toString() {
		return "AnswerDTO [a_num=" + a_num + ", g_num=" + g_num + ", a_radius=" + a_radius + ", a_x=" + a_x + ", a_y="
				+ a_y + "]";
	}

	public AnswerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnswerDTO(int a_num, int g_num, int a_radius, int a_x, int a_y) {
		super();
		this.a_num = a_num;
		this.g_num = g_num;
		this.a_radius = a_radius;
		this.a_x = a_x;
		this.a_y = a_y;
	}
	
	public int getA_num() {
		return a_num;
	}
	public void setA_num(int a_num) {
		this.a_num = a_num;
	}
	public int getG_num() {
		return g_num;
	}
	public void setG_num(int g_num) {
		this.g_num = g_num;
	}
	public int getA_radius() {
		return a_radius;
	}
	public void setA_radius(int a_radius) {
		this.a_radius = a_radius;
	}
	public int getA_x() {
		return a_x;
	}
	public void setA_x(int a_x) {
		this.a_x = a_x;
	}
	public int getA_y() {
		return a_y;
	}
	public void setA_y(int a_y) {
		this.a_y = a_y;
	}
}