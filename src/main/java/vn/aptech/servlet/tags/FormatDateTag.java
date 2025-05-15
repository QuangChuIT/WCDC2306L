package vn.aptech.servlet.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateTag extends SimpleTagSupport {
    private Date dateValue;

    private String pattern;

    private final static String DEFAULT_PATTERN = "dd/MM/yyyy";

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (pattern == null) {
            pattern = DEFAULT_PATTERN;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        getJspContext().getOut().print(formatter.format(dateValue));
    }
}
