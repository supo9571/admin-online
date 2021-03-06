package com.data.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.data.config.GlobalConfig;
import com.data.controller.BaseController;
import com.data.mapper.TenantMapper;
import com.data.service.UpdateService;
import com.manager.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author marvin 2021/8/30
 * 热更新
 */
@RestController
@RequestMapping("/api/v1")
public class HotUpdateController extends BaseController {

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private UpdateService updateService;
    @Autowired
    private TenantMapper tenantMapper;

    @GetMapping("/hotupdate")
    public JSONObject hotUpdate() {
//        String device = getHeader("Client-Device");//设备类型 windows,ios，android
//        String machineid = getHeader("Client-MachineCode");//设备机器码（设备唯一id）
        String hotVersion = getHeader("Client-VersionRes");//当前包内热更资源版本号 例：1.0.1
        String hotChannel = getHeader("Client-PackageChannel");//热更新渠道

        String channelId = getHeader("Client-ChannelId");//渠道id
        String versionId = getHeader("Client-VersionId");//版本号
        String platform = getHeader("Client-platform");//平台 windows,ios，android
        String ip = getIp();
        Integer tid = tenantMapper.getTidByCid(channelId);
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("apiUrl", new String[]{globalConfig.getApiUrl()});
        data.put("errUploadUrl", globalConfig.getErrUploadUrl());
        data.put("headUrl", globalConfig.getHeadUrl());
        data.put("platform",tid);
        //添加 客服信息
        data.put("customer", updateService.selectConsumer());
        //添加 停服公告
        data.put("stop_notice", updateService.selectStopNotice(channelId));
        //添加 整包更新信息
        String updateUrl = updateService.selectAllupdate(channelId, versionId);
        if (StringUtils.isNotBlank(updateUrl)) {
            updateUrl = updateUrl.replaceAll(".apk","_"+channelId+".apk");
            JSONObject appupdate = new JSONObject();
            appupdate.put("update_url", globalConfig.getResourcesUrl() + updateUrl);
            data.put("appupdate", appupdate);
        }
        //添加 热更信息
        List<Map> list = updateService.selectPackage(ip, hotChannel, hotVersion, platform);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                JSONObject gameInfo = JSONObject.parseObject((String) map.get("game_info"));
                Map<String, Object> platMap;
                platMap = gameInfo.getObject(platform, Map.class);
                JSONArray jsonArray = new JSONArray();
                for (String key : platMap.keySet()) {
                    JSONObject value = (JSONObject) platMap.get(key);
                    if ("lobby".equals(key)) {//大厅
                        JSONObject lobbyhotfix = new JSONObject();
                        lobbyhotfix.put("game_code", key);
                        lobbyhotfix.put("game_id", globalConfig.getGameConfig().get(key));
                        lobbyhotfix.put("manifest_res", value.get("manifest_res"));
                        lobbyhotfix.put("release_time", map.get("release_time"));
                        lobbyhotfix.put("resources_url", globalConfig.getResourcesUrl() + value.get("resources_url"));
                        lobbyhotfix.put("version", map.get("version"));
                        data.put("lobbyhotfix", lobbyhotfix);
                    }
                    if ("update".equals(key)) {//热更新模块数据
                        JSONObject updatehotfix = new JSONObject();
                        updatehotfix.put("game_code", key);
                        updatehotfix.put("game_id", globalConfig.getGameConfig().get(key));
                        updatehotfix.put("manifest_res", value.get("manifest_res"));
                        updatehotfix.put("release_time", map.get("release_time"));
                        updatehotfix.put("resources_url", globalConfig.getResourcesUrl() + value.get("resources_url"));
                        updatehotfix.put("version", map.get("version"));
                        data.put("updatehotfix", updatehotfix);
                    }
                    JSONObject hotfix = new JSONObject();
                    hotfix.put("game_code", key);
                    hotfix.put("game_id", globalConfig.getGameConfig().get(key));
                    hotfix.put("manifest_res", value.get("manifest_res"));
                    hotfix.put("release_time", map.get("release_time"));
                    hotfix.put("resources_url", globalConfig.getResourcesUrl() + value.get("resources_url"));
                    hotfix.put("version", map.get("version"));
                    jsonArray.add(hotfix);
                }
                data.put("hotfix", jsonArray);
            }
        }
        result.put("data", data);
        result.put("msg", "ok");
        result.put("status", 200);

        return result;
    }
}
