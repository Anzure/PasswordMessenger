package no.fagskolentelemark.util;

import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import no.fagskolentelemark.GitIgnored;

public class SMS {

	public static boolean sendSMS(int phone, String txt) {
		try {
			URL url = new URL("https://gatewayapi.com/rest/mtsms");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(
					"token=" + GitIgnored.sms_token
							+ "&sender=" + URLEncoder.encode("Fagskolen", "UTF-8")
							+ "&message=" + URLEncoder.encode(txt, "UTF-8")
							+ "&class=premium&priority=VERY_URGENT&recipients.0.msisdn=0047" + phone
					);
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("Respons: " + responseCode + " for +47 " + phone);

			if (responseCode == 200) return true;
			else return false;

		} catch (Exception ex) {
			return false;
		}
	}

}
