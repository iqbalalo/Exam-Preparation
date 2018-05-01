<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page_actionbar_top">
    <h4>${title}</h4>
    <div class="pull-right"  style="margin-right: 5em;">
        <c:if test="${title != 'New User'}">
            <button name="new" onclick="location.href = '${pageContext.request.contextPath}/user/new'" type="button">Create New User</button>
        </c:if>
    </div>
</div>