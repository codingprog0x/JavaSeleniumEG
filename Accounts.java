
public class Accounts {
	private String pmc;
	private String acctName;
	private String acctNum;
	private String routeNum;
	
	private String nickname;
	private String note;
	private double amt;

	Accounts() {
		
		setPMC("Name");
		setAcctName("Acctname");
		setAcctNum("1000000");
		setRouteNum("000000001");
		setNick();
		setNote(" a note here");
		setAmt(9000.00);
	}
	
	private void setPMC(String s) {
		this.pmc = s;
	}
	
	private void setAcctName(String s) {
		this.acctName = s;
	}
	
	private void setAcctNum(String s) {
		this.acctNum = s;
	}
	
	private void setRouteNum(String s) {
		this.routeNum = s;
	}
	
	private void setNick() {
		this.nickname = pmc + "-" + acctName;
	}
	
	private void setNote(String s) {
		this.note = s;
	}
	
	private void setAmt(double amt) {
		this.amt = amt;
	}
	
	public String getPMC() {
		return this.pmc;
	}
	
	public String getAcctName() {
		return this.acctName;
	}
	
	public String getAcctNum() {
		return this.acctNum;
	}
	
	public String getRouteNum() {
		return this.routeNum;
	}

	public String getNick() {
		return this.nickname;
	}
	
	public String getNote() {
		return this.note;
	}
	
	public double getAmt() {
		return this.amt;
	}
}
