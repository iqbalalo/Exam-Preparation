<%--
    Document   : User List
--%>
<%@ include file="../template/header.jsp" %>
<div class="col-lg-12 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <div class="page_body" style="max-height: 450px; overflow: scroll;">
        <%@ include file="../template/alert.jsp" %>

        <table class="mytable">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Password</th>
                    <th>Type</th>
                    <th style="width:10%"></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${users}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.name}</td>
                        <td>${u.password}</td>
                        <td>${u.type}</td>
                        <td><a class="delete" href="#" id="${u.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="page_actionbar_bottom">
        <div class="info" style="text-align: left;">Total User ${fn:length(users)}</div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/user.js"></script>
<%@ include file="../template/footer.jsp" %>
