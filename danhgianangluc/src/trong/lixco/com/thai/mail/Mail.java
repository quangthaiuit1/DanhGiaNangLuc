package trong.lixco.com.thai.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.jpa.entity.KyDanhGia;

@Named
@ViewScoped
public class Mail extends AbstractBean<KyDanhGia> {

	private static final long serialVersionUID = 1L;

//DANH GIA NANG LUC TUYEN DUNG
	// send 1
	public static boolean processSendMailAfterAddEmployee(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh,
			Account account) {
//			notify = new Notify(FacesContext.getCurrentInstance());

		try {

			// config mail server
			Properties props = System.getProperties();
//				props.put("mail.smtp.host", "mail.lixco.com");
//				props.put("mail.smtp.port", "25");

			// test
			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");
			// end test

			// props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			// props.put("mail.smtp.host", "smtp.gmail.com");
			// props.put("mail.smtp.port", "587");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			// Message msg = new MimeMessage(session);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Thân chào " + "<span style='font-weight: bold'>"
					+ tenNhanVien + "</span>" + "</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Vị trí "
					+ "<span style='font-weight: bold'>" + tenChucDanh + "</span>"
					+ " của bạn đã hết thời gian thử việc. Vì vậy, bạn vui lòng truy cập vào phần mềm và đăng nhập theo đường link và acount bên dưới để thực hiện việc đánh giá năng lực. Chi tiết cách thức đánh giá bạn vui lòng xem file đính kèm. </p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực bạn vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;'>Kính nhờ trưởng đơn vị hỗ trợ hướng dẫn nhân viên phòng mình thực hiện đánh giá.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>"
					+ "<p><b style='font-size: 20px;'>Link: <a href=\"http://erp.lixco.com/index.htm\">http://erp.lixco.com/index.htm</a></b></p>"
					+ "<p style='font-size: 20px;'>Username: " + account.getUserName() + "</p>"
					+ "<p style='font-size: 20px;'>Password: " + account.getPassword() + "</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();

			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA NANG LUC TUYEN DUNG
	// send mail 2 quan ly
	public static boolean processSendMailAfterSuccessEvaluate(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh) {

		try {
			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi anh/chị Trưởng đơn vị.</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Bạn "
					+ "<span style='font-weight: bold'>" + tenNhanVien + "</span>"
					+ " đã hoàn thành tự đánh giá năng lực sau thử việc ở vị trí " + "<span style='font-weight: bold'>"
					+ tenChucDanh + "</span>"
					+ ". Vì vậy, anh/chị vui lòng truy cập vào phần mềm để thực hiện đánh giá năng lực của bạn "
					+ tenNhanVien + " ở cấp quản lý.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực anh/chị vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;font-style:italic;font-weight: bold;'>Lưu ý: Song song với việc đánh giá năng lực đơn vị vui lòng hoàn thành bản đánh giá chương trình thử việc và gửi lại phòng nhân sự. Các đơn vị cũng triển khai cho nhân viên thực hiện đăng ký KPI trên phần mềm.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();
			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA NANG LUC TUYEN DUNG
	// gui hoi dong
	public static boolean processSendMailAfterManagerChecked(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh,
			String departmentName) {

		try {

			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi: Hội đồng đánh giá năng lực</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Trưởng đơn vị " + departmentName
					+ " đã hoàn thành việc đánh giá năng lực sau thử việc của bạn " + tenNhanVien + " ở vị trí "
					+ tenChucDanh + ". Vì vậy, kính nhờ hội đồng thực hiện đánh giá năng lực của bạn " + tenNhanVien
					+ " ở cấp hội đồng đánh giá.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực hội đồng vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.!</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();

			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
				Transport.send(msg);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// DANH GIA NANG LUC TOAN CONG TY
	// gui ca nhan
	public static boolean processSendMailPersonalCompany(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh) {

		try {

			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Thân chào " + "<span style='font-weight: bold'>"
					+ tenNhanVien + "</span>" + "</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Hiện tại bạn đang có kỳ "
					+ kyDanhGia.getTenkydanhgia() + " ở vị trí " + "<span style='font-weight: bold'>" + tenChucDanh
					+ "</span>"
					+ " . Vì vậy, bạn vui lòng truy cập vào phần mềm đánh giá năng lực để thực hiện việc đánh giá. </p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực anh/chị vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();
			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA NANG LUC TOAN CONG TY
	// quan ly
	public static boolean processSendMailManagerCompany(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh) {

		try {

			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi anh/chị Trưởng đơn vị</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Bạn "
					+ "<span style='font-weight: bold'>" + tenNhanVien + "</span>"
					+ " đã hoàn thành tự đánh giá năng lực cho kỳ " + kyDanhGia.getTenkydanhgia()
					+ " ở vị trí " + "<span style='font-weight: bold'>" + tenChucDanh + "</span>"
					+ ". Vì vậy, anh/chị vui lòng truy cập vào phần mềm để thực hiện đánh giá năng lực của bạn "
					+ "<span style='font-weight: bold'>" + tenNhanVien + "</span>" + " ở cấp quản lý.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực anh/chị vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();
			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA NANG LUC TOAN CONG TY
	// gui hoi dong
	public static boolean processSendMailHoiDongCompany(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh,
			String departmentName) {

		try {

			// config mail server
			Properties props = System.getProperties();

			// test
			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");
			// end test

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi: Hội đồng đánh giá năng lực</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Trưởng đơn vị " + departmentName
					+ " đã hoàn thành việc đánh giá năng lực sau thử việc của bạn " + tenNhanVien + " ở vị trí "
					+ tenChucDanh + ". Vì vậy, kính nhờ hội đồng thực hiện đánh giá năng lực của bạn " + tenNhanVien
					+ " ở cấp hội đồng đánh giá.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực hội đồng vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.!</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();

			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
				Transport.send(msg);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA QUY HOACH CAN BO
	// 1. nhan vien
	public static boolean processSendMailQuyHoachCanBoNhanVien(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh) {

		try {

			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Thân chào " + "<span style='font-weight: bold'>"
					+ tenNhanVien + "</span>" + "</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Hiện tại bạn đang có bài "
					+ kyDanhGia.getTenkydanhgia() + " ở vị trí " + "<span style='font-weight: bold'>" + tenChucDanh
					+ "</span>"
					+ " . Vì vậy, bạn vui lòng truy cập vào phần mềm đánh giá năng lực để thực hiện việc đánh giá. </p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực anh/chị vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();
			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
//DANH GIA QUY HOACH CAN BO
	// 2. Quan ly
	public static boolean processSendMailQuyHoachCanBoQuanLy(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh) {

		try {

			// config mail server
			Properties props = System.getProperties();

			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi anh/chị Trưởng đơn vị</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: anh/chị "
					+ "<span style='font-weight: bold'>" + tenNhanVien + "</span>"
					+ " đã hoàn thành tự đánh giá năng lực cho kỳ " + kyDanhGia.getTenkydanhgia()
					+ " ở vị trí " + "<span style='font-weight: bold'>" + tenChucDanh + "</span>"
					+ ". Vì vậy, anh/chị vui lòng truy cập vào phần mềm để thực hiện đánh giá năng lực của anh/chị "
					+ "<span style='font-weight: bold'>" + tenNhanVien + "</span>" + " ở cấp quản lý.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực anh/chị vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();
			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
			}
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//DANH GIA QUY HOACH CAN BO
	// 3. Hoi dong	
	public static boolean processSendMailQuyHoachCanBoHoiDong(String mailSend, String passMailSend,
			List<String> mailDestinations, KyDanhGia kyDanhGia, String tenNhanVien, String tenChucDanh,
			String departmentName) {

		try {

			// config mail server
			Properties props = System.getProperties();

			// test
			props.put("mail.smtp.host", "mail.lixco.com");
			props.put("mail.smtp.port", "25");
			// end test

			Authenticator pa = null;
			if (mailSend != null && passMailSend != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailSend, passMailSend);
					}
				};
			}
			Session session = Session.getInstance(props, pa);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailSend));

			msg.setSubject("[Mail tự động] THÔNG BÁO THỰC HIỆN " + kyDanhGia.getTenkydanhgia(), "UTF-8");
			String text = "";

			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(text, "utf-8");

			String htmlText = "<p style='font-size: 20px;'>Kính gửi: Hội đồng đánh giá năng lực</p>"
					+ "<p style='font-size: 20px;'>Phòng Nhân sự xin thông báo: Trưởng đơn vị " + departmentName
					+ " đã hoàn thành việc đánh giá năng lực sau thử việc của anh/chị " + tenNhanVien + " ở vị trí "
					+ tenChucDanh + ". Vì vậy, kính nhờ hội đồng thực hiện đánh giá năng lực của anh/chị " + tenNhanVien
					+ " ở cấp hội đồng đánh giá.</p>"
					+ "<p style='font-size: 20px;'>Nhằm đảm bảo thực hiện đúng theo tiến độ đánh giá năng lực hội đồng vui lòng hoàn thành việc đánh giá trong vòng <span style='font-weight: bold'> 2 ngày </span> kể từ ngày nhận được email này.!</p>"
					+ "<p style='font-size: 20px;'>Trân trọng cảm ơn!</p>";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlText, "text/html; charset=utf-8");

			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();

			for (int i = 0; i < mailDestinations.size(); i++) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinations.get(i), false));
				Transport.send(msg);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	protected void initItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
}
