package trong.lixco.com.thai.mail;

import java.util.List;

import trong.lixco.com.account.servicepublics.Account;

public class MailDestinationEntity {
	private String destinationTo;
	private List<String> destinationCC;
	private Account account;
	
	public String getDestinationTo() {
		return destinationTo;
	}
	public void setDestinationTo(String destinationTo) {
		this.destinationTo = destinationTo;
	}
	public List<String> getDestinationCC() {
		return destinationCC;
	}
	public void setDestinationCC(List<String> destinationCC) {
		this.destinationCC = destinationCC;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
