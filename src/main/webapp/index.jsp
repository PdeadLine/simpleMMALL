<%@page pageEncoding="UTF-8"%>
<html>

<body>
    <div align="center">
    <h1>SpringMVC文件上传</h1>
    <form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"><br><br>
        <input type="submit" value="SpringMVC文件上传"/>
    </form>
    <h2>富文本图片上传</h2>
    <form  name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload-file" /><br><br>
        <input type="submit" value="富文本文件上传"/>
    </form>
    </div>
</body>
</html>
