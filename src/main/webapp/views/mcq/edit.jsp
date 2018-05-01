<%--
    Document   : Edit MCQ Form
--%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<%@ include file="../template/header.jsp" %>
<%@ include file="left_menu.jsp" %>
<div class="col-lg-10 custom-container-right-side">
    <%@ include file="actions.jsp" %>
    <form action="${pageContext.request.contextPath}/mcq/update" method="post">
        <div class="page_body">
            <%@ include file="../template/alert.jsp" %>

            <div class="mcq-holder" style="font-size: 1em; padding: 1em .25em;">
                <h4 style="margin-top:0;">Question</h4>
                <input name="id" type="hidden" value="${mcq.id}">
                <textarea name="question" class="form-control" style="width:100%;" required>${fn:trim(mcq.question)}</textarea>
                <div id="multiple_choice_holder" style="margin: 2em 0;">
                    <div class="row" style="margin-bottom: 1em;">
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option A</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="0" required ${mcq.correctAnswer==mcq.answerOptions[0]?"checked":""}> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required>${fn:trim(mcq.answerOptions[0])}</textarea>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option B</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="1" required ${mcq.correctAnswer==mcq.answerOptions[1]?"checked":""}> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required>${fn:trim(mcq.answerOptions[1])}</textarea>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 1em;">
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option C</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="2" required  ${mcq.correctAnswer==mcq.answerOptions[2]?"checked":""}> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required>${fn:trim(mcq.answerOptions[2])}</textarea>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="col-lg-6">
                                <h4 style="margin-top:0;">Option D</h4>
                            </div>
                            <div class="col-lg-6" style="text-align: right;">
                                <input type="radio" name="correct_answer" value="3" required  ${mcq.correctAnswer==mcq.answerOptions[3]?"checked":""}> Correct Answer
                            </div>
                            <div class="col-lg-12">
                                <textarea name="answer_options[]" class="form-control" required>${fn:trim(mcq.answerOptions[3])}</textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 2em 0;">
                    <h4 style="margin-top:0;">Note</h4>
                    <textarea name="note"  class="form-control">${mcq.note}</textarea>
                </div>
                <input type="text" name="history" value="${mcq.history}" class="form-control" placeholder="Exam history">
                <div class="row" style="margin: 2em 0;">
                    <div class="row col-lg-3">
                        <%@ include file="exam_dropdown.jsp" %>
                    </div>
                    <div class="col-lg-3">
                        <%@ include file="subject_dropdown.jsp" %>
                    </div>
                </div>
            </div>
        </div>
        <div class="page_actionbar_bottom">
            <button name="save" type="submit">Save</button>
            <button name="cancel" onclick="location.href = '${pageContext.request.contextPath}/mcq/list?exam=${mcq.exam}&subject=${mcq.subject}'" type="button">Cancel</button>
        </div>
    </form>
</div>
<ckeditor:replaceAll className="form-control" basePath="${pageContext.request.contextPath}/resources/ckeditor/" />

<%@ include file="../template/footer.jsp" %>