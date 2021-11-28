package core2.chapter07.homework;

import com.alibaba.druid.pool.DruidDataSource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

class MessageItem {
    public MessageItem(int id, String from, String to, String subject, String content) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public void printMessage() {
        System.out.println(id + ", " + from + ", " + to + ", " + subject + ", " + content);
    }

    int id;
    String from;
    String to;
    String subject;
    String content;
}

class DruidFactory {
    private static DruidDataSource dataSource = null;

    public static void init() throws Exception {

        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setUrl("jdbc:mysql://localhost:33066/email");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        dataSource.setFilters("stat");
    }

    public static Connection getConnection() throws Exception {
        if (null == dataSource) {
            init();
        }
        return dataSource.getConnection();
    }
}

class MailStorage {
    public static void main(String[] args) {
        queryMessage(1);
    }

    public static MessageItem queryMessage(int id) {
        Connection connection = null;
        try {
            connection = DruidFactory.getConnection();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select `id`, `from`, `to`, `subject`, `content` from t_mail where id = " + id);

            rs.next();
            MessageItem item = new MessageItem(rs.getInt(1), rs.getString("from"),
                    rs.getString("to"), rs.getString("subject"), rs.getString("content"));

            item.printMessage();

            rs.close();
            stmt.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

class TextMessage {
    public static Message generate(Session session, MessageItem item) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(item.from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(item.to));
        message.setSentDate(new Date());
        message.setSubject(item.subject);
        message.setText(item.content);
        message.saveChanges();
        return message;
    }
}

public class MailClientSender {
    private Session session;
    private Transport transport;
    private final String username = "talentwill";
    private final String password = "Get from web";
    private final String smtpServer = "smtp.163.com";

    public Message createTextMessage() throws Exception {
        MessageItem message = MailStorage.queryMessage(1);
        return TextMessage.generate(session, message);
    }

    public void init() throws Exception {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
        properties.put("mail.smtp.host", smtpServer);
        properties.put("mail.smtp.auth", true);

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);
        transport = session.getTransport();
    }

    public void sendMessage(Message message) throws MessagingException {
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        System.out.println("Send mail success.");
    }

    public void close() throws MessagingException {
        transport.close();
    }

    public static void main(String[] args) throws Exception {
        MailClientSender sender = new MailClientSender();
        sender.init();
        sender.sendMessage(sender.createTextMessage());
        sender.close();
    }
}
