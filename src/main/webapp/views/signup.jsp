<%--
    Document   : Sign Up
--%>
<%@ include file="../views/template/header.jsp" %>
<div class="col-lg-12">

    <form action="${pageContext.request.contextPath}/signup" method="post">

        <div class="col-lg-5 col-lg-offset-1" style="margin-top: 4em; margin-bottom:4em;">
            <div class="page_actionbar_top">
                <h3>Sign Up</h3>
            </div>
            <div class="page_body" style="padding: 1em 1.5em;">
                <c:if test="${not empty message}">
                    <div class="alert alert-info">${message}</div>
                </c:if>
                <input style="margin-bottom: 1em;" type="text" class="form-control" name="name" placeholder="Name" required>
                <input style="margin-bottom: 1em;" type="text" class="form-control" name="phone" placeholder="Phone No." required>
                <input style="margin-bottom: 1em;" type="password" class="form-control" name="password" placeholder="Password" required>
            </div>
            <div class="page_actionbar_bottom">
                <a class="btn btn-default pull-left" href="${pageContext.request.contextPath}/signin">Back</a>
                <input type="submit" class="btn btn-success" value="Sign Up">
            </div>
        </div>
        <div class="col-lg-5" style="margin-top: 4em; margin-bottom:4em; line-height: 1.5em; font-size: 16px;">
            <%@ include file="signup_instruction.html" %>
        </div>

    </form>
</div>
<%@ include file="../views/template/footer.jsp" %>