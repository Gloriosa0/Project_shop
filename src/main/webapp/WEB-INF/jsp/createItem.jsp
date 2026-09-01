<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<div class="form">
    <form action="${pageContext.request.contextPath}/items/admin/create-item" method="POST">
        <label>Название товара</label><input type="text" name="itemName"><br>
        <label>Цена</label><input type="number" name="price" step="0.01"><br>
        <label>В наличии</label>
        <input type="hidden" name="inStock" value="false">
        <input type="checkbox" name="inStock" value="true"><br>
        <label>Продавец</label><input type="text" name="seller"><br>
        <label>Описание</label><input type="text" name="description"><br>
        <label>Дата доставки</label><input type="date" name="arrivalDate"><br>
        <input type="submit" value="Создать товар">
    </form>
</div>
</body>
</html>