<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

${navCrumbs}

<c:choose>
    <c:when test="${ empty coupon.retailerSite}">
        You can add coupons for a Retailer site ONLY. <br>Click <a
            href="<c:url value='/admin/coupons/list.do'/>">here</a> to choose a retailer site.
    </c:when>
    <c:otherwise>
        <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
        <form:form modelAttribute="coupon" method="POST" action="add.do" enctype="multipart/form-data">
            <span id="error_message"></span>
            <table>
                <tbody>

                <tr>
                    <td><form:label path="title">Title:</form:label></td>
                    <td><form:input  maxlength="255" path="title"/></td>
                    <form:errors path="title" cssClass="error"/>
                </tr>


                <tr>
                    <td><form:label path="description">Description:</form:label></td>
                    <td><form:textarea path="description"/></td>
                    <form:errors path="description" cssClass="error"/>
                </tr>

                <tr>
                    <td><form:label path="couponCode">Code:</form:label></td>
                    <td><form:input path="couponCode"/></td>
                    <form:errors path="couponCode" cssClass="error"/>
                </tr>


                <tr>
                    <td><form:label path="startDate">Start Date:</form:label></td>
                    <td>
                        <spring:bind path="startDate">
                            <input type="text" readonly="true" name="startDate" id="startDate"
                                   value="<fmt:formatDate value="${coupon.startDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                        </spring:bind>
                        <span id="start_date_error" class="error_msg"></span>
                    </td>
                </tr>

                <tr>
                    <td><form:label path="endDate">End Date:</form:label></td>
                    <td>
                        <spring:bind path="endDate">
                            <input type="text" readonly="true" id="endDate" name="endDate"
                                   value="<fmt:formatDate value="${coupon.endDate}" pattern="MM/dd/yyyy hh:mm"/>"/>
                        </spring:bind>
                        <span id="end_date_error" class="error_msg"></span>
                    </td>
                </tr>

                </tbody>
            </table>
            <form:hidden path="retailerSite.id"/>
            <input type="submit" value="Save"/>
            <a class="gbutton" href="<c:url value='/admin/library/view.do?siteID='/>${coupon.retailerSite.id}#coupons">Cancel</a>
        </form:form>
    </c:otherwise>
</c:choose>
