package vn.aptech.servlet.student;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

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
            List<Student> students = studentService.getAllStudents();
            request.setAttribute("students", students);
            request.getRequestDispatcher("/students.jsp").forward(request, response);
        } else if (action.equals("create")) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            int bornYear = Integer.parseInt(request.getParameter("bornYear"));
            if (studentService.existByEmail(email)) {
                request.setAttribute("error", "This email already exists");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("bornYear", bornYear);
                List<Student> students = studentService.getAllStudents();
                request.setAttribute("students", students);
                request.getRequestDispatcher("/students.jsp").forward(request, response);
            } else {
                Student student = new Student();
                student.setName(name);
                student.setEmail(email);
                student.setBornYear(bornYear);
                studentService.addStudent(student);
                response.sendRedirect("/students");
            }
        } else if (action.equals("delete")) {
            Long id = Long.parseLong(request.getParameter("id"));
            studentService.deleteStudent(id);
            response.sendRedirect("/students");
        }
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
