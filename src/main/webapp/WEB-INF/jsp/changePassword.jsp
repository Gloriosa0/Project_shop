<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Магазин</title>
</head>
<body>
<div class="form-container">
    <h1>Изменение пароля</h1>
    <form method="post" action="${pageContext.request.contextPath}/profile/password">
        <div class="form-group">
            <label for="oldPassword">Старый пароль:</label>
            <input type="password" id="oldPassword" name="oldPassword" required>
        </div>
        <div class="form-group">
            <label for="newPassword">Новый пароль:</label>
            <input type="password" id="newPassword" name="newPassword" required>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Подтверждение нового пароля:</label>
            <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
        </div>
        <button type="submit">Изменить пароль</button>
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/profile">Отмена</a>
</div>
</body>
</html>