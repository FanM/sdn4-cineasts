<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="director" type="org.neo4j.cineasts.domain.Director"--%>
<%--@elvariable id="movie" type="org.neo4j.cineasts.domain.Movie"--%>
<c:choose>
    <c:when test="${director != null}">
      <div class="span-4">
        <div class="actor-framed-image" style="background-image:url(${director.profileImageUrl})">
        </div>
        <div class="actor-sidebar">  
          <h2>${director.name}</h2>
          <p>Born <fmt:formatDate value="${director.birthday}" pattern="yyyy/dd/MM"/> in ${director.birthplace}.</p>
        </div>
        <div class="break"></div>
      </div>
      <div class="span-8 last">
        <h2>Biography</h2>
        <p>${actor.biography}</p>
        <h2>Directing</h2>
        <c:if test="${not empty movies}">
          <ul>
            <c:forEach items="${movies}" var="movie">
              <li>
                <a href="/movies/${movie.id}"><c:out value="${movie.title}" /> in <c:out value="${movie.year}"/></a><br/>
              </li>
            </c:forEach>
          </ul>
        </c:if>
      </div>
    </c:when>
    <c:otherwise>
      <h2>Actor cannot be found.</h2>
    </c:otherwise>
</c:choose>
