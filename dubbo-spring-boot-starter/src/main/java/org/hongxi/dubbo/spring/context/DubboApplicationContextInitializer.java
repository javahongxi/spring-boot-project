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
package org.hongxi.dubbo.spring.context;

import org.hongxi.dubbo.spring.beans.factory.config.DubboConfigBeanDefinitionConflictProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * Dubbo {@link ApplicationContextInitializer} implementation
 *
 * @see ApplicationContextInitializer
 * @since 2.7.1
 */
public class DubboApplicationContextInitializer implements ApplicationContextInitializer, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        overrideBeanDefinitions(applicationContext);
    }

    private void overrideBeanDefinitions(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new DubboConfigBeanDefinitionConflictProcessor());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}