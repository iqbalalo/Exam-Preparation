<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid" style="font-size: 1em; padding: 0.5em 0px 1em; margin-bottom: 0.8em;  border: 1px solid #F0F0F0;">
    <div class="col-lg-1" style="padding-right: 2px;">
        Q. ${sl.index+1}.
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
                            <i class="fa fa-lg ${mcq.correctAnswer!=answerOption?"fa-circle-thin":"fa-check-circle"}"></i>
                        </div>
                        <div class="col-lg-11">${answerOption}</div>
                    </div>
                    <c:if test="${loop.index eq 1 or loop.index eq 3}">
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <c:if test="${not empty mcq.note}">
        <div class="col-lg-12" style="margin-top: 1em;">
            <div style="padding: 4px; border-bottom: 1px dashed #444; font-weight: bold;">Note</div>
            <div style="padding: 6px; color: #444;">
                ${mcq.note}
            </div>
        </div>
    </c:if>
    <div class="col-lg-12" style="border-top: 1px solid #eee; margin:1.5em 0 0; padding-top: .5em;">
        <div class="col-lg-9 pull-left">
            <c:if test="${not empty mcq.history}">
                <span style="font-size:11px;">Also came in: ${mcq.history}</span>
            </c:if>
        </div>
        <div class="col-lg-3 pull-right" style="text-align: right;">
            <a class="btn btn-default btn-xs" href="${pageContext.request.contextPath}/mcq/edit/${mcq.id}">Edit</a>
            <c:if test="${loggedUser.type=='admin'}">
                <a class="delete btn btn-danger btn-xs" href="#" id="del_${mcq.id}">Delete</a>
                <a class="btn btn-success btn-xs" href="${pageContext.request.contextPath}/mcq/saveas/${mcq.id}" href="#">Save As</a>
            </c:if>
        </div>
    </div>
</div>
