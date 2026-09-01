<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<div class="container">
    <div class="welcome">
        <h1>Магазин</h1>
        <p>Добро пожаловать!</p>
        <div class="buttons">
            <a сlass="button" href="<c:url value="/login"/>">Войти</a>
            <a class="button registration" href="<c:url value="/registration"/>">
                Зарегистрироваться
            </a>
        </div>
    </div>
</div>
</body>
</html>