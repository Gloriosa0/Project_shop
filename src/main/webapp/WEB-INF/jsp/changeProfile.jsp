<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Магазин</title>
</head>
<body>
<h1>Редактирование профиля</h1>
<form:form method="post" modelAttribute="userUpdateDto">
    <div>
        Имя:
        <form:input path="name"/>
        <form:errors path="name"/>
    </div>
    <div>
        Фамилия:
        <form:input path="surname"/>
        <form:errors path="surname"/>
    </div>
    <div>
        Username:
        <form:input path="username"/>
        <form:errors path="username"/>
    </div>
    <div>
        Телефон:
        <form:input path="phoneNum"/>
        <form:errors path="phoneNum"/>
    </div>
    <button type="submit">Сохранить</button>
</form:form>
</body>
</html>