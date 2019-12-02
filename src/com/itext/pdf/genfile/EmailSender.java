/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.itext.pdf.model.EmailPlaceholder;

/**
 *
 * @author emmanuel.idoko
 */
public class EmailSender {

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;
    //@Autowired
    Configuration freemarkerConfig = new Configuration();
    private static final String SMTP_HOST_O365 = "smtp.office365.com";
    private static final int SMTP_PORT_O365 = 587;
    private static final String SMTP_USERNAME_O365 = "ITsupport@myeasycoop.com";
    private static final String SMTP_PASSWORD_O365 = "Renegade@123456";
    
//    private static final String SMTP_USERNAME_O365 = "shareportalsupport@africaprudential.com";
//    private static final String SMTP_PASSWORD_O365 = "PORTALaDm1n##";

    /**
     * Sends email to multiple email addresses
     *
     * @param fromUser the sender gmail username without @gmail.com (e.g
     * smith2345)
     * @param fromUserEmailPassword the password of the fromUser
     * @param to_emails the emails to send message to
     * @param subject subject of the email
     * @param emailBody the body of the mail
     * @param template html template to be sent
     * @throws AddressException
     * @throws MessagingException
     */
    public void sendBulkEmail(String fromUser, String fromUserEmailPassword, List<String> to_emails, String subject, String emailBody, String template) throws AddressException, MessagingException {

        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < to_emails.size(); i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to_emails.get(i)));
        }

        MessageFormat mf = new MessageFormat(template);
        String message_html = mf.format(new Object[]{emailBody});

        emailMessage.setSubject(subject);
        emailMessage.setContent(message_html, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email
        String emailHost = "smtp.gmail.com";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }

    /**
     * Sends email to a single email address
     *
     * @param fromUser the sender gmail username without @gmail.com (e.g
     * smith2345)
     * @param fromUserEmailPassword the password of the fromUser
     * @param to_email the email to send message to
     * @param subject subject of the email
     * @param placeholder
     * @throws AddressException
     */
    public void sendSingleEmail(String fromUser, String fromUserEmailPassword, String to_email, String subject,
            EmailPlaceholder placeholder) {
        try {
            String emailPort = "587";//gmail's smtp port
            // Using a subfolder such as /templates here
            freemarkerConfig.setClassForTemplateLoading(EmailSender.class, "/");

            //helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
            //URLDataSource source = new URLDataSource(this.getClass().getResource("/templates/logo.png"));
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", placeholder.getFirst_name());
            model.put("lastName", placeholder.getLast_name());
            model.put("location", placeholder.getCoop_name());
            model.put("comp_name", placeholder.getCoop_name());
            model.put("member_username", placeholder.getUsername());
            model.put("password", placeholder.getPassword());

            Template t = freemarkerConfig.getTemplate("create-member.ftl");
            StringWriter writer = new StringWriter();
            t.process(model, writer);
            //String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            emailProperties = System.getProperties();
            emailProperties.put("mail.smtp.port", emailPort);
            emailProperties.put("mail.smtp.auth", "true");
            emailProperties.put("mail.smtp.starttls.enable", "true");

            mailSession = Session.getDefaultInstance(emailProperties, null);
            emailMessage = new MimeMessage(mailSession);

            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));

//        MessageFormat mf = new MessageFormat(template);
//        String message_html = mf.format(new Object[]{emailBody});
            emailMessage.setSubject(subject);
            emailMessage.setContent(writer.toString(), "text/html");//for a html email
            //emailMessage.setText(emailBody);// for a text email
            String emailHost = "smtp.gmail.com";
            //String emailHost = "smtp.office365.com";

            Transport transport = mailSession.getTransport("smtp");

            transport.connect(emailHost, fromUser, fromUserEmailPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            System.out.println("Email sent successfully.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

    }

    /**
     * Sends email to a single email address
     *
     * @param to_email the email to send message to
     * @param subject subject of the email
     * @param placeholder
     * @throws java.io.IOException
     * @throws freemarker.template.TemplateException
     */
    public void sendSingleEmailOfiice365(String to_email, String subject,
            EmailPlaceholder placeholder) throws IOException, TemplateException {
        final Session session = Session.getInstance(this.getEmailProperties(), new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME_O365, SMTP_PASSWORD_O365);
            }

        });

        try {
            freemarkerConfig.setClassForTemplateLoading(EmailSender.class, "/");

            //helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
            //URLDataSource source = new URLDataSource(this.getClass().getResource("/templates/logo.png"));
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", placeholder.getFirst_name());
            model.put("lastName", placeholder.getLast_name());
            model.put("location", placeholder.getCoop_name());
            model.put("comp_name", placeholder.getCoop_name());
            model.put("member_username", placeholder.getUsername());
            model.put("password", placeholder.getPassword());

            Template t = freemarkerConfig.getTemplate("create-member.ftl");
            StringWriter writer = new StringWriter();
            t.process(model, writer);

            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
            message.setFrom(new InternetAddress(SMTP_USERNAME_O365));
            message.setSubject(subject);
            message.setContent(writer.toString(), "text/html");
            message.setSentDate(new Date());
            Transport.send(message);
            System.out.println("Email sent successfully to - " + to_email);
        } catch (final MessagingException ex) {
            System.out.println("Error occurred:::" + ex);
        }

    }

    public Properties getEmailProperties() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SMTP_HOST_O365);
        config.put("mail.smtp.port", SMTP_PORT_O365);
        return config;
    }
}
