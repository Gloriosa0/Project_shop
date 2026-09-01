<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Магазин</title>
</head>
<body>
<jsp:include page="header/header.jsp"/>
<h1>Пользователи</h1>
<div class="search">
    <h2>Поиск пользователей</h2>
    <form method="get" action="${pageContext.request.contextPath}/admin/users/search">
        <div>
            <label for="role">Роль:</label>
            <select name="role" id="role">
                <option value="">Любая</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role}">${role}</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="enabled">Аккаунт активен:</label>
            <select name="enabled" id="enabled">
                <option value="">Любой</option>
                <option value="true">Активен</option>
                <option value="false">Не активен</option>
            </select>
        </div>
        <div>
            <label for="accountNonLocked">Аккаунт не заблокирован:</label>
            <select name="accountNonLocked" id="accountNonLocked">
                <option value="">Любой</option>
                <option value="true">Не заблокирован</option>
                <option value="false">Заблокирован</option>
            </select>
        </div>
        <button type="submit">Найти</button>
    </form>
</div>
<div>
    <c:choose>
        <c:when test="${empty userListForAdminDtos}">
            <p>Пользователи не найдены.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Username</th>
                    <th>Телефон</th>
                    <th>Баланс</th>
                    <th>Статус</th>
                    <th>Подробнее</th>
                    <th>Действие</th>
                </tr>
                <c:forEach var="user" items="${userListForAdminDtos}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.username}</td>
                        <td>${user.phoneNum}</td>
                        <td>${user.balance}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.enabled}">
                                    Активен
                                </c:when>
                                <c:otherwise>
                                    Неактивен
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}">Открыть</a>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.accountNonLocked}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/users/${user.id}/blacklist">
                                        <button type="submit">Добавить в черный список</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/users/${user.id}/unblacklist">
                                        <button type="submit">Убрать из черного списка</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
<br>
<a href="${pageContext.request.contextPath}/items"> Вернуться к товарам </a>
</body>
</html>