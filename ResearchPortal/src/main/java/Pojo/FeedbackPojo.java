package Pojo;

public class FeedbackPojo {
	private int rid;
	private String token, rating;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public FeedbackPojo(int rid, String token, String rating) {
		super();
		this.rid = rid;
		this.token = token;
		this.rating = rating;
	}
	
	
	
	
	
	

}
