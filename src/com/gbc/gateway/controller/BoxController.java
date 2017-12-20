/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.controller;

import com.gbc.gateway.common.AppConst;
import com.gbc.gateway.common.CommonModel;
import com.gbc.gateway.common.JsonParserUtil;
import com.gbc.gateway.data.Box;
import com.gbc.gateway.model.BoxModel;
import com.gbc.gateway.model.HistoryBoxModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
public class BoxController extends HttpServlet {

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
            case "update_status_box":
                content = updateStatusBox(req, data);
                break;
            case "history_open_box":
                content = historyOpenBox(req, data);
                break;
            case "insert_box":
                content = insertBox(req, data);
                break;
            case "edit_box_info":
                content = editBoxInfo(req, data);
                break;
            case "delete_box":
                content = deleteBox(req, data);
                break;
            case "get_list_box":
                content = getListBoxByCabinetId(req, data);
                break;
            case "get_list_history_openbox":
                content = getListHistoryOpenBox(req, data);
                break;
        }

        CommonModel.out(content, resp);
    }

    private String updateStatusBox(HttpServletRequest req, String data) {
        String content;
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

    private String historyOpenBox(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int box_id = jsonObject.get("box_id").getAsInt();
                int cabinet_id = jsonObject.get("cabinet_id").getAsInt();
                String opencode = jsonObject.get("opencode").getAsString();

                if (cabinet_id <= 0 || box_id <= 0 || opencode.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = HistoryBoxModel.getInstance().insertHistoryBox(box_id, cabinet_id, opencode);
                    switch (ret) {
                        case 0:
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert history open box success");
                            break;
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "insert history open box failed");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".historyOpenBox: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    private String insertBox(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                Box box = _gson.fromJson(jsonObject.get("box").getAsJsonObject(), Box.class);
                if (box == null) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = BoxModel.getInstance().insertBox(box);
                    if (ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert box success", box);
                    } else {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert box failed");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".insertBox: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String deleteBox(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                JsonArray arrItemIDDel = null;
                if (jsonObject.has("list_item_id_del")) {
                    JsonElement ele = jsonObject.get("list_item_id_del");
                    if (ele.isJsonArray()) {
                        arrItemIDDel = ele.getAsJsonArray();
                    }
                }
                if (arrItemIDDel.size() <= 0) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = BoxModel.getInstance().deleteBoxbyBoxId(arrItemIDDel);
                    if (ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "delete box success");
                    } else {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "delete box failed");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".deleteBox: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    private String getListBoxByCabinetId(HttpServletRequest req, String data) {
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
                    JsonObject data_res = new JsonObject();
                    List<Box> list_box_master = new ArrayList<>();
                    List<JsonObject> list_box_module = new ArrayList<>();
                    ret = BoxModel.getInstance().getListBoxbyCabinetId(list_box_master, list_box_module, cabinet_id);
                    data_res.add("master", new Gson().toJsonTree(list_box_master));
                    data_res.add("module", new Gson().toJsonTree(list_box_module));
                    switch (ret) {
                        case 0:
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "get list box success", data_res);
                            break;
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "get list box failed");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".getListBoxByCabinetId: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String editBoxInfo(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                Box box = _gson.fromJson(jsonObject.get("box").getAsJsonObject(), Box.class);
                if (box == null) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = BoxModel.getInstance().updateBoxInfo(box);
                    if (ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "edit box success", box);
                    } else {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "edit box failed");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".editBoxInfo: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String getListHistoryOpenBox(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int currentPage = 0;
                int total_item_per_page = 0;
                int length = 0;
                List<Integer> lengthList = null;
                String fromDate = "";
                String toDate = "";
                List<JsonObject> list_history = new ArrayList<>();
                if(jsonObject.has("current_page")){
                    currentPage = jsonObject.get("current_page").getAsInt();
                }  
                if(jsonObject.has("total_item_per_page")){
                    total_item_per_page = jsonObject.get("total_item_per_page").getAsInt();
                } 
                if(jsonObject.has("fromDate")){
                    fromDate = jsonObject.get("fromDate").getAsString();
                } 
                if(jsonObject.has("toDate")){
                    toDate = jsonObject.get("toDate").getAsString();
                } 
                
                if(currentPage > 0 && total_item_per_page > 0 && !fromDate.isEmpty() && !toDate.isEmpty()){  
                    int fromRecord = currentPage*total_item_per_page - total_item_per_page;   
                    lengthList = new ArrayList<>(); 
                    ret = BoxModel.getInstance().getListHistoryOpenBox(list_history, fromRecord, total_item_per_page, fromDate, toDate, lengthList);
                    length = lengthList.get(0);
                }
                
                if (ret == 0) {
                    JsonObject jdata = new JsonObject();
                    jdata.addProperty("length", length);
                    jdata.addProperty("list_history", list_history.toString());
                    content = CommonModel.FormatResponse(AppConst.NO_ERROR, "get history openbox success", jdata);
                } else {
                    content = CommonModel.FormatResponse(AppConst.NO_ERROR, "get history openbox failed");
                }

            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".editBoxInfo: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }
}
