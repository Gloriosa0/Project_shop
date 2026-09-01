<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<h1><c:out value="${itemPageDto.itemName}"/></h1>
<p><b>Цена:</b><c:out value="${itemPageDto.price}"/></p>
<p><b>Продавец:</b><c:out value="${itemPageDto.seller}"/></p>
<p><b>Описание:</b><c:out value="${itemPageDto.description}"/></p>
<p><b>Дата доставки курьером:</b>
    <c:choose>
        <c:when test="${itemPageDto.arrivalDate != null}">
            <c:out value="${itemPageDto.arrivalDate}"/>
        </c:when>
        <c:otherwise>
            Товар не доступен к доставке курьером
        </c:otherwise>
    </c:choose>
</p>
<p><b>Наличие:</b>
    <c:choose>
        <c:when test="${itemPageDto.inStock}">
            В наличии
        </c:when>
        <c:otherwise>
            Нет в наличии
        </c:otherwise>
    </c:choose>
</p>
<c:if test="${itemPageDto.inStock}">
    <form method="post" action="${pageContext.request.contextPath}/cart/add">
        <input type="hidden" name="itemId" value="${itemPageDto.itemId}">
        <label>Количество:</label>
        <input type="number" name="amount" value="1" min="1">
        <button type="submit">Добавить в корзину</button>
    </form>
</c:if>
<br>
<a href="${pageContext.request.contextPath}/items">Вернуться в каталог</a>
</body>
</html>