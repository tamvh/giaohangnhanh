/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.controller;

import com.gbc.gateway.common.AppConst;
import com.gbc.gateway.common.CommonModel;
import com.gbc.gateway.common.JsonParserUtil;
import com.gbc.gateway.model.BoxModel;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class BoxController extends HttpServlet {
    protected final Logger logger = Logger.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        switch (cmd) {
            case "update_status_box":
                content = updateStatusBox(req, data);
                break;
        }

        CommonModel.out(content, resp);
    }

    private String updateStatusBox(HttpServletRequest req, String data) {
        String content = null;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                int box_id = jsonObject.get("box_id").getAsInt();
                int box_status = jsonObject.get("box_status").getAsInt();
                
                if (box_status < 0 || box_id <= 0) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = BoxModel.getInstance().updateBoxStatus(box_id, box_status);
                    switch (ret) {
                        case 0:         
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "update box status success");
                            break;                 
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "update box status failed");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".insertAccount: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }
}
