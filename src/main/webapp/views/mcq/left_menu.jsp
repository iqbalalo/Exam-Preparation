<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-lg-2 custom-container-left-side">
    <form action="${pageContext.request.contextPath}/mcq/list" method="get">
        <div style="margin-bottom: .5em;">
            <%@ include file="exam_dropdown.jsp" %>
        </div>
        <div  style="margin-bottom: .5em;">
            <%@ include file="subject_dropdown.jsp" %>
        </div>
        <button type="submit">Submit</button>

    </form>
</div>