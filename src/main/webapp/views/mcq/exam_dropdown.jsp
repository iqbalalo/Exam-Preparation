<select name="exam" class="form-control" required>
    <option value="">Select Exam</option>
    <c:forEach items="${exams}" var="e">
        <option value="${e.id}" ${e.id == exam ? "selected='true'" : ""}>${e.name}</option>
    </c:forEach>
</select>
