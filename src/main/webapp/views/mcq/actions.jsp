<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page_actionbar_top">
    <h4>${title}</h4>
    <c:if test="${title!= 'Excel File Upload Form'}">
        <c:if test="${title == 'MCQ List'}">
            <div class="pull-right">
                <form method="Post" action="${pageContext.request.contextPath}/mcq/search">
                    <input type="text" name="keyword" placeholder="Type keywords here">
                    <button type="submit" name="submit">Search</button>
                    <c:if test="${not empty reset}">
                        <a href="${pageContext.request.contextPath}/mcq/list?exam=${resetToExam}&subject=${resetToSubject}">Clear Filter</a>&nbsp;
                    </c:if>
                </form>
            </div>
        </c:if>
        <div class="pull-right"  style="margin-right: 5em;">
            <c:if test="${title != 'MCQ List'}">
                <button name="showList" onclick="location.href = '${pageContext.request.contextPath}/mcq/list?exam=${exam}&subject=${subject}'" type="button">Show List</button>
            </c:if>
            <c:if test="${title != 'New MCQ'}">
                <button name="new" onclick="location.href = '${pageContext.request.contextPath}/mcq/new'" type="button">Create New MCQ</button>
            </c:if>
        </div>
    </c:if>
</div>