<%--
    Document   : Registration List
--%>
<%@ include file="../template/header.jsp" %>
<div class="col-lg-12 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <div class="page_body" style="max-height: 450px; overflow: scroll;">
        <%@ include file="../template/alert.jsp" %>

        <table class="mytable">
            <thead>
                <tr>
                    <th>Tx. Id</th>
                    <th>Tx. Date</th>
                    <th>Tx. Amount</th>
                    <th>Phone</th>
                    <th>Device Id</th>
                    <th>Exam</th>
                    <th>Registration Date</th>
                    <th>Access Key</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${regs}" var="reg" varStatus="sl">
                    <tr>
                        <td>${reg.id}</td>
                        <td>${reg.txDate}</td>
                        <td>${reg.txAmount}</td>
                        <td>${reg.phone}</td>
                        <td>${reg.deviceId}</td>
                        <td>${reg.exam}</td>
                        <td>${reg.regDate}</td>
                        <td>${reg.accessKey}</td>
                        <td><a class="delete" href="#" id="${reg.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="page_actionbar_bottom">
        <div class="info" style="text-align: left;">Total Registration: ${fn:length(regs)}</div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/registration.js"></script>
<%@ include file="../template/footer.jsp" %>
