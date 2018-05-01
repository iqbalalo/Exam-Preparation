<%--
    Document   : New User Form
--%>
<%@ include file="../template/header.jsp" %>
<div class="col-lg-12 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <form action="${pageContext.request.contextPath}/user/save" method="post">
        <div class="page_body">
            <%@ include file="../template/alert.jsp" %>

            <div>
                <table class="table table-borderless" style="width: 75%;">
                    <tbody>
                        <tr>
                            <td>Name</td>
                            <td>
                                <input name="name" type="text" required/>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                        <tr>
                            <td>Phone Number</td>
                            <td>
                                <input name="id" type="text" required/>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td>
                                <input name="password" type="text" required/>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                        <tr>
                            <td>Type</td>
                            <td style="width:90%;">
                                <select name="type" required>
                                    <option value="admin">Admin</option>
                                    <option value="user" selected="true">User</option>
                                </select>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="page_actionbar_bottom">
            <button name="save" type="submit">Save</button>
            <button name="cancel" onclick="location.href = '${pageContext.request.contextPath}/user/list'" type="button">Cancel</button>
        </div>
    </form>
</div>
<%@ include file="../template/footer.jsp" %>