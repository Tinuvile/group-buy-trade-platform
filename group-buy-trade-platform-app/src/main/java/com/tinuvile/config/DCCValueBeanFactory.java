package com.tinuvile.config;

import com.tinuvile.types.annotations.DCCValue;
import com.tinuvile.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tinuvile
 * @description 基于Redis实现动态配置中心
 * @since 2025/11/7
 */
@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "group_buy_trade_platform_dcc_";

    private final RedissonClient redissonClient;

    private final Map<String, Object> dccObjGroup = new HashMap<>();

    public DCCValueBeanFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Bean("dccTopic")
    public RTopic dccRedisTopicListener(RedissonClient redissonClient) {
        RTopic topic = redissonClient.getTopic("group_buy_trade_platform_dcc");

        // 监听配置变更消息
        topic.addListener(String.class, (charSequence, s) -> {
            String[] split = s.split(Constants.SPLIT);

            String attribute = split[0];
            String key = BASE_CONFIG_PATH + attribute;
            String value = split[1];

            // 更新Redis中的值
            RBucket<String> bucket = redissonClient.getBucket(key);
            boolean exists = bucket.isExists();
            if (!exists) return;
            bucket.set(value);

            // 动态更新内存中的对象
            Object objBean = dccObjGroup.get(key);
            if (null == objBean) return;

            Class<?> objBeanClass = objBean.getClass();
            if (AopUtils.isAopProxy(objBean)) {
                objBeanClass = AopUtils.getTargetClass(objBean);
            }

            try {
                Field field = objBeanClass.getDeclaredField(attribute);
                field.setAccessible(true);
                field.set(objBean, value);
                field.setAccessible(false);

                log.info("DCC 节点监听，动态设置值 {} {}", key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return topic;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        
        // 处理AOP代理
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        if (AopUtils.isAopProxy(bean)) {
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }

        // 遍历所有字段
        Field[] fields = targetBeanClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DCCValue.class)) {
                // 跳过没有 @DCCValue 注解的字段
                continue;
            }

            // 处理 @DCCValue 注解的字段
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String value = dccValue.value();
            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(field.getName() + "@DCCValue is not config value config case 「isSwitch/isSwitch:1」");
            }

            String[] split = value.split(":");
            String key = BASE_CONFIG_PATH.concat(split[0]); // 构建Redis键名
            String defaultValue = split.length == 2 ? split[1] : null;

            String setValue = defaultValue;

            try {
                if (StringUtils.isBlank(defaultValue)) {
                    throw new RuntimeException("dcc config error " + key + " is not null - 请配置默认值！");
                }

                // 从Redis获取或设置配置值
                RBucket<String> bucket = redissonClient.getBucket(key);
                boolean exists = bucket.isExists();
                if (!exists) {
                    bucket.set(defaultValue);
                } else {
                    setValue = bucket.get();
                }

                // 通过反射设置字段值
                field.setAccessible(true);
                field.set(targetBeanObject, setValue);
                field.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dccObjGroup.put(key, targetBeanObject); // 记录对象引用，用于动态更新
        }
        return targetBeanObject;
    }
}
