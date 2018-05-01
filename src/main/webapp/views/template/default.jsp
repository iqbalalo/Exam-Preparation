<div class="col-lg-10 custom-container-right-side">
    <div class="page_actionbar_top">
        <h4>${title}</h4>
        <%@ include file="actions.jsp" %>
    </div>
    <div class="page_body">

        <c:if test="${not empty message}">
            <div class="alert alert-info">${message}</div>
        </c:if>

    </div>
    <div class="page_actionbar_bottom">
        <input type="button" value="Print">
    </div>
</div>
