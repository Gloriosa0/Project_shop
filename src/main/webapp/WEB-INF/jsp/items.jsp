<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<div class="container">
    <div class="catalog">
        <h1>Каталог товаров</h1>
        <hr>
        <c:forEach items="${itemShortDtos}" var="item">
            <div class="item">
                <div class="details">
                    <table border="1">
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Цена</th>
                            <th>В наличии</th>
                            <th>Дата поступления</th>
                            <th>Действия</th>
                        </tr>
                        <tr>
                            <td>${item.itemId}</td>
                            <td>${item.itemName}</td>
                            <td>${item.price}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.inStock}">
                                        Да
                                    </c:when>
                                    <c:otherwise>
                                        Нет
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${item.arrivalDate}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/items/${item.itemId}">Подробнее</a>
                            </td>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <td>
                                    <div class="admin-actions">
                                        <a class="edit-button"
                                           href="${pageContext.request.contextPath}/items/admin/${item.itemId}/edit-item">
                                            Изменить товар
                                        </a>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/items/admin/${item.itemId}/delete"
                                              style="display:inline;">
                                            <button type="submit" class="delete-button"
                                                    onclick="return confirm('Удалить товар из каталога?');">
                                                Удалить
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </sec:authorize>
                        </tr>
                    </table>
                </div>
                <br>
            </div>
        </c:forEach>
        <a href="${pageContext.request.contextPath}/cart">Корзина</a>
    </div>
</div>
</body>
</html>