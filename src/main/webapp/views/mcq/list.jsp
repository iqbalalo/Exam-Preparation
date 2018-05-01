<%--
    Document   : MCQ List
--%>
<%@ include file="../template/header.jsp" %>
<%@ include file="left_menu.jsp" %>
<div class="col-lg-10 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <div class="page_body" style="max-height: 450px; overflow: scroll;">
        <%@ include file="../template/alert.jsp" %>
        <c:forEach items="${mcqs}" var="mcq" varStatus="sl">
            <%@ include file="mcq.jsp" %>
        </c:forEach>
    </div>
    <div class="page_actionbar_bottom">
        <div class="info" style="text-align: left;">Total MCQ: ${fn:length(mcqs)}</div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/mcq.js"></script>
<%@ include file="../template/footer.jsp" %>
