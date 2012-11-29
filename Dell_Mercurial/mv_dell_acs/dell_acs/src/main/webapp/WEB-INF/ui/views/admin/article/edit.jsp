<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>Edit Article</title>
</head>
<body>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>

<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">

${navCrumbs}


<c:choose>
    <c:when test="${invalidType ==  true}">
        <a style="color: red;"><b>NOTE : Trying to load Content type which is not of type ARTICLE and is of type ${contentType}.</b></a>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${article != null}">

                <fieldset style="padding: 10px;">
                    <legend><strong>Edit Article</strong></legend>
                    <form:errors/>
                    <form:form modelAttribute="article" commandName="article" method="POST"
                               enctype="multipart/form-data"
                               action="edit.do">

                        <table>
                            <tr>
                                <td><form:label path="name">Title:<b style="color:red">*</b></form:label></td>

                                <td><form:input autocomplete="false" path="name" id="title"/>
                             <span id="name_error" class="error_msg">
                                 <form:errors path="name"/>
                            </span>
                                </td>
                            </tr>

                            <tr>
                                <td><form:label path="description">Description:</form:label></td>
                                <td><form:textarea path="description" id="contentDescription"/>
                                </td>
                            </tr>

                            <tr>
                                <td><form:label path="body">Body:<b style="color:red">*</b></form:label></td>
                                <td><form:textarea path="body" id="articleBody"/>
                            <span id="body_error" class="error_msg">
                                 <form:errors path="body"/>
                            </span>
                                </td>
                            </tr>

                            <tr>
                                <td><form:label path="author">Author:</form:label></td>
                                <td><form:input path="author"/></td>
                            </tr>

                            <tr>
                                <td><form:label path="source">Source:</form:label></td>
                                <td><form:input path="source"/></td>
                            </tr>

                            <tr>
                                <td><form:label path="abstractText">Abstract Text:</form:label></td>
                                <td><form:input path="abstractText"/></td>
                            </tr>

                            <tr>
                                <td><form:label path="publishDate">Publish Date:</form:label></td>
                                <td>
                                    <spring:bind path="publishDate">
                                        <input type="text" readonly="true" id="publishDate" name="publishDate"
                                               value="<fmt:formatDate value="${article.publishDate}" pattern="MM/dd/yyyy HH:mm"/>"/>
                                    </spring:bind>
                                    <span id="publish_date_error" class="error_msg"><form:errors
                                            path="publishDate"/></span>
                                </td>
                            </tr>
                            <tr>
                                <td>Tag it</td>
                                <td>
                                    <table>
                                        <tr>
                                            <td>

                                                <table border="0">
                                                    <tr>
                                                        <td>
                                                <a href="#" onClick='Tagview.showTagPopup("ARTICLE");' class='tag items_action' title="Choose Tag" align="right">Tags</a>
                                               </td>
                                                        <td>
                                                <span id="tags"></span>
                                                            <script type="text/javascript">
                                                                Tagview.trimtext('${tags}');
                                                                $('#tag').val('${tags}');
                                                                $('#TagValue').val('${tags}');
                                                            </script>

                                                        </td>
                                                    </tr>
                                                    </table>
                                                <input type="hidden" name="TagValue" id="TagValue" value="${tags}"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <form:hidden path="retailerSite.id"/>
                        <form:hidden path="id"/>
                        <input id="siteId" type="hidden" value="${article.retailerSite.id}"/>
                        <input type="submit" value="Update">
                        <a class="gbutton"
                           href="<c:url value='/admin/library/view.do?siteID='/>${article.retailerSite.id}#articles"
                           onclick="this.setCancelFlag();">Cancel</a>
                    </form:form>
                </fieldset>
            </c:when>
        </c:choose>
    </c:otherwise>
</c:choose>
</body>
</html>

