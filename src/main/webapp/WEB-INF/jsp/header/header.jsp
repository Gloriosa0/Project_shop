<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header>
    <div class="header">
        <div class="header-top">
            <div class="header-title">
                Интернет-магазин
            </div>
            <div class="header-links">
                <a href="${pageContext.request.contextPath}/items">Товары</a>
                <a href="${pageContext.request.contextPath}/profile">Профиль</a>
                <a href="${pageContext.request.contextPath}/orders">Мои заказы</a>
                <a href="${pageContext.request.contextPath}/cart">Корзина</a>
                <form method="post" action="${pageContext.request.contextPath}/logout" style="display:inline;">
                    <button type="submit">Выйти</button>
                </form>
            </div>
        </div>
        <div class="search-container">
            <form class="search-form" method="get" action="${pageContext.request.contextPath}/items/search">
                <input type="text" name="itemName" placeholder="Название товара" required>
                <button type="submit">Найти</button>
            </form>
        </div>
        <div class="admin-search">
            <div class="filter-title">
                Фильтр товаров
            </div>
            <form class="filter-form" method="get" action="${pageContext.request.contextPath}/items/filter">
                <input type="text" name="itemName" placeholder="Название">
                <sec:authorize access="!hasRole('ROLE_ADMIN')">
                    <input type="hidden" name="inStock" value="true">
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <select name="inStock">
                        <option value="true">Только в наличии</option>
                        <option value="false">Нет в наличии</option>
                    </select>
                </sec:authorize>
                <input type="number" name="minPrice" step="0.01" min="0" placeholder="Цена от">
                <input type="number" name="maxPrice" step="0.01" min="0" placeholder="Цена до">
                <input type="text" name="seller" placeholder="Продавец">
                <label>Дата от:</label>
                <input type="date" name="minDate">
                <label>Дата до:</label>
                <input type="date" name="maxDate">
                <button type="submit">Фильтровать</button>
            </form>
        </div>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <div class="admin-search">
                <div class="filter-title">
                    Расширенный поиск администратора
                </div>
                <form class="filter-form" method="get"
                      action="${pageContext.request.contextPath}/items/admin/advanced-search">
                    <input type="text" name="itemName" placeholder="Название">
                    <select name="inStock">
                        <option value="">Любое наличие</option>
                        <option value="true">В наличии</option>
                        <option value="false">Нет в наличии</option>
                    </select>
                    <button type="submit">Найти</button>
                </form>
            </div>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <div class="admin-links">
                <strong>Администрирование:</strong>
                <a href="${pageContext.request.contextPath}/items/admin">Товары</a>
                <a href="${pageContext.request.contextPath}/orders/admin">Заказы</a>
                <a href="${pageContext.request.contextPath}/admin/users">Пользователи</a>
            </div>
        </sec:authorize>
        <hr>
    </div>
</header>