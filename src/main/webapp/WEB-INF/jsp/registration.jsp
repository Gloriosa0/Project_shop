<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<h1>Регистрация</h1>
<form method="post" action="${pageContext.request.contextPath}/registration">
    <p><input type="text" name="username" placeholder="Логин" required></p>
    <p><input type="password" name="password" placeholder="Пароль" required></p>
    <p><input type="text" name="name" placeholder="Имя" required></p>
    <p><input type="text" name="surname" placeholder="Фамилия" required></p>
    <p><input type="text" name="phoneNum" placeholder="Телефон" required></p>
    <button type="submit">Зарегистрироваться</button>
</form>
<a href="${pageContext.request.contextPath}/login">Войти</a>
</body>
</html>