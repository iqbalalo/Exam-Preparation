<%--
    Document   : New Transaction Form
--%>
<%@ include file="../template/header.jsp" %>
<div class="col-lg-12 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <form action="${pageContext.request.contextPath}/registration/transaction/save" method="post">
        <div class="page_body">
            <%@ include file="../template/alert.jsp" %>

            <div>
                <table class="table table-borderless" style="width: 75%;">
                    <tbody>
                        <tr>
                            <td>Trx. Id</td>
                            <td style="width:90%;">
                                <select name="paymentBy" required>
                                    <option value="bKash">bKash</option>
                                    <option value="DBBL">DBBL</option>
                                    <option value="Cash">Cash</option>
                                </select>
                                <input name="txId" type="text" style="width: 50%;" required/>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                        <tr>
                            <td>Date</td>
                            <td>
                                <input name="txDate" type="text" class="datepicker" required/>
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                        <tr>
                            <td>Amount</td>
                            <td>
                                <input type="text" name="txAmount"  required/> Tk.
                                <i class="fa fa-check required-element"></i>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="page_actionbar_bottom">
            <button name="save" type="submit">Save</button>
            <button name="cancel" onclick="location.href = '${pageContext.request.contextPath}/registration/list'" type="button">Cancel</button>
        </div>
    </form>
</div>
<script>
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        yearRange: 'c-70:c+5',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true
    });
</script>

<%@ include file="../template/footer.jsp" %>