package site.pixeldetective.server.dto;

public class UserDTO {

	private int uNum;
	private String uId;
	private String uName;
	private String uPw;

	@Override
	public String toString() {
		return "UserDTO{" +
				"uNum=" + uNum +
				", uId='" + uId + '\'' +
				", uName='" + uName + '\'' +
				", uPw='" + uPw + '\'' +
				'}';
	}

	public UserDTO(int uNum, String uId, String uName, String uPw) {
		this.uNum = uNum;
		this.uId = uId;
		this.uName = uName;
		this.uPw = uPw;
	}

	public UserDTO(String uId, String uName, String uPw) {
		this.uId = uId;
		this.uName = uName;
		this.uPw = uPw;
	}

	public UserDTO() {
	}

	public int getuNum() {
		return uNum;
	}

	public void setuNum(int uNum) {
		this.uNum = uNum;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuPw() {
		return uPw;
	}

	public void setuPw(String uPw) {
		this.uPw = uPw;
	}
}
