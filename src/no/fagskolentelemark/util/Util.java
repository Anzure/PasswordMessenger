package no.fagskolentelemark.util;

public class Util {

	public static int parsePhone(String phone) {
		phone = phone.replace(" ", "");
		phone = phone.substring(phone.length() - 8);
		int telefon = Integer.parseInt(phone);
		return telefon;
	}
}
