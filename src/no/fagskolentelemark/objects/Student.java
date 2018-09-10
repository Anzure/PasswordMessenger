package no.fagskolentelemark.objects;

public class Student {

	private String group;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private int phone;
	private String schoolEmail;
	private String personalEmail;

	public Student(String group, String firstName, String lastName, String username, String password, int phone, String schoolEmail, String personalEmail) {
		this.group = group;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.schoolEmail = schoolEmail;
		this.personalEmail = personalEmail;
	}

	public String getGroup() {
		return group;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getPhoneNumber() {
		return phone;
	}

	public String getSchoolEmail() {
		return schoolEmail;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phone = phoneNumber;
	}

	public void setPersonalEmail(String email) {
		this.personalEmail = email;
	}
}