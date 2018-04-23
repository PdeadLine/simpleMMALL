package com.tmall.controller.backend;

import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.Product;
import com.tmall.pojo.User;
import com.tmall.service.IFileService;
import com.tmall.service.IProductService;
import com.tmall.service.IUserService;
import com.tmall.util.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * create by sintai
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 新增产品或更新产品
     * @param session
     * @param product
     * @return
     */
    @ResponseBody
    @RequestMapping("save.do")
    public ServerResponse save(HttpSession session,Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 设置商品上下架状态
     * @param productId
     * @param status
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(Integer productId,Integer status,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.setSaleStauts(productId,status);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 产品list
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                               HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 商品详细
     * @param productId
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("detial.do")
    public ServerResponse getDetial(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理

            return iProductService.manageProductDetial(productId);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName,
                                 Integer productId,
                                 @RequestParam(value ="pageNum",defaultValue = "1")
                                         Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "10")
                                 Integer pageSize,
                                 HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.productSearch(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 上传文件
     * @param session
     * @param file
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("upload.do")
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url= PropertiesUtil.getProperty("")+targetFileName;

            Map fileMap = new HashMap();
            fileMap.put("uri",targetFileName );
            fileMap.put("url",url);
            return ServerResponse.createBySuccessData(fileMap);//返回一个图片的URL地址
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     *  富文本图片上传
     * @param session
     * @param file
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("richtext_img_upload.do")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response){
        Map resultMap = new HashedMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登陆管理员");
            return resultMap;
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;

        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }

    }
}
