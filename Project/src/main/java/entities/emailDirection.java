package entities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class emailDirection {

    final static String sendingEmail = "teamdiamonddobharchu@gmail.com";
    final static String sendingPassword = "diamondchu";

    public static void sendEmail(String message, String receivingEmail) {

        System.out.println("Sending email");

        //Setting up configurations for the email connection to the Google SMTP server using TLS
        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        //Establishing a session with required user details
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendingEmail, sendingPassword);
            }
        });
        try {
            //Creating a Message object to set the email content
            MimeMessage msg = new MimeMessage(session);

            //Storing the comma seperated values to email addresses
            String to = receivingEmail;

            /*Parsing the String with defualt delimiter as a comma by marking the boolean as true and storing the email
            addresses in an array of InternetAddress objects*/
            InternetAddress[] address = InternetAddress.parse(to, true);

            //Setting the recepients from the address variable
            msg.setRecipients(Message.RecipientType.TO, address);

            String timeStamp = new SimpleDateFormat("yyyy/mm/dd_hh:mm:ss").format(new Date());
            msg.setSubject("Sample Mail : " + timeStamp);
            msg.setSentDate(new Date());
            msg.setText(message);
            msg.setHeader("XPriority", "1");

            Transport.send(msg);

            System.out.println("Mail has been sent successfully");

        } catch (MessagingException mex) {
            System.out.println("Unable to send an email" + mex);
        }
    }

}
