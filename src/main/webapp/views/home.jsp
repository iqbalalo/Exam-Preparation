<%--
    Document   : Home
--%>
<%@ include file="../views/template/header.jsp" %>
<div class="col-lg-12 custom-container-right-side">
    <%@ include file="../views/template/alert.jsp" %>

    <div class="col-lg-9">
        <div class="panel panel-default">
            <div class="panel-heading">
                Available Exam List
                <a href="${pageContext.request.contextPath}/ReCountMcq" class="pull-right">Refresh</a>
            </div>
            <div class="panel-body">
                <c:set var="grandTotalOfMcq" value="0" scope="page" />
                <c:forEach items="${exams}" var="exam">
                    <div>
                        <c:set var="examTotalMcq" value="0" scope="page" />
                        <c:forEach items="${exam.subjects}" var="s">
                            <c:set var="examTotalMcq" value="${examTotalMcq + s.totalMcq}" scope="page"/>
                        </c:forEach>
                        <c:set var="grandTotalOfMcq" value="${grandTotalOfMcq + examTotalMcq}" scope="page" />
                        <button type='button' style="width:100%;" data-toggle="collapse" data-parent="#accordion" data-target="#collapse${exam.id}"><b>${exam.name}</b> - ${examTotalMcq} MCQ(s) - ${exam.access}</button>
                        <ul id="collapse${exam.id}" class="panel-collapse collapse">
                            <c:forEach items="${exam.subjects}" var="s">
                                <li>${s.name} - ${s.totalMcq} MCQ(s)</li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">Summary</div>
            <div class="panel-body">

                <table class="table">
                    <tr>
                        <td>Total MCQ</td>
                        <td>${grandTotalOfMcq}</td>
                    </tr>
                    <tr>
                        <td>Total Registration</td>
                        <td>${totalReg}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

</div>
<%@ include file="../views/template/footer.jsp" %>