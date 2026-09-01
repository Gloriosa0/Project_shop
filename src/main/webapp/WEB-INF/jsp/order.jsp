<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<h1> Заказ №${orderPageDto.orderId} </h1>
<div class="order-info">
    <h2>Информация о заказе</h2>
    <p><strong>Пользователь:</strong>${orderPageDto.user.username}</p>
    <p><strong>Имя:</strong>${orderPageDto.user.name}</p>
    <p><strong>Фамилия:</strong>${orderPageDto.user.surname}</p>
    <p><strong>Телефон:</strong>${orderPageDto.user.phoneNum}</p>
    <p><strong>Стоимость заказа:</strong>${orderPageDto.totalPrice}</p>
    <p><strong>Дата оплаты:</strong>
        <c:choose>
            <c:when test="${not empty orderPageDto.payDate}">
                ${orderPageDto.payDate}
            </c:when>
            <c:otherwise>
                Заказ не оплачен
            </c:otherwise>
        </c:choose>
    </p>
    <p><strong>Комментарий:</strong>
        <c:choose>
            <c:when test="${not empty orderPageDto.comment}">
                ${orderPageDto.comment}
            </c:when>
            <c:otherwise>
                —
            </c:otherwise>
        </c:choose>
    </p>
    <p><strong>Текущий статус:</strong>
        <c:choose>
            <c:when test="${not empty orderPageDto.status}">
                ${orderPageDto.status}
            </c:when>
            <c:otherwise>
                Статус отсутствует
            </c:otherwise>
        </c:choose>
    </p>
</div>
<div>
    <h2>Товары в заказе</h2>
    <table>
        <tr>
            <th>ID товара</th>
            <th>Название</th>
            <th>Цена</th>
            <th>Количество</th>
            <th>Стоимость</th>
        </tr>
        <c:forEach var="orderItem" items="${orderPageDto.items}">
            <tr>
                <td>${orderItem.item.itemId}</td>
                <td>${orderItem.item.itemName}</td>
                <td>${orderItem.pricePerItem}</td>
                <td>${orderItem.amount}</td>
                <td>${orderItem.pricePerItem * orderItem.amount}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="actions">
    <h2>Действия</h2>
    <form method="post" action="${pageContext.request.contextPath}/orders/${orderPageDto.orderId}/pay">
        <button type="submit">Оплатить заказ</button>
    </form>
    <form method="post" action="${pageContext.request.contextPath}/orders/${orderPageDto.orderId}/cancel">
        <button type="submit">Отменить заказ</button>
    </form>
</div>
<div class="admin-block">
    <sec:authorize access="hasRole('ROLE_ADMIN')">
    <h2>Администрирование заказа</h2>
    <h3>Изменить статус</h3>
    <form method="post"
          action="${pageContext.request.contextPath}/orders/admin/${orderPageDto.orderId}/status">
        <select name="status">
            <option value="STATUS_CREATED">STATUS_CREATED</option>
            <option value="STATUS_FORMING">STATUS_FORMING</option>
            <option value="STATUS_SENT">STATUS_SENT</option>
            <option value="STATUS_READY">STATUS_READY</option>
            <option value="STATUS_DELIVERED_PAID">STATUS_DELIVERED_PAID</option>
            <option value="STATUS_REJECTED_UNPAID">STATUS_REJECTED_UNPAID</option>
            <option value="STATUS_RETURNED">STATUS_RETURNED</option>
            <option value="STATUS_CANCELED">STATUS_CANCELED</option>
        </select>
        <button type="submit">Изменить статус</button>
    </form>
    <h3>Изменить комментарий</h3>
    <form method="post"
          action="${pageContext.request.contextPath}/orders/admin/${orderPageDto.orderId}/comment">
        <textarea name="comment" rows="5" cols="50">${orderPageDto.comment}</textarea>
        <br>
        <button type="submit">Сохранить комментарий</button>
    </form>
    </sec:authorize>
</div>
<br>
<a href="${pageContext.request.contextPath}/orders"> Вернуться к заказам </a>
</body>
</html>