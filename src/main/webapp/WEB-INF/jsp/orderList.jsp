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

<h1>Список заказов</h1>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <form method="get" action="${pageContext.request.contextPath}/orders/admin/search">
        <div>
            <label for="userId">ID пользователя: </label>
            <input type="number" id="userId" name="userId" min="1" value="${param.userId}">
        </div>
        <br>
        <div>
            <label for="status"> Статус: </label>
            <select id="status" name="status">
                <option value=""> Все статусы</option>
                <option value="STATUS_CREATED"
                        <c:if test="${param.status == 'STATUS_CREATED'}">
                            selected
                        </c:if>>
                    STATUS_CREATED
                </option>
                <option value="STATUS_FORMING"
                        <c:if test="${param.status == 'STATUS_FORMING'}">
                            selected
                        </c:if>>
                    STATUS_FORMING
                </option>
                <option value="STATUS_SENT"
                        <c:if test="${param.status == 'STATUS_SENT'}">
                            selected
                        </c:if>>
                    STATUS_SENT
                </option>
                <option value="STATUS_READY"
                        <c:if test="${param.status == 'STATUS_READY'}">
                            selected
                        </c:if>>
                    STATUS_READY
                </option>
                <option value="STATUS_DELIVERED_PAID"
                        <c:if test="${param.status == 'STATUS_DELIVERED_PAID'}">
                            selected
                        </c:if>>
                    STATUS_DELIVERED_PAID
                </option>
                <option value="STATUS_REJECTED_UNPAID"
                        <c:if test="${param.status == 'STATUS_REJECTED_UNPAID'}">
                            selected
                        </c:if>>
                    STATUS_REJECTED_UNPAID
                </option>
                <option value="STATUS_RETURNED"
                        <c:if test="${param.status == 'STATUS_RETURNED'}">
                            selected
                        </c:if>>
                    STATUS_RETURNED
                </option>
                <option value="STATUS_CANCELED"
                        <c:if test="${param.status == 'STATUS_CANCELED'}">
                            selected
                        </c:if>>
                    STATUS_CANCELED
                </option>
            </select>
        </div>
        <br>
        <div>
            <label for="from"> Дата от: </label>
            <input type="datetime-local" id="from" name="from" value="${param.from}">
        </div>
        <br>
        <div>
            <label for="to"> Дата до: </label>
            <input type="datetime-local" id="to" name="to" value="${param.to}">
        </div>
        <br>
        <button type="submit"> Найти</button>
    </form>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_USER')">
    <form method="get" action="${pageContext.request.contextPath}/orders/search">
        <div>
            <label for="userStatus">Статус:</label>
            <select id="userStatus" name="status">
                <option value="">
                    Все статусы
                </option>
                <option value="STATUS_CREATED"
                        <c:if test="${param.status == 'STATUS_CREATED'}">
                            selected
                        </c:if>>
                    STATUS_CREATED
                </option>
                <option value="STATUS_FORMING" <c:if test="${param.status == 'STATUS_FORMING'}"> selected </c:if>>
                    STATUS_FORMING
                </option>
                <option value="STATUS_SENT" <c:if test="${param.status == 'STATUS_SENT'}"> selected </c:if>>
                    STATUS_SENT
                </option>
                <option value="STATUS_READY" <c:if test="${param.status == 'STATUS_READY'}"> selected </c:if>>
                    STATUS_READY
                </option>
                <option value="STATUS_DELIVERED_PAID" <c:if
                        test="${param.status == 'STATUS_DELIVERED_PAID'}"> selected </c:if>> STATUS_DELIVERED_PAID
                </option>
                <option value="STATUS_REJECTED_UNPAID" <c:if
                        test="${param.status == 'STATUS_REJECTED_UNPAID'}"> selected </c:if>> STATUS_REJECTED_UNPAID
                </option>
                <option value="STATUS_RETURNED" <c:if test="${param.status == 'STATUS_RETURNED'}"> selected </c:if>>
                    STATUS_RETURNED
                </option>
                <option value="STATUS_CANCELED" <c:if test="${param.status == 'STATUS_CANCELED'}"> selected </c:if>>
                    STATUS_CANCELED
                </option>
            </select></div>
        <br>
        <div><label for="userFrom"> Дата от: </label> <input type="datetime-local" id="userFrom" name="from"
                                                             value="${param.from}"></div>
        <br>
        <div><label for="userTo"> Дата до: </label> <input type="datetime-local" id="userTo" name="to"
                                                           value="${param.to}">
        </div>
        <br>
        <button type="submit"> Найти</button>
    </form>
</sec:authorize>
<br>
<c:choose>
    <c:when test="${isAdmin}">
        <a href="${pageContext.request.contextPath}/orders/admin"> Сбросить фильтр </a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/orders"> Сбросить фильтр </a>
    </c:otherwise>
</c:choose>
<hr>
<c:choose>
    <c:when test="${empty orderShortDtos}">
        <div class="empty">
            Заказов нет.
        </div>
    </c:when>
    <c:otherwise>
        <table>
            <tr>
                <th>ID заказа</th>
                <th>Пользователь</th>
                <th>Дата оплаты</th>
                <th>Статус</th>
                <th>Стоимость</th>
                <th>Комментарий</th>
                <th>Подробнее</th>
            </tr>
            <c:forEach var="order" items="${orderShortDtos}">
                <tr>
                    <td>
                            ${order.orderId}
                    </td>
                    <td>
                            ${order.user.username}
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty order.payDate}">
                                ${order.payDate}
                            </c:when>
                            <c:otherwise>
                                Не оплачен
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty order.status}">
                                ${order.status}
                            </c:when>
                            <c:otherwise>
                                Нет статуса
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                            ${order.totalPrice}
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty order.comment}">
                                ${order.comment}
                            </c:when>
                            <c:otherwise>
                                —
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <a href="${pageContext.request.contextPath}/orders/admin/${order.orderId}">
                                Открыть
                            </a>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_USER')">
                            <a href="${pageContext.request.contextPath}/orders/${order.orderId}">
                                Открыть
                            </a>
                        </sec:authorize>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/items"> Вернуться к товарам </a>
</body>
</html>