package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.NotificationHandler;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandlerBeanFactoryProcessor
        implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
        for (String beanName : registry.getBeanDefinitionNames()) {
            final BeanDefinition definition = registry.getBeanDefinition(beanName);
            final Class<?> clazz = getBeanClass(definition);
            if (clazz == null || !NotificationHandler.class.isAssignableFrom(clazz)) {
                continue;
            }

            if (!AbstractNotificationHandler.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException(
                        "Spring bean " + clazz.getName() +
                                " implements NotificationHandler but must extend AbstractNotificationHandler"
                );
            }

            if (NotificationTemplate.byBindingKey(beanName) == null) {
                throw new IllegalStateException("No NotificationTemplate for handler bean: " + beanName);
            }

            if (!hasMessagePropertiesConstructor(clazz)) {
                throw new IllegalStateException(
                        "NotificationHandler " + clazz.getName() +
                                " must declare a constructor with MessageProperties argument"
                );
            }

            final MessageProperties props = bindMessageProperties(beanName);
            if (props == null) {
                throw new IllegalStateException("No message config for handler: " + beanName);
            }

            definition.getConstructorArgumentValues().addGenericArgumentValue(props);
        }
    }

    @Override
    public void postProcessBeanFactory(final org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) {
        // no-op: validation and constructor wiring happen in postProcessBeanDefinitionRegistry
    }

    private MessageProperties bindMessageProperties(final String beanName) {
        return Binder.get(this.environment)
                .bind("notification.messages." + toPropertyKey(beanName), Bindable.of(MessageProperties.class))
                .orElse(null);
    }

    private Class<?> getBeanClass(final BeanDefinition definition) {
        final String className = definition.getBeanClassName();
        if (className == null) {
            return null;
        }

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    private boolean hasMessagePropertiesConstructor(final Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                if (MessageProperties.class.equals(parameterType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String toPropertyKey(final String beanName) {
        final StringBuilder key = new StringBuilder();
        for (int i = 0; i < beanName.length(); i++) {
            final char ch = beanName.charAt(i);
            if (Character.isUpperCase(ch)) {
                key.append('-').append(Character.toLowerCase(ch));
            } else {
                key.append(ch);
            }
        }
        return key.toString();
    }
}
