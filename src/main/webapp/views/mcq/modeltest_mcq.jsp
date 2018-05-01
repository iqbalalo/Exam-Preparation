<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid" style="font-size: 1em; padding: 0.5em 0px 1em; margin-bottom: 0.8em;  border: 1px solid #F0F0F0;">
    <div class="col-lg-1" style="padding-right: 2px;">
        Q. ${qSl}.
    </div>
    <div class="col-lg-11" style="color: #F6360C;font-weight: bold;">
        ${mcq.question}
    </div>

    <div class="col-lg-12">
        <div class="row">
            <c:forEach items="${mcq.answerOptions}" var="answerOption" varStatus="loop">
                <c:if test="${loop.index eq 0 or  loop.index eq 2}">
                    <div class="row">
                    </c:if>
                    <div class="col-lg-6" style="margin:1.5em 0 0;">
                        <div class="col-lg-1">
                            <input type="radio" class="correctAnswer" name="correctAnswer" value="${answerOption}" required>
                        </div>
                        <div class="col-lg-11">${answerOption}</div>
                    </div>
                    <c:if test="${loop.index eq 1 or loop.index eq 3}">
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>
