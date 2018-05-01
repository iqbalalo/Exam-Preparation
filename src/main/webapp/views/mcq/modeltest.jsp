<%--
    Document   : Model Test
--%>
<%@ include file="../template/header.jsp" %>
<div class="col-lg-12 custom-container-left-side">
    <%@ include file="../template/alert.jsp" %>

    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span>${exam.name} - ${subject.name}</span>
                <span id="examTime" class="pull-right" style="font-weight: bold;"></span>
                <div class="clearfix"></div>
            </div>
            <div id="questionHolder" class="panel-body">

            </div>
            <div class="panel-footer">
                <button id="btnCancel" type="button" class="btn btn-default btn-sm pull-right">Cancel</button>
                <button id="btnFinish" style="margin-right:.5em;" type="button" class="btn btn-success btn-sm pull-right">Finish</button>
                <button id="btnFirst" style="margin-right:.5em;" type="button" class="btn btn-default btn-sm"><i class="fa fa-angle-double-left"></i> First</button>
                <button id="btnPrevious" style="margin-right:.5em;" type="button" class="btn btn-default btn-sm"><i class="fa fa-angle-left"></i> Previous</button>
                <button id="btnNext" type="button" class="btn btn-default btn-sm">Next <i class="fa fa-angle-right"></i></button>
            </div>
        </div>
    </div>
</div>
<script>
    var exam = "<c:out value='${exam.id}'/>";
    var subject = "<c:out value='${subject.id}'/>";

    var mcqIds = new Array();
    <c:forEach items="${mcqs}" var="mcq">
    mcqIds.push(${mcq.id});
    </c:forEach>
</script>
<script src="${pageContext.request.contextPath}/resources/js/examinee.js"></script>
<%@ include file="../template/footer.jsp" %>