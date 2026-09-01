<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<h1>Корзина</h1>
<c:choose>
    <c:when test="${empty cart.items}">
        <p>Корзина пуста.</p>
        <a href="${pageContext.request.contextPath}/items">Вернуться в каталог</a>
    </c:when>
    <c:otherwise>
        <table border="1">
            <tr>
                <th>Товар</th>
                <th>Цена</th>
                <th>Количество</th>
                <th>Стоимость</th>
                <th>Действие</th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
                <tr>
                    <td>${item.itemName}</td>
                    <td>${item.pricePerItem}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cart/change">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                            <input type="number" name="amount" value="${item.amount}" min="1">
                            <button type="submit">Изменить</button>
                        </form>
                    </td>
                    <td>${item.totalPrice}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cart/remove">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                            <button type="submit">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <h2>Итого: ${cart.totalPrice}</h2>
        <p>Количество товаров:${cart.totalAmount}</p>
        <form method="post" action="${pageContext.request.contextPath}/cart/clear">
            <button type="submit">Очистить корзину</button>
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/items">Продолжить покупки</a>
        <br>
        <form method="post" action="${pageContext.request.contextPath}/cart/checkout">
            <button type="submit">Оформить заказ</button>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>