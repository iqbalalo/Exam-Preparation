<select name="subject" class="form-control" required>
    <option value="">Select Subject</option>
    <c:forEach items="${subjects}" var="s">
        <option value="${s.id}" ${subject == s.id ? "selected='true'":''}>${s.name}</option>
    </c:forEach>
</select>