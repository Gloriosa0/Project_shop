<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<div class="error-container">
    <div class="status">
        <c:out value="${status}"/>
    </div>
    <div class="error">
        <c:out value="${error}"/>
    </div>
    <div class="message">
        <c:out value="${message}"/>
    </div>
    <a class="back" href="${pageContext.request.contextPath}/items">
        Вернуться в магазин
    </a>
</div>
</body>
</html>