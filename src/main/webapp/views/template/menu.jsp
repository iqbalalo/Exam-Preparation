<%--
    Document   : Menu
--%>
<div class="row" style="background: #444;">
    <ul class="nav nav-pills">
        <li class="${menu == "Home" ? 'active' : ''}" style="float: left;"><a href="${pageContext.request.contextPath}/home"><i class="fa fa-home fa-lg" style="vertical-align: text-top;"></i>&nbsp;&nbsp;Home</a></li>
        <c:if test="${loggedUser.type=='admin' || loggedUser.type=='user'}">
            <li class="${menu == "MCQ" ? 'active' : ''}" style="float: left;"><a href="${pageContext.request.contextPath}/mcq"><i class="fa fa-list-ul fa-lg"style="vertical-align: text-top;"></i>&nbsp;&nbsp;MCQ Bank</a></li>
            <c:if test="${loggedUser.type=='admin'}">
                <li class="${menu == "Registration" ? 'active' : ''}" style="float: left;"><a href="${pageContext.request.contextPath}/registration"><i class="fa fa-android fa-lg"style="vertical-align: text-top;"></i>&nbsp;&nbsp;Registration</a></li>
                <li class="${menu == "UploadExcel" ? 'active' : ''}" style="float: left;"><a href="${pageContext.request.contextPath}/mcq/upload_excel_file"><i class="fa fa-upload fa-lg"style="vertical-align: text-top;"></i>&nbsp;&nbsp;Upload Excel File</a></li>
                <li class="${menu == "User" ? 'active' : ''}" style="float: left;"><a href="${pageContext.request.contextPath}/user"><i class="fa fa-user fa-lg"style="vertical-align: text-top;"></i>&nbsp;&nbsp;User</a></li>
            </c:if>
        </c:if>

        <li style='float:right;'><a href="${pageContext.request.contextPath}/signout">${loggedUser.name}&nbsp;&nbsp;<i class="fa fa-sign-out fa-lg"style="vertical-align: text-top;"></i>&nbsp;&nbsp;Sign Out</a></li>
    </ul>
</div>
