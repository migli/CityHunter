package lu.uni.cityhunter;

public class Mistery {
	
	private String question;
	private String answer;
	
	public Mistery(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
}
