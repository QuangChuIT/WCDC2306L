package vn.aptech.servlet.student;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(name = "StudentServlet", urlPatterns = {"/students"})
public class StudentServlet extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        if (emf == null) {
            throw new ServletException("EntityManagerFactory not initialized.");
        }
        this.studentService = new StudentService(emf);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            int page = 1;
            int size = 3;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            List<Student> students = studentService.getAllStudents(page, size);
            long totalStudent = studentService.getTotalStudents();

            int totalPage = (int) Math.ceil((double) totalStudent / size);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPage);
            request.setAttribute("students", students);
            Cookie cookie = new Cookie("username", "administrator");
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            request.setAttribute("now", new Date());
            String language = request.getParameter("lang");
            String countryCode = "VI";
            if (language == null) {
                language = "vi";
                countryCode = "VN";
            } else if (language.equals("vi")) {
                language = "vi";
                countryCode = "VN";
            } else if (language.equals("en")) {
                language = "en";
                countryCode = "US";
            } else if (language.equals("fr")) {
                language = "fr";
                countryCode = "FR";
            }
            String lo = language + "_" + countryCode;
            request.setAttribute("locale", lo);
            Locale locale = new Locale(language, countryCode);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
            String welcome = resourceBundle.getString("welcome");
            request.setAttribute("welcome", welcome);

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
            request.setAttribute("currentDate", dateFormat.format(new Date()));

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            request.setAttribute("price", numberFormat.format(new BigDecimal("1000000")));

            request.getRequestDispatcher("/students.jsp").forward(request, response);
        } else if (action.equals("showFrmC")) {
            Student student = new Student();
            request.setAttribute("student", student);
            request.getRequestDispatcher("/create_student.jsp").forward(request, response);
        } else if (action.equals("create")) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            int bornYear = request.getParameter("bornYear").isEmpty() ? 0 : Integer.parseInt(request.getParameter("bornYear"));
            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setBornYear(bornYear);
            if (name == null || name.isEmpty()) {
                request.setAttribute("error", "Name is required.");
                sendError(request, response, student);
                return;
            }

            if (email == null || email.isEmpty()) {
                request.setAttribute("error", "Email is required.");
                sendError(request, response, student);
                return;
            }

            if (bornYear < 1970) {
                request.setAttribute("error", "Born year must be greater than 1970.");
                sendError(request, response, student);
                return;
            }

            if (studentService.existByEmail(email)) {
                request.setAttribute("error", "This email already exists");
                sendError(request, response, student);
            } else {
                studentService.addStudent(student);
                response.sendRedirect("/students");
            }
        } else if (action.equals("delete")) {
            Long id = Long.parseLong(request.getParameter("id"));
            studentService.deleteStudent(id);
            response.sendRedirect("/students");
        } else if (action.equals("showFrmU")) {
            Long id = Long.parseLong(request.getParameter("id"));

            Student student = studentService.getStudent(id);
            if (student == null) {
                response.sendRedirect("/students");
            } else {
                request.setAttribute("student", student);
                request.getRequestDispatcher("/update_student.jsp").forward(request, response);
            }
        } else if (action.equals("update")) {
            Long id = request.getParameter("id").isEmpty() ? 0L : Long.parseLong(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            int bornYear = request.getParameter("bornYear").isEmpty() ? 0 : Integer.parseInt(request.getParameter("bornYear"));
            Student student = new Student();
            student.setId(id);
            student.setName(name);
            student.setEmail(email);
            student.setBornYear(bornYear);
            if (name == null || name.isEmpty()) {
                request.setAttribute("error", "Name is required.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("/update_student.jsp").forward(request, response);
                return;
            }

            if (email == null || email.isEmpty()) {
                request.setAttribute("error", "Email is required.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("/update_student.jsp").forward(request, response);
                return;
            }

            if (bornYear < 1970) {
                request.setAttribute("error", "Born year must be greater than 1970.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("/update_student.jsp").forward(request, response);
                return;
            }
            Student studentDB = studentService.getStudent(id);
            if (studentDB == null) {
                student = new Student();
                student.setName(name);
                student.setEmail(email);
                student.setBornYear(bornYear);
                request.setAttribute("student", student);
                request.getRequestDispatcher("/update_student.jsp").forward(request, response);
            } else {
                String oldEmail = studentDB.getEmail();
                if (!oldEmail.equals(email)) {
                    if (studentService.existByEmail(email)) {
                        request.setAttribute("error", "This email already exists");
                        request.setAttribute("student", student);
                        request.getRequestDispatcher("/update_student.jsp").forward(request, response);
                        return;
                    }
                }
                studentDB.setEmail(email);
                studentDB.setName(name);
                studentDB.setBornYear(bornYear);
                studentService.updateStudent(studentDB);
                response.sendRedirect("/students");
            }
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, Student student) throws
            ServletException, IOException {
        request.setAttribute("student", student);
        request.getRequestDispatcher("/create_student.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
