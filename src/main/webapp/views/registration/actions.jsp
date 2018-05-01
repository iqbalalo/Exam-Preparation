<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page_actionbar_top">
    <h4>${title}</h4>
    <c:if test="${title == 'Registration List'}">
        <div class="pull-right">
            <form method="Post" action="${pageContext.request.contextPath}/registration/search">
                <input type="text" name="keyword" placeholder="Type keywords here">
                <button type="submit" name="submit">Search</button>
                <c:if test="${not empty reset}">
                    <a href="${pageContext.request.contextPath}/registration">Clear Filter</a>&nbsp;
                </c:if>
            </form>
        </div>
    </c:if>
    <div class="pull-right"  style="margin-right: 5em;">
        <c:if test="${title != 'New Transaction'}">
            <button name="new" onclick="location.href = '${pageContext.request.contextPath}/registration/transaction/new'" type="button">Create New Transaction</button>
        </c:if>
    </div>
</div>