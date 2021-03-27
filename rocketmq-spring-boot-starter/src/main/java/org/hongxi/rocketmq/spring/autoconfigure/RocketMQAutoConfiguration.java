/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hongxi.rocketmq.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.hongxi.rocketmq.spring.autoconfigure.annotation.RocketMQMessageListener;
import org.hongxi.rocketmq.spring.autoconfigure.core.DefaultRocketMQListenerContainer;
import org.hongxi.rocketmq.spring.autoconfigure.core.RocketMQListener;
import org.hongxi.rocketmq.spring.autoconfigure.core.RocketMQTemplate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.impl.MQClientAPIImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.hongxi.rocketmq.spring.autoconfigure.enums.ConsumeMode;
import org.hongxi.rocketmq.spring.autoconfigure.core.DefaultRocketMQListenerContainerConstants;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass(MQClientAPIImpl.class)
@Order
@Slf4j
public class RocketMQAutoConfiguration {
    @Bean
    @ConditionalOnClass(DefaultMQProducer.class)
    @ConditionalOnMissingBean(DefaultMQProducer.class)
    @ConditionalOnProperty(prefix = "spring.rocketmq", value = {"nameServer", "producer.group"})
    @Order(1)
    public DefaultMQProducer mqProducer(RocketMQProperties rocketMQProperties) {

        RocketMQProperties.Producer producerConfig = rocketMQProperties.getProducer();
        String groupName = producerConfig.getGroup();
        Assert.hasText(groupName, "[spring.rocketmq.producer.group] must not be null");

        DefaultMQProducer producer = new DefaultMQProducer(producerConfig.getGroup());
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryAnotherBrokerWhenNotStoreOk());

        return producer;
    }

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    @ConditionalOnMissingBean(name = "rocketMQMessageObjectMapper")
    public ObjectMapper rocketMQMessageObjectMapper() {
        return new ObjectMapper();
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnBean(DefaultMQProducer.class)
    @ConditionalOnMissingBean(name = "rocketMQTemplate")
    @Order(2)
    public RocketMQTemplate rocketMQTemplate(DefaultMQProducer mqProducer,
                                             @Autowired(required = false)
        @Qualifier("rocketMQMessageObjectMapper")
            ObjectMapper objectMapper) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(mqProducer);
        if (Objects.nonNull(objectMapper)) {
            rocketMQTemplate.setObjectMapper(objectMapper);
        }

        return rocketMQTemplate;
    }

    @Configuration
    @ConditionalOnClass(DefaultMQPushConsumer.class)
    @EnableConfigurationProperties(RocketMQProperties.class)
    @ConditionalOnProperty(prefix = "spring.rocketmq", value = "nameServer")
    @Order
    public static class ListenerContainerConfiguration implements ApplicationContextAware, BeanFactoryAware, InitializingBean {
        private ConfigurableApplicationContext applicationContext;

        private DefaultListableBeanFactory beanFactory;

        private AtomicLong counter = new AtomicLong(0);

        @Resource
        private StandardEnvironment environment;

        @Resource
        private RocketMQProperties rocketMQProperties;

        private ObjectMapper objectMapper;

        public ListenerContainerConfiguration() {
        }

        @Autowired(required = false)
        public ListenerContainerConfiguration(
            @Qualifier("rocketMQMessageObjectMapper") ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }

        @Override
        public void afterPropertiesSet() {
            Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class);

            if (Objects.nonNull(beans)) {
                beans.forEach(this::registerContainer);
            }
        }

        private void registerContainer(String beanName, Object bean) {
            Class<?> clazz = AopUtils.getTargetClass(bean);

            if (!RocketMQListener.class.isAssignableFrom(bean.getClass())) {
                throw new IllegalStateException(clazz + " is not instance of " + RocketMQListener.class.getName());
            }

            RocketMQListener rocketMQListener = (RocketMQListener) bean;
            GenericBeanDefinition bd = (GenericBeanDefinition) beanFactory.getBeanDefinition(beanName);
            RocketMQMessageListener annotation = bd.getBeanClass().getAnnotation(RocketMQMessageListener.class);
            validate(annotation);
            BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultRocketMQListenerContainer.class);
            String nameServer = rocketMQProperties.getNameServer();
            String annotationNameServer = environment.resolvePlaceholders(annotation.nameServer());
            if (!StringUtils.isEmpty(annotationNameServer)) {
                nameServer = annotationNameServer;
                String instanceName = environment.resolvePlaceholders(annotation.instanceName());
                if (StringUtils.isEmpty(instanceName)) instanceName = nameServer;
                beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_INSTANCE_NAME, instanceName);
            }
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_NAMESERVER, nameServer);
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_TOPIC, environment.resolvePlaceholders(annotation.topic()));

            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_CONSUMER_GROUP, environment.resolvePlaceholders(annotation.consumerGroup()));
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_CONSUME_MODE, annotation.consumeMode());
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_CONSUME_THREAD_MAX, annotation.consumeThreadMax());
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_RECONSUME_TIMES, annotation.reconsumeTimes());
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_MESSAGE_MODEL, annotation.messageModel());
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_SELECTOR_EXPRESS, environment.resolvePlaceholders(annotation.selectorExpress()));
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_SELECTOR_TYPE, annotation.selectorType());
            beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_ROCKETMQ_LISTENER, rocketMQListener);
            if (Objects.nonNull(objectMapper)) {
                beanBuilder.addPropertyValue(DefaultRocketMQListenerContainerConstants.PROP_OBJECT_MAPPER, objectMapper);
            }
            beanBuilder.setDestroyMethodName(DefaultRocketMQListenerContainerConstants.METHOD_DESTROY);

            String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), counter.incrementAndGet());
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
            beanFactory.registerBeanDefinition(containerBeanName, beanBuilder.getBeanDefinition());

            DefaultRocketMQListenerContainer container = beanFactory.getBean(containerBeanName, DefaultRocketMQListenerContainer.class);

            if (!container.isStarted()) {
                try {
                    container.start();
                } catch (Exception e) {
                    log.error("started container failed. {}", container, e);
                    throw new RuntimeException(e);
                }
            }

            log.info("register rocketMQ listener to container, listenerBeanName:{}, containerBeanName:{}", beanName, containerBeanName);
        }

        private void validate(RocketMQMessageListener annotation) {
            if (annotation.consumeMode() == ConsumeMode.ORDERLY &&
                annotation.messageModel() == MessageModel.BROADCASTING)
                throw new BeanDefinitionValidationException("Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }
    }

}
