package com.tinuvile.infrastructure.dcc;


import com.tinuvile.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/7
 */
@Service
public class DCCService {

    /**
     * 降级开关 0关闭 1开启
     */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;
    
    /**
     * 切流范围 0-100
     */
    @DCCValue("cutRange:100")
    private String cutRange;

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

}
