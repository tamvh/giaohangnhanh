/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.controller;

import com.gbc.gateway.common.AppConst;
import com.gbc.gateway.common.CommonModel;
import com.gbc.gateway.common.JsonParserUtil;
import com.gbc.gateway.data.Cabinet;
import com.gbc.gateway.model.AccountModel;
import com.gbc.gateway.model.CabinetModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class CabinetController extends HttpServlet {

    protected final Logger logger = Logger.getLogger(this.getClass());
    private static final Gson _gson = new Gson();
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

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        switch (cmd) {
            case "cabinet":
                content = getListCabinet(req);
                break;
            case "insert_cabinet":
                content = insertCabinet(req, data);
                break; 
            case "delete_cabinet":
                content = deleteCabinet(req, data);
                break; 
        }

        CommonModel.out(content, resp);
    }

    private String getListCabinet(HttpServletRequest req) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            List<Cabinet> list_cabinet = new ArrayList<>();
            ret = CabinetModel.getInstance().getListCabinet(list_cabinet);
            if (ret == 0) {
                content = CommonModel.FormatResponse(AppConst.NO_ERROR, "", list_cabinet);
            } else {
                content = CommonModel.FormatResponse(AppConst.ERROR_GET_LIST_CABINET, "");
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".getListCabinet: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String insertCabinet(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                Cabinet cabinet = _gson.fromJson(jsonObject.get("cabinet").getAsJsonObject(), Cabinet.class);
                if (cabinet.getId() <= 0) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = CabinetModel.getInstance().insertCabinet(cabinet);
                    if(ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert cabinet success");
                    } else {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert cabinet failed");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".insertCabinet: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String deleteCabinet(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                int cabinet_id = jsonObject.get("cabinet_id").getAsInt();
                if (cabinet_id <= 0) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = CabinetModel.getInstance().deleteCabinetById(cabinet_id);
                    if(ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "delete cabinet success");
                    } else {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "delete cabinet failed");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".deleteCabinet: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }
}
