<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<h1>Профиль пользователя</h1>
<div class="profile">
    <div class="field">
        <strong>ID:</strong>${userPageDto.id}
    </div>
    <div class="field">
        <strong>Имя:</strong>${userPageDto.name}
    </div>
    <div class="field">
        <strong>Фамилия:</strong>${userPageDto.surname}
    </div>
    <div class="field">
        <strong>Телефон:</strong>${userPageDto.phoneNum}
    </div>
    <div class="field">
        <strong>Username:</strong>${userPageDto.username}
    </div>
    <div class="field">
        <strong>Баланс:</strong>${userPageDto.balance}
    </div>
</div>
<c:if test="${isOwnProfile}">
    <div class="actions">
        <h2>Действия</h2>
        <p><a href="${pageContext.request.contextPath}/profile/change">Изменить данные профиля</a></p>
        <p><a href="${pageContext.request.contextPath}/profile/password">Изменить пароль</a></p>
        <h3>Пополнить баланс</h3>
        <form method="post" action="${pageContext.request.contextPath}/profile/add-money">
            <input type="number" name="money" min="0.01" step="0.01" placeholder="Сумма" required>
            <button type="submit">Пополнить</button>
        </form>
        <h3>Удалить аккаунт</h3>
        <form method="post" action="${pageContext.request.contextPath}/profile/disable">
            <button type="submit">Удалить аккаунт</button>
        </form>
    </div>
</c:if>
<c:if test="${not isOwnProfile}">
    <p>
        <em>
            Это профиль другого пользователя.
            Действия владельца профиля недоступны.
        </em>
    </p>
</c:if>
<br>
<a href="${pageContext.request.contextPath}/items"> Вернуться к товарам </a>
</body>
</html>