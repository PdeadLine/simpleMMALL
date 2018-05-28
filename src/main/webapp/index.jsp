<%@page pageEncoding="UTF-8"%>
<html>

<body>
    <form action="/user/login.do" method="post" enctype="multipart/form-data">
        用户:<input type="text" name="username"/><br>
        密码:<input type="text" name="password"/><br>
        <input type="submit" value="提交"/>
    </form>
    <div align="center">
    <h1>SpringMVC文件上传</h1>
    <form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"><br><br>
        <input type="submit" value="SpringMVC文件上传"/>
    </form>
    <h2>富文本图片上传</h2>
    <form  name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file" /><br><br>
        <input type="submit" value="富文本文件上传"/>
    </form>
    </div>
</body>
</html>
