<%--
    Document   : Examinee Home
--%>
<%@ include file="../views/template/header.jsp" %>
<div class="col-lg-12 custom-container-left-side">
    <%@ include file="../views/template/alert.jsp" %>

    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading"><span class="btn">Your Registered Exam(s)</span>
                <button id="buttonRegisterExam" class="btn btn-primary btn-sm pull-right" type="button" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus-circle fa-lg" style="vertical-align: text-top;"></i>&nbsp;&nbsp;Register New Exam</button>
                <div class="clearfix"></div>
            </div>
            <div class="panel-body">
                <div class="panel-group" id="accordion">
                    <!--Registered exam list-->
                    <c:forEach items="${registeredExams}" var="e">
                        <div class="panel panel-default" style="margin-bottom:.5em;">
                            <a style="text-decoration: none;" data-toggle="collapse" data-parent="#accordion" href="#collapse${e.id}">
                                <div class="panel-heading" >
                                    <c:set var="examTotalMcq" value="0" scope="page" />
                                    <c:forEach items="${e.subjects}" var="s">
                                        <c:set var="examTotalMcq" value="${examTotalMcq + s.totalMcq}" scope="page"/>
                                    </c:forEach>
                                    <b>${e.name}</b> &raquo; ${fn:length(e.subjects)} Subjects | <c:out value="${examTotalMcq}"/> MCQ(s)
                                </div>
                            </a>
                            <div id="collapse${e.id}" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <table class="table">
                                        <tbody>
                                            <c:forEach items="${e.subjects}" var="s">
                                                <tr>
                                                    <td>${s.name}</td>
                                                    <td style="text-align: right;">
                                                        <span style="font-size:11px;">${s.totalMcq} MCQ(s)</span>
                                                        <a class="btnModelTest btn btn-default btn-sm" id="${e.id}&${s.id}" href="#" class="btn btn-default btn-sm" type="button">Model Test</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading">Score History</div>
            <div class="panel-body">
                <table class="table" style="font-size: 12px;">
                    <thead>
                        <tr>
                            <th>Exam Time</th>
                            <th>Exam</th>
                            <th>Subject</th>
                            <th>Time Taken</th>
                            <th style="text-align: right;">Mark</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${scores}" var="score">
                            <tr>
                                <td>${score.examDateTime}</td>
                                <td>${score.exam}</td>
                                <td>${score.subject}</td>
                                <td>${score.timeTaken}</td>
                                <td style="text-align: right;">${score.obtainedMark} / ${score.totalMark}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>
    </div>

    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document" style="width:75%;">
            <form id="form" action="${pageContext.request.contextPath}/registration/register_exam" method="post">
                <div class="modal-content">

                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Exam Registration</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-lg-5">
                                <br/>
                                <select class="form-control-signin" name="exam" style="margin-bottom: 1em;" required>
                                    <option value="">Select Subject</option>
                                    <c:forEach items="${exams}" var="e">
                                        <option value="${e.id}">${e.name}</option>
                                    </c:forEach>
                                </select>
                                <div class="row"  style="margin-bottom: 1em;">
                                    <div class="col-lg-4">
                                        <select name="paymentBy" class="form-control" required>
                                            <option value="Cash">Cash</option>
                                            <option value="bKash">bKash</option>
                                            <option value="DBBL">DBBL</option>
                                        </select>
                                    </div>
                                    <div class="col-lg-8">
                                        <input type="text" class="form-control" name="transactionId" placeholder="bKash/DBBL Transaction ID" required>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-7">
                                <%@ include file="registration_instruction.html" %>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>
<script>

    $(".btnModelTest").click(function () {

        var response = confirm("Are you ready to attend in the Model Test?\n\nNumber of MCQ: 100\nExam Time: 120 Minutes");
        console.log(response);
        if (response) {
            $(".loading").toggleClass("hide");
            var result = $(this).attr("id").split("&");
            var exam = result[0];
            var subject = result[1];

            url = ctx + "/mcq/modeltest/" + exam + "/" + subject;
            window.location.href = url;
        }
    });
</script>
<%@ include file="../views/template/footer.jsp" %>