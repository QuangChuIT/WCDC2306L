<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Student Management</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/asset/css/style.css"/>"/>
</head>
<body class="main">
<div class="container">
    <jsp:include page="layout/header.jsp"/>
    <div class="row text-center">
        <div class="col-md-12 col-xs-12 col-sm-12">
            <h1>Student Management</h1>
            <table class="table table-bordered table-striped">
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Age</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="student" items="${requestScope.students}">
                    <tr>
                        <td>${student.getId()}</td>
                        <td>${student.getName()}</td>
                        <td>${student.getEmail()}</td>
                        <td>${student.getAge()}</td>
                        <td>
                            <a href="<c:url value="/students?action=delete&id=${student.getId()}"/>"
                               class="btn btn-danger">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 col-xs-12 col-sm-12">
            <div class="d-flex justify-content-center">
                <div class="text-danger">
                    <c:if test="${error != null}">
                        ${error}
                    </c:if>
                </div>
            </div>
            <div class="d-flex justify-content-center">
                <form action="<c:url value="/students?action=create"/>" method="post" class="student-form">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Enter name"
                               value="${name}" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email address</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="Enter email"
                               value="${email}" } required>
                    </div>
                    <div class="mb-3">
                        <label for="bornYear" class="form-label">Born Year</label>
                        <input type="number" class="form-control" id="bornYear" name="bornYear"
                               placeholder="Enter born year" value="${bornYear}" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
</body>
</html>
