package vn.aptech.servlet.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

public class CustomTag extends SimpleTagSupport {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (message != null) {
            getJspContext().getOut().write("Custom Message: " + message);
        } else {
            getJspContext().getOut().write("Default Custom Message");
        }
    }
}
