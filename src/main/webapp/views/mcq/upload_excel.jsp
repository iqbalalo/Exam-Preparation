<%--
    Document   : New MCQ Form
--%>
<%@ include file="../template/header.jsp" %>
<%@ include file="left_menu.jsp" %>
<div class="col-lg-10 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <%@ include file="../template/alert.jsp" %>

    <form action="${pageContext.request.contextPath}/mcq/excel_file_submit" method="post" enctype="multipart/form-data">
        <div style="margin: 2em 0;">
            <span class="btn btn-default btn-file">
                Select Excel File <input name="excelFile" type="file">
            </span>
            <button class="btn btn-default" name="submit">Upload</button>
        </div>
    </form>
</div>
<%@ include file="../template/footer.jsp" %>