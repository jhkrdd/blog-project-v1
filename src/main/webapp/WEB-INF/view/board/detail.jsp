<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>


<div class="container my-3">
    <c:if test="${brbyid.userId == principal.id}">
        <div class="mb-3">
            <a href="/board/${brbyid.id}/updateForm" class="btn btn-warning">수정</a>
            <button onclick="deleteById(${brbyid.id})" class="btn btn-danger">삭제</button>
        </div>
    </c:if>

    <div class="mb-2 d-flex justify-content">
        글 번호 :
        <span id="id" class="me-3">
            <i>${brbyid.id}</i>
        </span>
        작성자 :
        <span class="me-3">
            <i>${brbyid.username} </i>
        </span>
    </div>

    <div>
        <h1><b>${brbyid.title}</b></h1>
    </div>
    <hr/>
    <div>
        <div>${brbyid.content}</div>
    </div>
    <hr/>

    <div class="card mt-3">
        <form>
            <div class="card-body">
                <textarea id="reply-content" class="form-control" rows="1"></textarea>
            </div>
            <div class="card-footer">
                <button type="button" id="btn-reply-save" class="btn btn-primary">등록</button>
            </div>
        </form>
    </div>
    <br/>
    <div class="card">
        <div class="card-header">댓글 리스트</div>
        <ul id="reply-box" class="list-group">
            <li id="reply-1" class="list-group-item d-flex justify-content-between">
                <div>댓글내용입니다</div>
                <div class="d-flex">
                    <div class="font-italic">작성자 : cos &nbsp;</div>
                    <button onClick="replyDelete()" class="badge bg-secondary">삭제</button>
                </div>
            </li>
        </ul>
    </div>
</div>
<script>
    function deleteById(id) {
        
        $.ajax({
            type:"delete",
            url:"/board/"+id,
            dataType: "json"
        }).done((res) => {
            alert(res.msg);
            location.href="/";
        }).fail((err)=>{
            alert(err.responseJSON.msg);
        });
    }
</script>
<%@ include file="../layout/footer.jsp"%>