package com.tinuvile.infrastructure.dcc;


import com.tinuvile.types.annotations.DCCValue;
import com.tinuvile.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/7
 */
@Service
public class DCCService {

    /* 降级开关 0关闭 1开启 */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;
    
    /* 切流范围 0-100 */
    @DCCValue("cutRange:100")
    private String cutRange;

    /* SC渠道黑名单 */
    @DCCValue("scBlankList:s02c02")
    private String scBlankList;

    /* 白名单用户列表，逗号分隔 */
    @DCCValue("whiteListUsers:EMPTY")
    private String whiteListUsers;

    /* 白名单开关 0关闭 1开启 */
    @DCCValue("whiteListSwitch:0")
    private String whiteListSwitch;

    public boolean isDowngradeSwitch() {
        return "1".equals(downgradeSwitch);
    }

    public boolean isCutRange(String userId) {
        int hashcode = Math.abs(userId.hashCode());

        int lastTwoDigits = hashcode % 100;

        if (lastTwoDigits <= Integer.parseInt(cutRange)) {
            return true;
        }

        return false;
    }

    public boolean isSCBlankIntercept(String source, String channel) {
        List<String> list = Arrays.asList(scBlankList.split(Constants.SPLIT));
        return list.contains(source + channel);
    }

    public boolean isWhiteListSwitch() {
        return "1".equals(whiteListSwitch);
    }

    // TODO 后期优化：缓存已经解析的用户集，仅在配置更改时重新解析
    public boolean isInWhiteList(String userId) {
        if (!isWhiteListSwitch()) {
            return false;
        }

        if (StringUtils.isBlank(whiteListUsers) || "EMPTY".equals(whiteListUsers.trim())) {
            return false;
        }

        String[] userArray = whiteListUsers.split(",");
        for (String user : userArray) {
            if (user.trim().equals(userId.trim())) {
                return true;
            }
        }

        return false;
    }

}
