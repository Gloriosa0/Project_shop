<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Редактирование товара</title>
</head>
<body>
<h1>Редактирование товара</h1>
<form:form method="post" modelAttribute="itemPageDto"
           action="${pageContext.request.contextPath}/items/admin/${itemId}/edit-item">
    <div>
        <label>Название:</label>
        <form:input path="itemName"/>
        <form:errors path="itemName"/>
    </div>
    <br>
    <div>
        <label>Цена:</label>
        <form:input path="price" type="number" step="0.01" min="0"/>
        <form:errors path="price"/>
    </div>
    <br>
    <div>
        <label>В наличии:</label>
        <form:checkbox path="inStock"/>
        <form:errors path="inStock"/>
    </div>
    <br>
    <div>
        <label>Продавец:</label>
        <form:input path="seller"/>
        <form:errors path="seller"/>
    </div>
    <br>
    <div>
        <label>Описание:</label>
        <form:textarea path="description"/>
        <form:errors path="description"/>
    </div>
    <br>
    <div>
        <label>Дата поступления:</label>
        <form:input path="arrivalDate" type="date"/>
        <form:errors path="arrivalDate"/>
    </div>
    <br>
    <button type="submit">Сохранить</button>
</form:form>
<br>
<a href="${pageContext.request.contextPath}/items">Назад к товарам</a>
</body>
</html>