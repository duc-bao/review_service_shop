package com.ducbao.service_be.model.constant;

public class AppConstants {
    public static final String SUBJECT_REGISTER = "Bạn vui lòng nhấn xác nhận ở bên dưới để tiếp tục đăng nhập vào hệ thống";
    public static final String CONTENT_REGISTER = "<div style=\"font-size: 16px; letter-spacing: normal;\">Chào [[name]]," +
            "</div><div style=\"font-size: 16px; letter-spacing: normal;\"><i><br></i></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\">" +
            "<i>Vui lòng nhấn vào link bên dưới để xác nhận việc đăng kí của bạn</i></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\"><i><br></i></div>" +
            "<a href=\"[[URL]]\" target=\"_self\" style=\"color: rgb(0, 123, 255); background-color: " +
            "transparent; font-size: 16px; letter-spacing: normal;\">VERIFY</a><div style=\"font-size: 16px;" +
            " letter-spacing: normal;\"><span style=\"font-size: 18px;\"><span style=\"font-size: 24px;\">" +
            "<span style=\"font-weight: bolder;\"><font color=\"#ff0000\"></font></span></span>" +
            "</span></div><div style=\"font-size: 16px; letter-spacing: normal;\"><br></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\">Thanks,</div>";
    public static final String LINK_ACTIVE_ACCOUNT = "http://localhost:5173/auth/verify-account?";
    public static final String CONTENT_SHOP_ACTIVATION = "Hello, [[name]]! cửa hàng của bạn đã được admin phê duyệt bạn có thể truy cập ở đây" +  "<a href=\\\"[[URL]]\\\" target=\\\"_self\\\" style=\\\"color: rgb(0, 123, 255); background-color: \" +\n" +
            "            \"transparent; font-size: 16px; letter-spacing: normal;\\\">Di chuyển</a>.";
    public static final String LINK_SHOP = "http://localhost:5173/auth/shop";
    public static final String FORGOT = "Chào bạn, chúng tôi xin gửi đến bạn mật khẩu mới tạm thời";
    public static final String FORGOT_PASSWORD = "Chào bạn [[name]], Mật khẩu tạm thời mới của bạn sẽ là" + "<b> [[URL]] </b>" + ".Bạn vui lòng đăng nhập bằng mật khẩu này và vui lòng đổi lại mật khẩu";
}
