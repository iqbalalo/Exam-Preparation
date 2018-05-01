<%--
    Document   : alert
--%>

<c:if test="${not empty message}">
    <div class="alert alert-info">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        ${message}
    </div>
</c:if>