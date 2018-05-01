<%--
    Document   : New MCQ Form
--%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<%@ include file="../template/header.jsp" %>
<%@ include file="left_menu.jsp" %>
<div class="col-lg-10 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <form action="${pageContext.request.contextPath}/mcq/save" method="post">
        <div class="page_body">
            <%@ include file="../template/alert.jsp" %>

            <div class="mcq-holder" style="font-size: 1em; padding: 1em .25em;">
                <h4 style="margin-top:0;">Question</h4>
                <textarea name="question" class="form-control" style="width:100%;" required></textarea>
                <div id="multiple_choice_holder" style="margin: 2em 0;">
                    <div class="row" style="margin-bottom: 1em;">
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option A</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="0" required> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required></textarea>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option B</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="1" required> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 1em;">
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option C</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="2" required> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required></textarea>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option D</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="3" required> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 2em 0;">
                    <h4 style="margin-top:0;">Note</h4>
                    <textarea name="note"  class="form-control"></textarea>
                </div>
                <input type="text" name="history" class="form-control" placeholder="Exam history">
                <div class="row" style="margin: 2em 0;">
                    <div class="row col-lg-3">
                        <select name="exam" class="form-control" required>
                            <option value="">Select Exam</option>
                            <c:forEach items="${exams}" var="e">
                                <option value="${e.id}" ${e.id == '' ? "selected='true'" : ""}>${e.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-lg-3">
                        <select name="subject" class="form-control" required>
                            <option value="">Select Subject</option>
                            <c:forEach items="${subjects}" var="s">
                                <option value="${s.id}" ${subject == '' ? "selected='true'":''}>${s.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="page_actionbar_bottom">
            <button name="saveAndNewForm" type="submit">Save & New Form</button>
            <button name="save" type="submit">Save</button>
            <button name="cancel" onclick="location.href = '${pageContext.request.contextPath}/mcq/list?exam=${exam}&subject=${subject}'" type="button">Cancel</button>
        </div>
    </form>
</div>
<ckeditor:replaceAll className="form-control" basePath="${pageContext.request.contextPath}/resources/ckeditor/" />

<%@ include file="../template/footer.jsp" %>