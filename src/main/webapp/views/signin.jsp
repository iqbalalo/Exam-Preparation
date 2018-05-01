<%--
    Document   : Sign In
--%>
<%@ include file="../views/template/header.jsp" %>
<div class="col-lg-12">

    <c:if test="${not empty message}">
        <div class="alert alert-info" style="margin-top: .5em;">${message}</div>
    </c:if>
    <div class="col-lg-6" style="margin-top: 4em; margin-bottom:4em; line-height: 1.5em; font-size: 16px;">
        <%@ include file="signup_msg.html" %>
        <h3><a class="btn btn-info" href="${pageContext.request.contextPath}/signup">Registration for Online Model Test</a></h3>
    </div>
    <form action="${pageContext.request.contextPath}/signin" method="post">

        <div class="col-lg-4 col-lg-offset-2" style="margin-top: 2em; margin-bottom:4em;">
            <div class="page_actionbar_top">
                <h3>Sign In</h3>
            </div>
            <div class="page_body" style="padding: 1em 1.5em;">
                <input style="margin: 1em 0;" type="text" class="form-control" name="id" placeholder="Phone No." required>
                <input style="margin: 1em 0;" type="password" class="form-control" name="password" placeholder="Password" required>
            </div>
            <div class="page_actionbar_bottom">
                <input type="submit" class="btn btn-success" value="Sign In">
            </div>
        </div>

    </form>
</div>
<%@ include file="../views/template/footer.jsp" %>